package com.afrouzi.apporigin.data.catalog

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.afrouzi.apporigin.data.model.AppItem
import com.afrouzi.apporigin.data.model.StoreType

data class StoreSearchTarget(
    val storeType: StoreType,
    val nameEn: String,
    val nameFa: String,
    val packageName: String?,
    val isInstalled: Boolean,
    val isCurrentSource: Boolean = false,
)

object StoreCatalog {

    // Major Global & Regional Store Package Identifiers
    const val PKG_GOOGLE_PLAY = "com.android.vending"
    const val PKG_FDROID = "org.fdroid.fdroid"
    const val PKG_FDROID_PRIV = "org.fdroid.fdroid.privileged"
    const val PKG_FDROID_BASIC = "org.fdroid.basic"
    const val PKG_AURORA = "com.aurora.store"
    const val PKG_AURORA_DROID = "com.aurora.adroid"
    const val PKG_GALAXY_STORE = "com.sec.android.app.samsungapps"
    const val PKG_GOOD_LOCK = "com.samsung.android.goodlock"
    const val PKG_AMAZON = "com.amazon.venezia"
    const val PKG_HUAWEI = "com.huawei.appmarket"
    const val PKG_XIAOMI = "com.xiaomi.mipicks"
    const val PKG_APKPURE = "com.apkpure.aegon"
    const val PKG_APTOIDE = "cm.aptoide.pt"
    const val PKG_CAFE_BAZAAR = "com.farsitel.bazaar"
    const val PKG_MYKET = "ir.mservices.market"
    const val PKG_CHROME = "com.android.chrome"

    // Generic OS package installers that indicate manual sideload
    val GENERIC_INSTALLERS = setOf(
        "com.google.android.packageinstaller",
        "com.android.packageinstaller",
        "com.samsung.android.packageinstaller",
        "com.miui.packageinstaller",
        "com.coloros.packageinstaller",
        "com.oppo.packageinstaller",
        "com.vivo.packageinstaller",
        "com.huawei.packageinstaller",
        "com.transsion.packageinstaller",
    )

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

        if (installingPackage.isNullOrBlank()) {
            return if (isSystemApp) {
                StoreType.SYSTEM
            } else if (initiatingPackage in GENERIC_INSTALLERS) {
                StoreType.SIDELOAD
            } else {
                StoreType.DIRECT_ADB
            }
        }

