package com.afrouzi.apporigin.domain.repository

import android.graphics.drawable.Drawable
import com.afrouzi.apporigin.data.model.AppItem
import com.afrouzi.apporigin.data.model.AuditSummary
import kotlinx.coroutines.flow.StateFlow

interface PackageRepository {
    val installedApps: StateFlow<List<AppItem>>
    val auditSummary: StateFlow<AuditSummary>
    val isScanning: StateFlow<Boolean>

    suspend fun refresh(force: Boolean = false)
    fun getAppIcon(packageName: String): Drawable?
}
