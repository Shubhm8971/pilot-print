# Print Pilot operations service

This Worker is the automation engine for the Notion Track submission. It polls a Notion `Print Requests` data source every five minutes, moves new requests to `Needs approval`, waits for a human status change in Notion, sends an external shop notification after `Approved`, and writes each transition to a Notion `Run Log` data source.

Each request/status pair is routed through a dedicated SQLite-backed Durable Object lock. This serializes overlapping cron and webhook deliveries, while KV retains the longer-lived audit idempotency marker.

## Operator runbook

Notion is the operating console. Create or review requests in `Print Requests`; the Worker owns the transitions and the `Run Log` is the audit trail.

| Status | Meaning | Operator action |
| --- | --- | --- |
| `Submitted` | New request waiting for automated review | Wait for the Worker |
| `Needs approval` | Reviewed and paused before the shop is contacted | Check the request, then choose `Approved` or `Rejected` |
| `Approved` | Human approved the external shop action | Wait for the Worker to send it |
| `Sent` | Shop notification delivered | No action required |
| `Rejected` | Human stopped the request | No action required |

For proof of a run, show the request status, its matching `Run Log` row, and the external webhook request. The Worker writes timestamps and idempotency keys; operators should never create audit rows by hand.

Rejected requests are also processed by the Worker: the decision is recorded in `Run Log`, the request receives a clear summary, and no shop notification is sent.

## Notion setup

Create and share these data sources with your Notion integration:

### Print Requests

Use these exact property names and types:

- `Name`: title
- `Status`: status with `Submitted`, `Needs approval`, `Approved`, `Rejected`, and `Sent`
- `Pages`: number
- `Priority`: select
- `Automation summary`: rich text

### Run Log

Use these exact property names and types:

- `Name`: title
- `Request`: rich text
- `Action`: rich text
- `Outcome`: rich text
- `Occurred at`: rich text
- `Idempotency key`: rich text

Copy the **data source IDs**, not only the parent database IDs, into the secrets below.

## Local setup

```powershell
npm install
Copy-Item .dev.vars.example .dev.vars
npm run types
npm run check
npm run dev
```

Set real values in `.dev.vars`. Never commit that file or a Notion token.

## Deploy

```powershell
npx wrangler kv namespace create RUNS
npx wrangler secret put NOTION_TOKEN
npx wrangler secret put WEBHOOK_SECRET
npx wrangler secret put NOTION_REQUESTS_DATA_SOURCE_ID
npx wrangler secret put NOTION_RUN_LOG_DATA_SOURCE_ID
npx wrangler secret put SHOP_WEBHOOK_URL
npm run deploy
```

Test the trigger without opening the Android app:

```powershell
Invoke-RestMethod http://localhost:8787/health
Invoke-RestMethod -Method Post -Uri http://localhost:8787/webhooks/requests -Headers @{"x-print-pilot-secret"="local-demo-secret"}
```

The external action is intentionally a generic webhook so it can target a shop's Slack, Discord, or automation endpoint during the demo. Replace it with email or Telegram once the core flow is proven.