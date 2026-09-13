package com.afrouzi.apporigin

import com.afrouzi.apporigin.data.model.AppItem
import com.afrouzi.apporigin.data.model.StoreType
import com.afrouzi.apporigin.domain.usecase.CategoryFilter
import com.afrouzi.apporigin.domain.usecase.FilterAndSortAppsUseCase
import com.afrouzi.apporigin.domain.usecase.SortOption
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FilterAndSortAppsUseCaseTest {

    private val useCase = FilterAndSortAppsUseCase()

    private val sampleApps = listOf(
        AppItem(
            packageName = "com.google.android.youtube",
            label = "YouTube",
            versionName = "19.0",
            versionCode = 1900,
            targetSdk = 34,
            minSdk = 26,
            storeType = StoreType.GOOGLE_PLAY,
            updateOwnerPackage = "com.android.vending",
            installingPackage = "com.android.vending",
            initiatingPackage = "com.android.vending",
            originatingPackage = null,
            isSystemApp = false,
            firstInstallTime = 1000L,
            lastUpdateTime = 5000L,
        ),
        AppItem(
            packageName = "org.fdroid.fdroid",
            label = "F-Droid",
            versionName = "1.19",
            versionCode = 1190,
            targetSdk = 33,
            minSdk = 26,
            storeType = StoreType.SIDELOAD,
            updateOwnerPackage = null,
            installingPackage = null,
            initiatingPackage = null,
            originatingPackage = null,
            isSystemApp = false,
            firstInstallTime = 2000L,
            lastUpdateTime = 4000L,
        ),
        AppItem(
            packageName = "com.android.settings",
            label = "Settings",
            versionName = "14",
            versionCode = 34,
            targetSdk = 34,
            minSdk = 26,
            storeType = StoreType.SYSTEM,
            updateOwnerPackage = "com.google.android.packageinstaller",
            installingPackage = null,
            initiatingPackage = null,
            originatingPackage = null,
            isSystemApp = true,
            firstInstallTime = 500L,
            lastUpdateTime = 500L,
        ),
    )

    @Test
    fun testFilterBySearchQuery() {
        val resultLabel = useCase(sampleApps, query = "You", selectedStore = null)
        assertEquals(1, resultLabel.size)
        assertEquals("YouTube", resultLabel.first().label)

        val resultPkg = useCase(sampleApps, query = "fdroid", selectedStore = null)
        assertEquals(1, resultPkg.size)
        assertEquals("F-Droid", resultPkg.first().label)
    }

    @Test
    fun testFilterByStore() {
        val resultPlay = useCase(sampleApps, query = "", selectedStore = StoreType.GOOGLE_PLAY)
        assertEquals(1, resultPlay.size)
        assertEquals(StoreType.GOOGLE_PLAY, resultPlay.first().storeType)

        val resultSideload = useCase(sampleApps, query = "", selectedStore = StoreType.SIDELOAD)
        assertEquals(1, resultSideload.size)
        assertEquals("F-Droid", resultSideload.first().label)
    }

    @Test
    fun testExcludeSystemApps() {
        val userOnly = useCase(sampleApps, query = "", selectedStore = null, categoryFilter = CategoryFilter.USER_ONLY)
        assertEquals(2, userOnly.size)
        assertTrue(userOnly.none { it.isSystemApp })
    }

    @Test
    fun testSorting() {
        val sortedByName = useCase(sampleApps, query = "", selectedStore = null, sortOption = SortOption.NAME)
        assertEquals("F-Droid", sortedByName[0].label)
        assertEquals("Settings", sortedByName[1].label)
        assertEquals("YouTube", sortedByName[2].label)

        val sortedByUpdate = useCase(sampleApps, query = "", selectedStore = null, sortOption = SortOption.LAST_UPDATED)
        assertEquals("YouTube", sortedByUpdate[0].label)
        assertEquals("F-Droid", sortedByUpdate[1].label)
        assertEquals("Settings", sortedByUpdate[2].label)
    }
}
