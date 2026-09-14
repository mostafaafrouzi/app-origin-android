package com.afrouzi.apporigin

import com.afrouzi.apporigin.data.export.ReportExporter
import com.afrouzi.apporigin.data.model.AppItem
import com.afrouzi.apporigin.data.model.StoreType
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class ReportExporterTest {

    private val sampleApps = listOf(
        AppItem(
            packageName = "com.example.app",
            label = "Sample \"Quoted\" App",
            versionName = "1.0.0",
            versionCode = 10,
            targetSdk = 35,
            minSdk = 26,
            storeType = StoreType.GOOGLE_PLAY,
            updateOwnerPackage = "com.android.vending",
            installingPackage = "com.android.vending",
            initiatingPackage = null,
            originatingPackage = null,
            isSystemApp = false,
            firstInstallTime = 1700000000000L,
            lastUpdateTime = 1700005000000L,
        )
    )

    @Test
    fun testCsvExportFormat() = runBlocking {
        val csv = ReportExporter.generateCsv(sampleApps)
        assertTrue(csv.contains("App Name,Package Name,Version Name"))
        assertTrue(csv.contains("\"Sample \"\"Quoted\"\" App\""))
        assertTrue(csv.contains("\"com.example.app\""))
        assertTrue(csv.contains("\"Google Play\""))
        assertTrue(csv.contains("\"com.android.vending\""))
    }

    @Test
    fun testJsonExportFormat() = runBlocking {
        val json = ReportExporter.generateJson(sampleApps)
        assertTrue(json.startsWith("[\n"))
        assertTrue(json.contains("\"label\": \"Sample \\\"Quoted\\\" App\""))
        assertTrue(json.contains("\"packageName\": \"com.example.app\""))
        assertTrue(json.contains("\"store\": \"google_play\""))
        assertTrue(json.endsWith("]\n"))
    }
}
