package com.afrouzi.apporigin.data.repository

import android.graphics.drawable.Drawable
import com.afrouzi.apporigin.data.model.AppItem
import com.afrouzi.apporigin.data.model.AuditSummary
import com.afrouzi.apporigin.data.source.PackageManagerDataSource
import com.afrouzi.apporigin.domain.repository.PackageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PackageRepositoryImpl(
    private val dataSource: PackageManagerDataSource,
    private val scope: CoroutineScope,
) : PackageRepository {

    private val _installedApps = MutableStateFlow<List<AppItem>>(emptyList())
    override val installedApps: StateFlow<List<AppItem>> = _installedApps.asStateFlow()

    private val _auditSummary = MutableStateFlow(AuditSummary())
    override val auditSummary: StateFlow<AuditSummary> = _auditSummary.asStateFlow()

    private val _isScanning = MutableStateFlow(false)
    override val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    init {
        scope.launch(Dispatchers.Default) {
            refresh(force = true)
        }
    }

    override suspend fun refresh(force: Boolean) {
        if (_isScanning.value && !force) return
        _isScanning.value = true
        try {
            val apps = dataSource.getAllInstalledApps()
            _installedApps.value = apps
            _auditSummary.value = AuditSummary.fromApps(apps)
        } finally {
            _isScanning.value = false
        }
    }

    override fun getAppIcon(packageName: String): Drawable? {
        return dataSource.getAppIcon(packageName)
    }
}
