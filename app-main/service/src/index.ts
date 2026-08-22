import { DurableObject } from "cloudflare:workers";

interface Env {
  RUNS: KVNamespace;
  REQUEST_LOCKS: DurableObjectNamespace<RequestLock>;
  NOTION_TOKEN: string;
  WEBHOOK_SECRET: string;
  NOTION_REQUESTS_DATA_SOURCE_ID: string;
  NOTION_RUN_LOG_DATA_SOURCE_ID: string;
  SHOP_WEBHOOK_URL: string;
}

interface NotionPage {
  id: string;
  url?: string;
  properties: Record<string, NotionProperty>;
}

interface NotionProperty {
  type: string;
  title?: Array<{ plain_text?: string }>;
  rich_text?: Array<{ plain_text?: string }>;
  number?: number | null;
  select?: { name?: string } | null;
  status?: { name?: string } | null;
  email?: string | null;
}

interface NotionListResponse {
  results: NotionPage[];
  has_more: boolean;
  next_cursor: string | null;
}

const NOTION_VERSION = "2026-03-11";

export default {
  async fetch(request: Request, env: Env, ctx: ExecutionContext): Promise<Response> {
    const url = new URL(request.url);

    if (request.method === "GET" && url.pathname === "/health") {
      return json({ ok: true, service: "print-pilot-ops" });
    }

    if (request.method === "POST" && url.pathname === "/webhooks/requests") {
      if (!authorized(request, env.WEBHOOK_SECRET)) return json({ error: "Unauthorized" }, 401);
      const payload = await readJson(request);
      if (payload) await createRequest(env, payload);
      if (url.searchParams.get("sync") === "true") {
        try {
          return json({ accepted: true, ...(await processRequests(env)) });
        } catch (error) {
          console.error(JSON.stringify({ event: "request.batch_failed", error: errorMessage(error) }));
          return json({ accepted: false, error: errorMessage(error) }, 502);
        }
      }
      ctx.waitUntil(processRequests(env).catch((error) => {
        console.error(JSON.stringify({ event: "request.batch_failed", error: errorMessage(error) }));
      }));
      return json({ accepted: true });
    }

    return json({ error: "Not found" }, 404);
  },

  async scheduled(_controller: ScheduledController, env: Env, ctx: ExecutionContext): Promise<void> {
    ctx.waitUntil(processRequests(env));
  }
};

async function processRequests(env: Env): Promise<{ processed: number; failed: number }> {
  const pages = await queryRequests(env, ["Submitted", "Approved", "Rejected"]);
  let processed = 0;
  let failed = 0;

  for (const page of pages) {
    const requestId = page.id;
    const currentStatus = propertyText(page.properties.Status);
    const titleText = propertyText(page.properties.Name) || "Untitled print request";
    const idempotencyKey = `${requestId}:${currentStatus}`;

    if (await env.RUNS.get(idempotencyKey)) continue;
    const lock = env.REQUEST_LOCKS.getByName(idempotencyKey);
    if (!(await lock.claim())) continue;

    try {
      if (currentStatus === "Submitted") {
        const reason = approvalReason(page);
        await updatePage(env, requestId, {
          Status: { status: { name: "Needs approval" } },
          "Automation summary": richText(`Reviewed by Print Pilot: ${reason}`)
        });
        await writeRun(env, {
          Event: "request.reviewed",
          Request: titleText,
          Action: "Moved request to Needs approval",
          Outcome: "Waiting for human approval",
          "Occurred at": new Date().toISOString(),
          "Idempotency key": idempotencyKey
        });
      } else if (currentStatus === "Approved") {
        await sendShopNotification(env, titleText, page.url);
        await updatePage(env, requestId, {
          Status: { status: { name: "Sent" } },
          "Automation summary": richText("Approval recorded in Notion; shop notification sent.")
        });
        await writeRun(env, {
          Event: "request.sent",
          Request: titleText,
          Action: "Sent print request to shop",
          Outcome: "Delivered",
          "Occurred at": new Date().toISOString(),
          "Idempotency key": idempotencyKey
        });
      } else if (currentStatus === "Rejected") {
        await updatePage(env, requestId, {
          "Automation summary": richText("Rejected by operator; no shop notification was sent.")
        });
        await writeRun(env, {
          Event: "request.rejected",
          Request: titleText,
          Action: "Recorded operator rejection",
          Outcome: "No external action taken",
          "Occurred at": new Date().toISOString(),
          "Idempotency key": idempotencyKey
        });
      }

      await env.RUNS.put(idempotencyKey, "done", { expirationTtl: 60 * 60 * 24 * 30 });
      await lock.complete();
      processed++;
    } catch (error) {
      await lock.release();
      failed++;
      try {
        await writeRun(env, {
          Event: "request.failed",
          Request: titleText,
          Action: "Process print request",
          Outcome: errorMessage(error),
          "Occurred at": new Date().toISOString(),
          "Idempotency key": idempotencyKey
        });
      } catch (logError) {
        console.error(JSON.stringify({ event: "request.failure_log_failed", requestId, error: errorMessage(logError) }));
      }
      console.error(JSON.stringify({ requestId, error }));
    }
  }

  return { processed, failed };
}

async function queryRequests(env: Env, statuses: string[]): Promise<NotionPage[]> {
  const pages: NotionPage[] = [];
  let cursor: string | null = null;

  do {
    const response: NotionListResponse = await notionFetch<NotionListResponse>(env, `/v1/data_sources/${env.NOTION_REQUESTS_DATA_SOURCE_ID}/query`, {
      method: "POST",
      body: JSON.stringify({
        filter: { or: statuses.map((name) => ({ property: "Status", status: { equals: name } })) },
        page_size: 50,
        ...(cursor ? { start_cursor: cursor } : {})
      })
    });
    pages.push(...response.results);
    cursor = response.has_more ? response.next_cursor : null;
  } while (cursor);

  return pages;
}

