package com.afrouzi.apporigin.data.export

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.afrouzi.apporigin.data.model.AppItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.coroutineContext

object ReportExporter {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

    suspend fun generateCsv(
        apps: List<AppItem>,
        onProgress: (current: Int, total: Int) -> Unit = { _, _ -> },
    ): String = withContext(Dispatchers.Default) {
        val sb = StringBuilder()
        val total = apps.size
        // Header
        sb.append("App Name,Package Name,Version Name,Version Code,Store Type,Has Update Owner,Update Owner Package,Installing Package,Initiating Package,Originating Package,Target SDK,Min SDK,Is System App,First Install Time,Last Update Time\n")

        apps.forEachIndexed { index, app ->
            coroutineContext.ensureActive()

            val firstInstall = if (app.firstInstallTime > 0) dateFormat.format(Date(app.firstInstallTime)) else ""
            val lastUpdate = if (app.lastUpdateTime > 0) dateFormat.format(Date(app.lastUpdateTime)) else ""

            sb.append("\"${escapeCsv(app.label)}\",")
            sb.append("\"${escapeCsv(app.packageName)}\",")
            sb.append("\"${escapeCsv(app.versionName)}\",")
            sb.append("${app.versionCode},")
            sb.append("\"${app.storeType.nameEn}\",")
            sb.append("${app.hasUpdateOwner},")
            sb.append("\"${escapeCsv(app.updateOwnerPackage ?: "")}\",")
            sb.append("\"${escapeCsv(app.installingPackage ?: "")}\",")
            sb.append("\"${escapeCsv(app.initiatingPackage ?: "")}\",")
            sb.append("\"${escapeCsv(app.originatingPackage ?: "")}\",")
            sb.append("${app.targetSdk},")
            sb.append("${app.minSdk},")
            sb.append("${app.isSystemApp},")
            sb.append("\"$firstInstall\",")
            sb.append("\"$lastUpdate\"\n")

            if (index % 10 == 0 || index == total - 1) {
                onProgress(index + 1, total)
            }
        }

        sb.toString()
    }

    suspend fun generateJson(
        apps: List<AppItem>,
        onProgress: (current: Int, total: Int) -> Unit = { _, _ -> },
    ): String = withContext(Dispatchers.Default) {
        val sb = StringBuilder()
        val total = apps.size
        sb.append("[\n")
        apps.forEachIndexed { index, app ->
            coroutineContext.ensureActive()

            val firstInstall = if (app.firstInstallTime > 0) dateFormat.format(Date(app.firstInstallTime)) else ""
            val lastUpdate = if (app.lastUpdateTime > 0) dateFormat.format(Date(app.lastUpdateTime)) else ""

            sb.append("  {\n")
            sb.append("    \"label\": \"${escapeJson(app.label)}\",\n")
            sb.append("    \"packageName\": \"${escapeJson(app.packageName)}\",\n")
            sb.append("    \"versionName\": \"${escapeJson(app.versionName)}\",\n")
            sb.append("    \"versionCode\": ${app.versionCode},\n")
            sb.append("    \"store\": \"${app.storeType.id}\",\n")
            sb.append("    \"storeName\": \"${app.storeType.nameEn}\",\n")
            sb.append("    \"hasUpdateOwner\": ${app.hasUpdateOwner},\n")
            sb.append("    \"updateOwnerPackage\": ${app.updateOwnerPackage?.let { "\"${escapeJson(it)}\"" } ?: "null"},\n")
            sb.append("    \"installingPackage\": ${app.installingPackage?.let { "\"${escapeJson(it)}\"" } ?: "null"},\n")
            sb.append("    \"initiatingPackage\": ${app.initiatingPackage?.let { "\"${escapeJson(it)}\"" } ?: "null"},\n")
            sb.append("    \"originatingPackage\": ${app.originatingPackage?.let { "\"${escapeJson(it)}\"" } ?: "null"},\n")
            sb.append("    \"targetSdk\": ${app.targetSdk},\n")
            sb.append("    \"minSdk\": ${app.minSdk},\n")
            sb.append("    \"isSystemApp\": ${app.isSystemApp},\n")
            sb.append("    \"firstInstallTime\": \"$firstInstall\",\n")
            sb.append("    \"lastUpdateTime\": \"$lastUpdate\"\n")
            sb.append("  }")
            if (index < total - 1) sb.append(",")
            sb.append("\n")

            if (index % 10 == 0 || index == total - 1) {
                onProgress(index + 1, total)
            }
        }
        sb.append("]\n")
        sb.toString()
    }

    fun shareReport(context: Context, fileName: String, content: String, mimeType: String, title: String) {
        try {
            val cacheFile = File(context.cacheDir, fileName)
            cacheFile.writeText(content, Charsets.UTF_8)

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                cacheFile,
            )

            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                type = mimeType
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TITLE, title)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            val chooser = Intent.createChooser(sendIntent, title).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(chooser)
        } catch (e: Exception) {
            // Fallback to text intent if file provider fails
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, content.take(50000))
                putExtra(Intent.EXTRA_TITLE, title)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            val chooser = Intent.createChooser(sendIntent, title).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            runCatching { context.startActivity(chooser) }
        }
    }

    private fun escapeCsv(text: String): String {
        return text.replace("\"", "\"\"")
    }

    private fun escapeJson(text: String): String {
        return text.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t")
    }
}
