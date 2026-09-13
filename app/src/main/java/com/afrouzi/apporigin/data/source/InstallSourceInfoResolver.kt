package com.afrouzi.apporigin.data.source

import android.content.pm.PackageManager
import android.os.Build

data class RawInstallSource(
    val installingPackage: String?,
    val initiatingPackage: String?,
    val originatingPackage: String?,
    val updateOwnerPackage: String?,
)

object InstallSourceInfoResolver {

    fun resolve(packageManager: PackageManager, packageName: String): RawInstallSource {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val info = packageManager.getInstallSourceInfo(packageName)
                val updateOwner = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    info.updateOwnerPackageName
                } else {
                    null
                }
                RawInstallSource(
                    installingPackage = info.installingPackageName,
                    initiatingPackage = info.initiatingPackageName,
                    originatingPackage = info.originatingPackageName,
                    updateOwnerPackage = updateOwner,
                )
            } else {
                @Suppress("DEPRECATION")
                val installer = packageManager.getInstallerPackageName(packageName)
                RawInstallSource(
                    installingPackage = installer,
                    initiatingPackage = null,
                    originatingPackage = null,
                    updateOwnerPackage = null,
                )
            }
        } catch (_: Exception) {
            RawInstallSource(null, null, null, null)
        }
    }
}