async function createRequest(env: Env, payload: Record<string, unknown>): Promise<void> {
  const name = stringValue(payload.name) || "Untitled print request";
  const pages = Math.max(1, numberValue(payload.pages));
  const priority = stringValue(payload.priority) || "Normal";
  await notionFetch(env, "/v1/pages", {
    method: "POST",
    body: JSON.stringify({
      parent: { data_source_id: env.NOTION_REQUESTS_DATA_SOURCE_ID },
      properties: {
        Name: title(name),
        Status: { status: { name: "Submitted" } },
        Pages: { number: pages },
        Priority: { select: { name: priority } },
        "Automation summary": richText("Received from Print Pilot Android app.")
      }
    })
  });
}

async function updatePage(env: Env, pageId: string, properties: Record<string, unknown>): Promise<void> {
  await notionFetch(env, `/v1/pages/${pageId}`, {
    method: "PATCH",
    body: JSON.stringify({ properties })
  });
}

async function writeRun(env: Env, properties: Record<string, unknown>): Promise<void> {
  await notionFetch(env, "/v1/pages", {
    method: "POST",
    body: JSON.stringify({
      parent: { data_source_id: env.NOTION_RUN_LOG_DATA_SOURCE_ID },
      properties: {
        Name: title(properties.Event as string),
        ...Object.fromEntries(Object.entries(properties).filter(([key]) => key !== "Event").map(([key, value]) => [key, richText(String(value))]))
      }
    })
  });
}

async function sendShopNotification(env: Env, titleText: string, pageUrl?: string): Promise<void> {
  const response = await fetch(env.SHOP_WEBHOOK_URL, {
    method: "POST",
    headers: { "content-type": "application/json" },
    body: JSON.stringify({ text: `Print Pilot approved: ${titleText}`, requestUrl: pageUrl })
  });
  if (!response.ok) throw new Error(`Shop notification failed with ${response.status}`);
}

async function notionFetch<T = unknown>(env: Env, path: string, init: RequestInit): Promise<T> {
  const response = await fetch(`https://api.notion.com${path}`, {
    ...init,
    headers: {
      Authorization: `Bearer ${env.NOTION_TOKEN}`,
      "Notion-Version": NOTION_VERSION,
      "content-type": "application/json",
      ...(init.headers || {})
    }
  });
  if (!response.ok) throw new Error(`Notion API failed with ${response.status}: ${await response.text()}`);
  return response.json<T>();
}

function approvalReason(page: NotionPage): string {
  const pages = propertyNumber(page.properties.Pages) ?? 0;
  const priority = propertyText(page.properties.Priority).toLowerCase();
  return pages > 100 || priority === "high"
    ? "Large or high-priority job; a human must approve it before sending."
    : "Standard job; a human still confirms the shop action before sending.";
}

function propertyText(property: NotionProperty | undefined): string {
  if (!property) return "";
  if (property.title) return property.title.map((item) => item.plain_text || "").join("");
  if (property.rich_text) return property.rich_text.map((item) => item.plain_text || "").join("");
  return property.select?.name || property.status?.name || property.email || "";
}

function propertyNumber(property: NotionProperty | undefined): number | null {
  return property?.number ?? null;
}

function title(value: string): { title: Array<{ text: { content: string } }> } {
  return { title: [{ text: { content: value.slice(0, 2000) } }] };
}

function richText(value: string): { rich_text: Array<{ text: { content: string } }> } {
  return { rich_text: [{ text: { content: value.slice(0, 2000) } }] };
}

function authorized(request: Request, secret: string): boolean {
  return request.headers.get("x-print-pilot-secret") === secret;
}

async function readJson(request: Request): Promise<Record<string, unknown> | null> {
  const contentType = request.headers.get("content-type") || "";
  if (!contentType.includes("application/json")) return null;
  const body: unknown = await request.json();
  return body && typeof body === "object" && !Array.isArray(body) ? body as Record<string, unknown> : null;
}

function stringValue(value: unknown): string {
  return typeof value === "string" ? value.trim() : "";
}

function numberValue(value: unknown): number {
  return typeof value === "number" && Number.isFinite(value) ? Math.round(value) : 1;
}

export class RequestLock extends DurableObject<Env> {
  constructor(ctx: DurableObjectState, env: Env) {
    super(ctx, env);
    ctx.blockConcurrencyWhile(async () => {
      this.ctx.storage.sql.exec("CREATE TABLE IF NOT EXISTS lock_state (id INTEGER PRIMARY KEY, state TEXT NOT NULL)");
    });
  }

  claim(): boolean {
    const existing = this.ctx.storage.sql.exec<{ state: string }>("SELECT state FROM lock_state WHERE id = 1").toArray()[0];
    if (existing) return false;
    this.ctx.storage.sql.exec("INSERT INTO lock_state (id, state) VALUES (1, 'processing')");
    return true;
  }

  complete(): void {
    this.ctx.storage.sql.exec("UPDATE lock_state SET state = 'done' WHERE id = 1");
  }

  release(): void {
    this.ctx.storage.sql.exec("DELETE FROM lock_state WHERE id = 1");
  }
}

function json(body: unknown, status = 200): Response {
  return new Response(JSON.stringify(body), { status, headers: { "content-type": "application/json" } });
}

function errorMessage(error: unknown): string {
  return error instanceof Error ? error.message : "Unknown error";
}