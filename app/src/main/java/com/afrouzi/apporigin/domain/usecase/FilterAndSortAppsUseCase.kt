package com.afrouzi.apporigin.domain.usecase

import com.afrouzi.apporigin.data.model.AppItem
import com.afrouzi.apporigin.data.model.StoreType

enum class OwnershipFilter {
    ALL, WITH_OWNER, NO_OWNER
}

enum class CategoryFilter {
    ALL, USER_ONLY, SYSTEM_ONLY
}

enum class SortOption {
    LAST_UPDATED, INSTALL_DATE, NAME, TARGET_SDK
}

class FilterAndSortAppsUseCase {

    operator fun invoke(
        apps: List<AppItem>,
        query: String,
        selectedStore: StoreType?,
        ownershipFilter: OwnershipFilter = OwnershipFilter.ALL,
        categoryFilter: CategoryFilter = CategoryFilter.ALL,
        sortOption: SortOption = SortOption.LAST_UPDATED,
    ): List<AppItem> {
        val trimmedQuery = query.trim().lowercase()

        return apps.asSequence()
            .filter { app ->
                // Search query match
                if (trimmedQuery.isNotBlank()) {
                    app.label.lowercase().contains(trimmedQuery) ||
                            app.packageName.lowercase().contains(trimmedQuery)
                } else true
            }
            .filter { app ->
                // Store type filter
                if (selectedStore != null) {
                    app.storeType == selectedStore
                } else true
            }
            .filter { app ->
                // Ownership filter
                when (ownershipFilter) {
                    OwnershipFilter.ALL -> true
                    OwnershipFilter.WITH_OWNER -> app.hasUpdateOwner
                    OwnershipFilter.NO_OWNER -> !app.hasUpdateOwner
                }
            }
            .filter { app ->
                // Category filter
                when (categoryFilter) {
                    CategoryFilter.ALL -> true
                    CategoryFilter.USER_ONLY -> !app.isSystemApp
                    CategoryFilter.SYSTEM_ONLY -> app.isSystemApp
                }
            }
            .sortedWith { a, b ->
                when (sortOption) {
                    SortOption.LAST_UPDATED -> b.lastUpdateTime.compareTo(a.lastUpdateTime)
                    SortOption.INSTALL_DATE -> b.firstInstallTime.compareTo(a.firstInstallTime)
                    SortOption.NAME -> a.label.compareTo(b.label, ignoreCase = true)
                    SortOption.TARGET_SDK -> b.targetSdk.compareTo(a.targetSdk)
                }
            }
            .toList()
    }
}
