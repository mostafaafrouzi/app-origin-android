package com.afrouzi.apporigin.data.export

import android.content.Context
import android.content.Intent
import com.afrouzi.apporigin.data.model.AppItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ReportExporter {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

    fun generateCsv(apps: List<AppItem>): String {
        val sb = StringBuilder()
        // Header
        sb.append("App Name,Package Name,Version Name,Version Code,Store Type,Has Update Owner,Update Owner Package,Installing Package,Initiating Package,Originating Package,Target SDK,Min SDK,Is System App,First Install Time,Last Update Time\n")

        apps.forEach { app ->
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
        }

        return sb.toString()
    }

    fun generateJson(apps: List<AppItem>): String {
        val sb = StringBuilder()
        sb.append("[\n")
        apps.forEachIndexed { index, app ->
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
            if (index < apps.size - 1) sb.append(",")
            sb.append("\n")
        }
        sb.append("]\n")
        return sb.toString()
    }

    fun shareReport(context: Context, content: String, mimeType: String, title: String) {
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_TEXT, content)
            putExtra(Intent.EXTRA_TITLE, title)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val shareIntent = Intent.createChooser(sendIntent, title).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        runCatching { context.startActivity(shareIntent) }
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
