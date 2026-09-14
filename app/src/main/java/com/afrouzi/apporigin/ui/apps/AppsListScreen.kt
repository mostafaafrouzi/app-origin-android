package com.afrouzi.apporigin.ui.apps

import android.graphics.drawable.Drawable
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.afrouzi.apporigin.R
import com.afrouzi.apporigin.data.model.AppItem
import com.afrouzi.apporigin.data.model.StoreType
import com.afrouzi.apporigin.domain.usecase.CategoryFilter
import com.afrouzi.apporigin.domain.usecase.FilterAndSortAppsUseCase
import com.afrouzi.apporigin.domain.usecase.OwnershipFilter
import com.afrouzi.apporigin.domain.usecase.SortOption
import com.afrouzi.apporigin.ui.components.AppCardItem
import com.afrouzi.apporigin.ui.theme.LocalAppSettings

@Composable
fun AppsListScreen(
    apps: List<AppItem>,
    initialStoreFilter: StoreType? = null,
    onIconLoad: (String) -> Drawable?,
    onAppClick: (AppItem) -> Unit,
    filterAndSortUseCase: FilterAndSortAppsUseCase,
) {
    val settings = LocalAppSettings.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedStore by remember(initialStoreFilter) { mutableStateOf(initialStoreFilter) }
    var ownershipFilter by remember { mutableStateOf(OwnershipFilter.ALL) }
    var sortOption by remember { mutableStateOf(SortOption.LAST_UPDATED) }

    BackHandler(enabled = searchQuery.isNotBlank()) {
        searchQuery = ""
    }
    BackHandler(enabled = searchQuery.isBlank() && selectedStore != null && initialStoreFilter == null) {
        selectedStore = null
    }
    BackHandler(enabled = searchQuery.isBlank() && selectedStore == null && ownershipFilter != OwnershipFilter.ALL) {
        ownershipFilter = OwnershipFilter.ALL
    }

    val filteredApps = remember(apps, searchQuery, selectedStore, ownershipFilter, sortOption, settings.showSystemApps) {
        val category = when {
            selectedStore == StoreType.SYSTEM -> CategoryFilter.SYSTEM_ONLY
            settings.showSystemApps -> CategoryFilter.ALL
            else -> CategoryFilter.USER_ONLY
        }
        filterAndSortUseCase(
            apps = apps,
            query = searchQuery,
            selectedStore = selectedStore,
            ownershipFilter = ownershipFilter,
            categoryFilter = category,
            sortOption = sortOption,
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp),
    ) {
        // Search Header
        Column(modifier = Modifier.padding(horizontal = 18.dp)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(stringResource(R.string.search_placeholder)) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = null)
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                ),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        val availableStores = remember(apps, settings.showSystemApps, selectedStore) {
            val storesInCatalog = apps.map { it.storeType }.toSet()
            val preferredOrder = listOf(
                StoreType.GOOGLE_PLAY,
                StoreType.FDROID,
                StoreType.AURORA,
                StoreType.GALAXY_STORE,
                StoreType.CAFE_BAZAAR,
                StoreType.MYKET,
                StoreType.AMAZON_APPSTORE,
                StoreType.HUAWEI_APPGALLERY,
                StoreType.XIAOMI_GETAPPS,
                StoreType.APKPURE,
                StoreType.APTOIDE,
                StoreType.OTHER_STORE,
                StoreType.SIDELOAD,
                StoreType.SYSTEM,
            )
            val ordered = preferredOrder.filter { it in storesInCatalog }
            val remaining = storesInCatalog.filter { it !in preferredOrder }
            val all = ordered + remaining
            if (settings.showSystemApps || selectedStore == StoreType.SYSTEM) {
                all
            } else {
                all.filter { it != StoreType.SYSTEM }
            }
        }

        // Store Filter Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            item {
                FilterChip(
                    selected = selectedStore == null,
                    onClick = { selectedStore = null },
                    label = { Text(stringResource(R.string.filter_all)) },
                    shape = RoundedCornerShape(12.dp),
                )
            }
            items(items = availableStores, key = { it.id }) { store ->
                FilterChip(
                    selected = selectedStore == store,
                    onClick = { selectedStore = if (selectedStore == store) null else store },
                    label = { Text(stringResource(store.nameRes)) },
                    shape = RoundedCornerShape(12.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Count header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.apps_count, filteredApps.size),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        // App List
        if (filteredApps.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 96.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.no_apps_found),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 4.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                items(
                    items = filteredApps,
                    key = { it.packageName },
                ) { app ->
                    AppCardItem(
                        app = app,
                        onIconLoad = onIconLoad,
                        onClick = { onAppClick(app) },
                    )
                }
            }
        }
    }
}