        // Check known stores
        return when {
            installingPackage == PKG_GOOGLE_PLAY -> StoreType.GOOGLE_PLAY
            installingPackage == PKG_CAFE_BAZAAR -> StoreType.CAFE_BAZAAR
            installingPackage == PKG_MYKET -> StoreType.MYKET
            installingPackage == PKG_GALAXY_STORE -> StoreType.GALAXY_STORE
            installingPackage == PKG_GOOD_LOCK -> StoreType.GOOD_LOCK
            installingPackage == PKG_FDROID || installingPackage == PKG_FDROID_PRIV || installingPackage == PKG_FDROID_BASIC || installingPackage.contains("fdroid") -> StoreType.FDROID
            installingPackage == PKG_AURORA || installingPackage == PKG_AURORA_DROID || installingPackage.contains("aurora") -> StoreType.AURORA
            installingPackage == PKG_AMAZON || installingPackage.contains("amazon.venezia") -> StoreType.AMAZON_APPSTORE
            installingPackage == PKG_HUAWEI || installingPackage.contains("appmarket") -> StoreType.HUAWEI_APPGALLERY
            installingPackage == PKG_XIAOMI || installingPackage.contains("mipicks") -> StoreType.XIAOMI_GETAPPS
            installingPackage == PKG_APKPURE || installingPackage.contains("apkpure") -> StoreType.APKPURE
            installingPackage == PKG_APTOIDE || installingPackage.contains("aptoide") -> StoreType.APTOIDE
            installingPackage.contains("kimstore") || installingPackage.contains("kimo") -> StoreType.OTHER_STORE
            installingPackage in GENERIC_INSTALLERS -> StoreType.SIDELOAD
            !isSystemApp -> StoreType.OTHER_STORE // Any custom store/installer is preserved as a store
            else -> StoreType.SYSTEM
        }
    }

    fun isStoreInstalled(context: Context, packageName: String?): Boolean {
        if (packageName.isNullOrBlank()) return false
        return runCatching {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        }.getOrDefault(false)
    }

    fun getSearchTargets(context: Context, app: AppItem): List<StoreSearchTarget> {
        val targets = mutableListOf<StoreSearchTarget>()

        fun makeTarget(type: StoreType, pkg: String?): StoreSearchTarget {
            return StoreSearchTarget(
                storeType = type,
                nameEn = type.nameEn,
                nameFa = type.nameFa,
                packageName = pkg,
                isInstalled = isStoreInstalled(context, pkg),
                isCurrentSource = (app.storeType == type || app.installingPackage == pkg || app.updateOwnerPackage == pkg),
            )
        }

        // Global Stores
        targets.add(makeTarget(StoreType.GOOGLE_PLAY, PKG_GOOGLE_PLAY))
        targets.add(makeTarget(StoreType.FDROID, PKG_FDROID))
        targets.add(makeTarget(StoreType.AURORA, PKG_AURORA))
        targets.add(makeTarget(StoreType.GALAXY_STORE, PKG_GALAXY_STORE))
        targets.add(makeTarget(StoreType.AMAZON_APPSTORE, PKG_AMAZON))
        targets.add(makeTarget(StoreType.HUAWEI_APPGALLERY, PKG_HUAWEI))
        targets.add(makeTarget(StoreType.XIAOMI_GETAPPS, PKG_XIAOMI))
        targets.add(makeTarget(StoreType.APKPURE, PKG_APKPURE))
        targets.add(makeTarget(StoreType.APTOIDE, PKG_APTOIDE))

        // Regional Stores
        targets.add(makeTarget(StoreType.CAFE_BAZAAR, PKG_CAFE_BAZAAR))
        targets.add(makeTarget(StoreType.MYKET, PKG_MYKET))

        // If the app was installed from a custom or third-party store not in the standard list
        if (app.storeType == StoreType.OTHER_STORE && !app.installingPackage.isNullOrBlank()) {
            targets.add(
                0,
                StoreSearchTarget(
                    storeType = StoreType.OTHER_STORE,
                    nameEn = app.installingPackage,
                    nameFa = app.installingPackage,
                    packageName = app.installingPackage,
                    isInstalled = isStoreInstalled(context, app.installingPackage),
                    isCurrentSource = true,
                )
            )
        }

        // Sort: Current source first, then installed on device, then rest
        return targets.sortedWith(
            compareByDescending<StoreSearchTarget> { it.isCurrentSource }
                .thenByDescending { it.isInstalled }
        )
    }

    fun executeSearch(
        context: Context,
        target: StoreType,
        query: String,
        targetPackage: String,
    ) {
        when (target) {
            StoreType.GOOGLE_PLAY -> searchInGooglePlay(context, query)
            StoreType.FDROID -> searchInFDroid(context, query)
            StoreType.AURORA -> searchInAurora(context, query)
            StoreType.GALAXY_STORE -> searchInGalaxyStore(context, query, targetPackage)
            StoreType.AMAZON_APPSTORE -> searchInAmazon(context, query)
            StoreType.HUAWEI_APPGALLERY -> searchInHuawei(context, query)
            StoreType.XIAOMI_GETAPPS -> searchInXiaomi(context, query)
            StoreType.APKPURE -> searchInAPKPure(context, query)
            StoreType.APTOIDE -> searchInAptoide(context, query)
            StoreType.CAFE_BAZAAR -> searchInCafeBazaar(context, query)
            StoreType.MYKET -> searchInMyket(context, query)
            else -> searchInWeb(context, query)
        }
    }

    fun searchInGooglePlay(context: Context, query: String) {
        openUriOrWeb(
            context = context,
            primaryUri = "market://search?q=$query&c=apps",
            fallbackUrl = "https://play.google.com/store/search?q=$query&c=apps",
            storePackage = PKG_GOOGLE_PLAY,
        )
    }

    fun searchInFDroid(context: Context, query: String) {
        openUriOrWeb(
            context = context,
            primaryUri = "market://search?q=$query",
            fallbackUrl = "https://search.f-droid.org/?q=$query",
            storePackage = PKG_FDROID,
        )
    }

    fun searchInAurora(context: Context, query: String) {
        openUriOrWeb(
            context = context,
            primaryUri = "market://search?q=$query",
            fallbackUrl = "https://play.google.com/store/search?q=$query&c=apps",
            storePackage = PKG_AURORA,
        )
    }

    fun searchInGalaxyStore(context: Context, query: String, targetPackage: String) {
        openUriOrWeb(
            context = context,
            primaryUri = "samsungapps://ProductDetail/$targetPackage",
            fallbackUrl = "https://galaxystore.samsung.com/detail/$targetPackage",
            storePackage = PKG_GALAXY_STORE,
        )
    }

    fun searchInAmazon(context: Context, query: String) {
        openUriOrWeb(
            context = context,
            primaryUri = "amzn://apps/android?s=$query",
            fallbackUrl = "https://www.amazon.com/s?k=$query&i=mobile-apps",
            storePackage = PKG_AMAZON,
        )
    }

    fun searchInHuawei(context: Context, query: String) {
        openUriOrWeb(
            context = context,
            primaryUri = "appmarket://search?keyword=$query",
            fallbackUrl = "https://appgallery.huawei.com/#/search/$query",
            storePackage = PKG_HUAWEI,
        )
    }

    fun searchInXiaomi(context: Context, query: String) {
        openUriOrWeb(
            context = context,
            primaryUri = "mimarket://search?keyword=$query",
            fallbackUrl = "https://global.app.mi.com/search?keyword=$query",
            storePackage = PKG_XIAOMI,
        )
    }

    fun searchInAPKPure(context: Context, query: String) {
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://apkpure.com/search?q=$query")).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        runCatching { context.startActivity(webIntent) }
    }

    fun searchInAptoide(context: Context, query: String) {
        openUriOrWeb(
            context = context,
            primaryUri = "aptoideinstall://search?query=$query",
            fallbackUrl = "https://en.aptoide.com/search?query=$query",
            storePackage = PKG_APTOIDE,
        )
    }

    fun searchInCafeBazaar(context: Context, query: String) {
        openUriOrWeb(
            context = context,
            primaryUri = "bazaar://search?q=$query",
            fallbackUrl = "https://cafebazaar.ir/search?q=$query",
            storePackage = PKG_CAFE_BAZAAR,
        )
    }

    fun searchInMyket(context: Context, query: String) {
        openUriOrWeb(
            context = context,
            primaryUri = "myket://search?q=$query",
            fallbackUrl = "https://myket.ir/search/$query",
            storePackage = PKG_MYKET,
        )
    }

    fun searchInWeb(context: Context, query: String) {
        val searchUrl = "https://www.google.com/search?q=" + Uri.encode("$query android app official download")
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        runCatching { context.startActivity(webIntent) }
    }

    fun openInStore(context: Context, targetType: StoreType, targetPackage: String) {
        when (targetType) {
            StoreType.GOOGLE_PLAY -> openInGooglePlay(context, targetPackage)
            StoreType.CAFE_BAZAAR -> openInCafeBazaar(context, targetPackage)
            StoreType.MYKET -> openInMyket(context, targetPackage)
            StoreType.GALAXY_STORE, StoreType.GOOD_LOCK -> openInGalaxyStore(context, targetPackage)
            StoreType.FDROID -> openUriOrWeb(context, "market://details?id=$targetPackage", "https://f-droid.org/en/packages/$targetPackage/", PKG_FDROID)
            StoreType.AURORA -> openUriOrWeb(context, "market://details?id=$targetPackage", "https://play.google.com/store/apps/details?id=$targetPackage", PKG_AURORA)
            StoreType.AMAZON_APPSTORE -> openUriOrWeb(context, "amzn://apps/android?p=$targetPackage", "https://www.amazon.com/dp/$targetPackage", PKG_AMAZON)
            StoreType.HUAWEI_APPGALLERY -> openUriOrWeb(context, "appmarket://details?id=$targetPackage", "https://appgallery.huawei.com/#/app/$targetPackage", PKG_HUAWEI)
            StoreType.APKPURE -> openUriOrWeb(context, "market://details?id=$targetPackage", "https://apkpure.com/search?q=$targetPackage", PKG_APKPURE)
            StoreType.APTOIDE -> openUriOrWeb(context, "aptoideinstall://package=$targetPackage", "https://en.aptoide.com/search?query=$targetPackage", PKG_APTOIDE)
            else -> searchInWeb(context, targetPackage)
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
