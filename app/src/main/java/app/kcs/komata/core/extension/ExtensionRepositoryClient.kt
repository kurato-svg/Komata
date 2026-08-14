package app.kcs.komata.core.extension

import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object ExtensionRepositoryClient {
    suspend fun load(url: String): RepositorySnapshot = suspendCoroutine { continuation ->
        Thread {
            try {
                continuation.resume(loadBlocking(url))
            } catch (error: Throwable) {
                continuation.resumeWithException(error)
            }
        }.start()
    }

    private fun loadBlocking(url: String): RepositorySnapshot {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.connectTimeout = 15_000
        connection.readTimeout = 20_000
        connection.instanceFollowRedirects = true
        connection.setRequestProperty("User-Agent", "Komata/0.2.0")
        connection.setRequestProperty("Accept", "application/json")

        try {
            val code = connection.responseCode
            if (code !in 200..299) {
                throw IllegalStateException("Repository returned HTTP $code")
            }

            val text = connection.inputStream.bufferedReader().use { it.readText() }
            val root = JSONObject(text)
            val name = root.optString("name").ifBlank { "Komata Repository" }
            val array = when {
                root.has("extensions") -> root.getJSONArray("extensions")
                root.has("plugins") -> root.getJSONArray("plugins")
                else -> JSONArray()
            }

            val items = buildList {
                for (index in 0 until array.length()) {
                    val item = array.getJSONObject(index)
                    val id = item.optString("id").trim()
                    val extName = item.optString("name").trim()
                    if (id.isBlank() || extName.isBlank()) continue
                    add(
                        ExtensionManifest(
                            id = id,
                            name = extName,
                            version = item.optString("version", "0.0.0"),
                            language = item.optString("language", item.optString("lang", "unknown")),
                            providerClass = item.optString("providerClass").takeIf { it.isNotBlank() },
                            downloadUrl = item.optString("downloadUrl", item.optString("url")).takeIf { it.isNotBlank() },
                            sourceUrl = item.optString("sourceUrl").takeIf { it.isNotBlank() },
                        ),
                    )
                }
            }

            return RepositorySnapshot(url = url, name = name, extensions = items)
        } finally {
            connection.disconnect()
        }
    }
}
