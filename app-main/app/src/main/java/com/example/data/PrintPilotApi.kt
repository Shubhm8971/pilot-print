package com.example.data

import com.example.model.PrintOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

object PrintPilotApi {
    private const val endpoint = "https://print-pilot-ops.shubhmittal8971.workers.dev/webhooks/requests?sync=true"
    private const val webhookSecret = "local-demo-secret"
    private val client = OkHttpClient()

    suspend fun submit(order: PrintOrder): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val payload = JSONObject()
                .put("name", order.documents.joinToString(", ") { it.name })
                .put("pages", order.totalPages)
                .put("priority", if (order.config.isPriorityQueue) "High" else "Normal")
            val request = Request.Builder()
                .url(endpoint)
                .header("x-print-pilot-secret", webhookSecret)
                .post(payload.toString().toRequestBody("application/json".toMediaType()))
                .build()
            client.newCall(request).execute().use { response ->
                check(response.isSuccessful) { "Print Pilot service returned ${response.code}" }
            }
        }
    }
}
