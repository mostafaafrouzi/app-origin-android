package com.afrouzi.apporigin

import com.afrouzi.apporigin.data.catalog.StoreCatalog
import com.afrouzi.apporigin.data.model.StoreType
import org.junit.Assert.assertEquals
import org.junit.Test

class StoreCatalogTest {

    @Test
    fun testKnownStoreIdentification() {
        assertEquals(
            StoreType.GOOGLE_PLAY,
            StoreCatalog.resolveStoreType("com.example.app", "com.android.vending", null, false)
        )
        assertEquals(
            StoreType.CAFE_BAZAAR,
            StoreCatalog.resolveStoreType("com.example.app", "com.farsitel.bazaar", null, false)
        )
        assertEquals(
            StoreType.MYKET,
            StoreCatalog.resolveStoreType("com.example.app", "ir.mservices.market", null, false)
        )
        assertEquals(
            StoreType.GALAXY_STORE,
            StoreCatalog.resolveStoreType("com.example.app", "com.sec.android.app.samsungapps", null, false)
        )
        assertEquals(
            StoreType.FDROID,
            StoreCatalog.resolveStoreType("com.example.app", "org.fdroid.fdroid", null, false)
        )
        assertEquals(
            StoreType.AURORA,
            StoreCatalog.resolveStoreType("com.example.app", "com.aurora.store", null, false)
        )
        assertEquals(
            StoreType.AMAZON_APPSTORE,
            StoreCatalog.resolveStoreType("com.example.app", "com.amazon.venezia", null, false)
        )
        assertEquals(
            StoreType.HUAWEI_APPGALLERY,
            StoreCatalog.resolveStoreType("com.example.app", "com.huawei.appmarket", null, false)
        )
        assertEquals(
            StoreType.XIAOMI_GETAPPS,
            StoreCatalog.resolveStoreType("com.example.app", "com.xiaomi.mipicks", null, false)
        )
        assertEquals(
            StoreType.APKPURE,
            StoreCatalog.resolveStoreType("com.example.app", "com.apkpure.aegis", null, false)
        )
        assertEquals(
            StoreType.APTOIDE,
            StoreCatalog.resolveStoreType("com.example.app", "cm.aptoide.pt", null, false)
        )
    }

    @Test
    fun testGenericInstallersAndSideload() {
        assertEquals(
            StoreType.DIRECT_ADB,
            StoreCatalog.resolveStoreType("com.example.app", null, null, false)
        )
        assertEquals(
            StoreType.SIDELOAD,
            StoreCatalog.resolveStoreType("com.example.app", null, "com.google.android.packageinstaller", false)
        )
        assertEquals(
            StoreType.SIDELOAD,
            StoreCatalog.resolveStoreType("com.example.app", "com.google.android.packageinstaller", null, false)
        )
        assertEquals(
            StoreType.SIDELOAD,
            StoreCatalog.resolveStoreType("com.example.app", "org.telegram.messenger", null, false)
        )
        assertEquals(
            StoreType.SYSTEM,
            StoreCatalog.resolveStoreType("com.android.settings", null, null, true)
        )
    }

    @Test
    fun testCustomThirdPartyStoreIdentification() {
        // Any custom store installer like kimstore or other unrecognized packages are preserved as OTHER_STORE
        assertEquals(
            StoreType.OTHER_STORE,
            StoreCatalog.resolveStoreType("com.example.app", "com.kimstore.app", null, false)
        )
        assertEquals(
            StoreType.OTHER_STORE,
            StoreCatalog.resolveStoreType("com.example.app", "custom.thirdparty.installer", null, false)
        )
    }
}
