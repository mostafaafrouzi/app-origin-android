package com.afrouzi.apporigin.data.source

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.LruCache
import com.afrouzi.apporigin.data.catalog.StoreCatalog
import com.afrouzi.apporigin.data.model.AppItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PackageManagerDataSource(private val context: Context) {

    private val packageManager: PackageManager = context.packageManager

    // In-memory icon cache to ensure 120Hz smooth scrolling
    private val iconCache = object : LruCache<String, Drawable>(250) {}

    suspend fun getAllInstalledApps(): List<AppItem> = withContext(Dispatchers.Default) {
        val flags = PackageManager.GET_META_DATA
        val installedPackages: List<PackageInfo> = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getInstalledPackages(PackageManager.PackageInfoFlags.of(flags.toLong()))
            } else {
                packageManager.getInstalledPackages(flags)
            }
        } catch (_: Exception) {
            emptyList()
        }

        installedPackages.mapNotNull { packageInfo ->
            runCatching {
                parsePackage(packageInfo)
            }.getOrNull()
        }.sortedBy { it.label.lowercase() }
    }

    private fun parsePackage(packageInfo: PackageInfo): AppItem {
        val packageName = packageInfo.packageName
        val appInfo = packageInfo.applicationInfo

        val isSystem = if (appInfo != null) {
            (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0 ||
                    (appInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0
        } else false

        val label = if (appInfo != null) {
            runCatching { appInfo.loadLabel(packageManager).toString() }.getOrDefault(packageName)
        } else {
            packageName
        }

        val versionName = packageInfo.versionName ?: ""
        val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            @Suppress("DEPRECATION")
            packageInfo.versionCode.toLong()
        }

        val targetSdk = appInfo?.targetSdkVersion ?: 0
        val minSdk = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            appInfo?.minSdkVersion ?: 0
        } else 0

        val firstInstall = packageInfo.firstInstallTime
        val lastUpdate = packageInfo.lastUpdateTime

        val rawSource = InstallSourceInfoResolver.resolve(packageManager, packageName)

        val storeType = StoreCatalog.resolveStoreType(
            packageName = packageName,
            installingPackage = rawSource.installingPackage,
            initiatingPackage = rawSource.initiatingPackage,
            isSystemApp = isSystem,
        )

        return AppItem(
            packageName = packageName,
            label = label.ifBlank { packageName },
            versionName = versionName,
            versionCode = versionCode,
            targetSdk = targetSdk,
            minSdk = minSdk,
            firstInstallTime = firstInstall,
            lastUpdateTime = lastUpdate,
            isSystemApp = isSystem,
            installingPackage = rawSource.installingPackage,
            initiatingPackage = rawSource.initiatingPackage,
            originatingPackage = rawSource.originatingPackage,
            updateOwnerPackage = rawSource.updateOwnerPackage,
            storeType = storeType,
            icon = null, // Loaded on demand via getAppIcon()
        )
    }

    fun getAppIcon(packageName: String): Drawable? {
        iconCache.get(packageName)?.let { return it }
        return try {
            val drawable = packageManager.getApplicationIcon(packageName)
            iconCache.put(packageName, drawable)
            drawable
        } catch (_: Exception) {
            null
        }
    }
}
