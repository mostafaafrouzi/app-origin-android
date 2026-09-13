package com.afrouzi.apporigin.data.catalog

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.afrouzi.apporigin.data.model.StoreType

object StoreCatalog {

    const val PKG_GOOGLE_PLAY = "com.android.vending"
    const val PKG_CAFE_BAZAAR = "com.farsitel.bazaar"
    const val PKG_MYKET = "ir.mservices.market"
    const val PKG_GALAXY_STORE = "com.sec.android.app.samsungapps"
    const val PKG_GOOD_LOCK = "com.samsung.android.goodlock"
    const val PKG_FDROID = "org.fdroid.fdroid"
    const val PKG_FDROID_PRIV = "org.fdroid.fdroid.privileged"
    const val PKG_AURORA = "com.aurora.store"
    const val PKG_PACKAGE_INSTALLER_GOOGLE = "com.google.android.packageinstaller"
    const val PKG_PACKAGE_INSTALLER_AOSP = "com.android.packageinstaller"
    const val PKG_PACKAGE_INSTALLER_SAMSUNG = "com.samsung.android.packageinstaller"
    const val PKG_CHROME = "com.android.chrome"

    fun resolveStoreType(
        packageName: String,
        installingPackage: String?,
        initiatingPackage: String?,
        isSystemApp: Boolean,
    ): StoreType {
        // Progressive Web App (WebAPK)
        if (packageName.startsWith("org.chromium.webapk") ||
            packageName.startsWith("com.google.android.apps.chrome.webapk") ||
            installingPackage == PKG_CHROME
        ) {
            return StoreType.WEB_APK
        }

        // Primary inspection based on installingPackage
        return when (installingPackage) {
            PKG_GOOGLE_PLAY -> StoreType.GOOGLE_PLAY
            PKG_CAFE_BAZAAR -> StoreType.CAFE_BAZAAR
            PKG_MYKET -> StoreType.MYKET
            PKG_GALAXY_STORE -> StoreType.GALAXY_STORE
            PKG_GOOD_LOCK -> StoreType.GOOD_LOCK
            PKG_FDROID, PKG_FDROID_PRIV -> StoreType.FDROID
            PKG_AURORA -> StoreType.AURORA
            PKG_PACKAGE_INSTALLER_GOOGLE,
            PKG_PACKAGE_INSTALLER_AOSP,
            PKG_PACKAGE_INSTALLER_SAMSUNG -> StoreType.SIDELOAD
            null, "" -> {
                if (isSystemApp) {
                    StoreType.SYSTEM
                } else if (initiatingPackage in listOf(
                        PKG_PACKAGE_INSTALLER_GOOGLE,
                        PKG_PACKAGE_INSTALLER_AOSP,
                        PKG_PACKAGE_INSTALLER_SAMSUNG
                    )
                ) {
                    StoreType.SIDELOAD
                } else {
                    StoreType.DIRECT_ADB
                }
            }
            else -> {
                if (isSystemApp) StoreType.SYSTEM else StoreType.SIDELOAD
            }
        }
    }

    fun openInGooglePlay(context: Context, targetPackage: String) {
        openUriOrWeb(
            context = context,
            primaryUri = "market://details?id=$targetPackage",
            fallbackUrl = "https://play.google.com/store/apps/details?id=$targetPackage",
            storePackage = PKG_GOOGLE_PLAY,
        )
    }

    fun openInCafeBazaar(context: Context, targetPackage: String) {
        openUriOrWeb(
            context = context,
            primaryUri = "bazaar://details?id=$targetPackage",
            fallbackUrl = "https://cafebazaar.ir/app/$targetPackage",
            storePackage = PKG_CAFE_BAZAAR,
        )
    }

    fun openInMyket(context: Context, targetPackage: String) {
        openUriOrWeb(
            context = context,
            primaryUri = "myket://details?id=$targetPackage",
            fallbackUrl = "https://myket.ir/app/$targetPackage",
            storePackage = PKG_MYKET,
        )
    }

    fun openInGalaxyStore(context: Context, targetPackage: String) {
        openUriOrWeb(
            context = context,
            primaryUri = "samsungapps://ProductDetail/$targetPackage",
            fallbackUrl = "https://galaxystore.samsung.com/detail/$targetPackage",
            storePackage = PKG_GALAXY_STORE,
        )
    }

    fun searchInCafeBazaar(context: Context, query: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("bazaar://search?q=$query")
            setPackage(PKG_CAFE_BAZAAR)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        runCatching { context.startActivity(intent) }.onFailure {
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://cafebazaar.ir/search?q=$query")).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            runCatching { context.startActivity(webIntent) }
        }
    }

    fun searchInGooglePlay(context: Context, query: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("market://search?q=$query&c=apps")
            setPackage(PKG_GOOGLE_PLAY)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        runCatching { context.startActivity(intent) }.onFailure {
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/search?q=$query&c=apps")).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            runCatching { context.startActivity(webIntent) }
        }
    }

    fun searchInMyket(context: Context, query: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("myket://search?q=$query")
            setPackage(PKG_MYKET)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        runCatching { context.startActivity(intent) }.onFailure {
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://myket.ir/search/$query")).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            runCatching { context.startActivity(webIntent) }
        }
    }

    private fun openUriOrWeb(
        context: Context,
        primaryUri: String,
        fallbackUrl: String,
        storePackage: String? = null,
    ) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(primaryUri)).apply {
            if (!storePackage.isNullOrBlank()) {
                setPackage(storePackage)
            }
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        runCatching {
            context.startActivity(intent)
        }.onFailure {
            val fallbackIntent = Intent(Intent.ACTION_VIEW, Uri.parse(fallbackUrl)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            runCatching { context.startActivity(fallbackIntent) }
        }
    }
}
