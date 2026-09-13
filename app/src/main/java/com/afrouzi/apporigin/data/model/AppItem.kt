package com.afrouzi.apporigin.data.model

import android.graphics.drawable.Drawable

data class AppItem(
    val packageName: String,
    val label: String,
    val versionName: String,
    val versionCode: Long,
    val targetSdk: Int,
    val minSdk: Int,
    val firstInstallTime: Long,
    val lastUpdateTime: Long,
    val isSystemApp: Boolean,
    val installingPackage: String?,
    val initiatingPackage: String?,
    val originatingPackage: String?,
    val updateOwnerPackage: String?,
    val storeType: StoreType,
    val icon: Drawable? = null,
) {
    /**
     * Whether the application has an active, registered update owner
     * that can perform silent/unattended background updates (Android 14+).
     */
    val hasUpdateOwner: Boolean
        get() = !updateOwnerPackage.isNullOrBlank()

    /**
     * An orphaned app is a user app installed without a recognized store update owner,
     * such as manual APK sideloads, meaning it will never receive automatic updates.
     */
    val isOrphaned: Boolean
        get() = !isSystemApp && storeType != StoreType.WEB_APK && !hasUpdateOwner

    /**
     * Checks if there is a conflict or mismatch between the installer store
     * and the update owner store.
     */
    val hasOwnershipMismatch: Boolean
        get() = !installingPackage.isNullOrBlank() &&
                !updateOwnerPackage.isNullOrBlank() &&
                installingPackage != updateOwnerPackage
}
