package com.afrouzi.apporigin.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.afrouzi.apporigin.R
import com.afrouzi.apporigin.AppOriginApplication
import com.afrouzi.apporigin.data.model.AppItem
import com.afrouzi.apporigin.data.model.StoreType
import com.afrouzi.apporigin.data.prefs.AppSettings
import com.afrouzi.apporigin.ui.about.AboutScreen
import com.afrouzi.apporigin.ui.apps.AppsListScreen
import com.afrouzi.apporigin.ui.components.AppDetailSheet
import com.afrouzi.apporigin.ui.dashboard.DashboardScreen
import com.afrouzi.apporigin.ui.navigation.NavigationItem
import com.afrouzi.apporigin.ui.settings.SettingsScreen
import com.afrouzi.apporigin.ui.theme.AppOriginTheme
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = AppOriginApplication.from(this)

        setContent {
            val settings by app.settingsRepository.settings.collectAsState(initial = AppSettings())

            AppOriginTheme(settings = settings) {
                AppOriginRoot(
                    app = app,
                    onOpenUrl = { url ->
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        runCatching { startActivity(intent) }
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppOriginRoot(
    app: AppOriginApplication,
    onOpenUrl: (String) -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val installedApps by app.packageRepository.installedApps.collectAsState()
    val auditSummary by app.packageRepository.auditSummary.collectAsState()
    val isScanning by app.packageRepository.isScanning.collectAsState()

    var currentTab by remember { mutableStateOf(NavigationItem.DASHBOARD) }
    var tabBackStack by remember { mutableStateOf(listOf(NavigationItem.DASHBOARD)) }

    var selectedAppForDetail by remember { mutableStateOf<AppItem?>(null) }
    var appForStoreSearch by remember { mutableStateOf<AppItem?>(null) }
    var appDetailBeforeSearch by remember { mutableStateOf<AppItem?>(null) }
    var presetStoreFilter by remember { mutableStateOf<StoreType?>(null) }

    var lastBackPressTime by remember { mutableLongStateOf(0L) }
    val exitToastMessage = stringResource(R.string.press_back_again_to_exit)

    fun switchTab(item: NavigationItem) {
        if (currentTab != item) {
            currentTab = item
            tabBackStack = (tabBackStack.filter { it != item }) + item
        }
    }

    // --- HIERARCHICAL BACK NAVIGATION ---

    // 1. Store Search Bottom Sheet: dismiss and restore Detail Sheet if opened from it
    BackHandler(enabled = appForStoreSearch != null) {
        appForStoreSearch = null
        if (appDetailBeforeSearch != null) {
            selectedAppForDetail = appDetailBeforeSearch
            appDetailBeforeSearch = null
        }
    }

    // 2. App Detail Bottom Sheet: dismiss
    BackHandler(enabled = selectedAppForDetail != null && appForStoreSearch == null) {
        selectedAppForDetail = null
    }

    // 3. Tab navigation backstack: return to previous tab or Dashboard
    BackHandler(
        enabled = currentTab != NavigationItem.DASHBOARD &&
            selectedAppForDetail == null &&
            appForStoreSearch == null
    ) {
        presetStoreFilter = null
        val updatedStack = tabBackStack.filter { it != currentTab }
        if (updatedStack.isNotEmpty()) {
            tabBackStack = updatedStack
            currentTab = updatedStack.last()
        } else {
            tabBackStack = listOf(NavigationItem.DASHBOARD)
            currentTab = NavigationItem.DASHBOARD
        }
    }

    // 4. Root Dashboard exit: double-tap back to exit with safety Toast
    BackHandler(
        enabled = currentTab == NavigationItem.DASHBOARD &&
            selectedAppForDetail == null &&
            appForStoreSearch == null
    ) {
        val now = System.currentTimeMillis()
        if (now - lastBackPressTime < 2000L) {
            (context as? Activity)?.finish()
        } else {
            lastBackPressTime = now
            Toast.makeText(context, exitToastMessage, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp,
            ) {
                NavigationItem.entries.forEach { item ->
                    val isSelected = currentTab == item
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { switchTab(item) },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = stringResource(item.titleRes),
                            )
                        },
                        label = {
                            Text(
                                text = stringResource(item.titleRes),
                                style = MaterialTheme.typography.labelMedium,
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                    )
                }
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when (currentTab) {
                NavigationItem.DASHBOARD -> {
                    DashboardScreen(
                        summary = auditSummary,
                        apps = installedApps,
                        isScanning = isScanning,
                        onRefresh = {
                            scope.launch { app.packageRepository.refresh(force = true) }
                        },
                        onSelectStoreFilter = { storeType ->
                            presetStoreFilter = storeType
                            switchTab(NavigationItem.APPS)
                        },
                        onAppClick = { appItem ->
                            selectedAppForDetail = appItem
                        },
                        onSearchStore = { appItem ->
                            appForStoreSearch = appItem
                        },
                    )
                }
                NavigationItem.APPS -> {
                    AppsListScreen(
                        apps = installedApps,
                        initialStoreFilter = presetStoreFilter,
                        onIconLoad = { pkg -> app.packageRepository.getAppIcon(pkg) },
                        onAppClick = { appItem ->
                            selectedAppForDetail = appItem
                        },
                        filterAndSortUseCase = app.filterAndSortAppsUseCase,
                    )
                }
                NavigationItem.SETTINGS -> {
                    SettingsScreen(
                        apps = installedApps,
                        onUpdateTheme = { mode ->
                            scope.launch { app.settingsRepository.setThemeMode(mode) }
                        },
                        onUpdateLanguage = { lang ->
                            scope.launch {
                                app.settingsRepository.setLanguage(lang)
                                app.applyLanguage(lang)
                            }
                        },
                        onToggleShowSystem = { show ->
                            scope.launch { app.settingsRepository.setShowSystemApps(show) }
                        },
                        onRescan = {
                            scope.launch { app.packageRepository.refresh(force = true) }
                        },
                    )
                }
                NavigationItem.ABOUT -> {
                    AboutScreen(onOpenUrl = onOpenUrl)
                }
            }
        }
    }

    // App Detail Modal Bottom Sheet
    selectedAppForDetail?.let { appItem ->
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        AppDetailSheet(
            app = appItem,
            sheetState = sheetState,
            onDismiss = { selectedAppForDetail = null },
            onIconLoad = { pkg -> app.packageRepository.getAppIcon(pkg) },
            onSearchStore = { targetApp ->
                appDetailBeforeSearch = selectedAppForDetail
                selectedAppForDetail = null
                appForStoreSearch = targetApp
            },
        )
    }

    // Store Search Picker Modal Bottom Sheet
    appForStoreSearch?.let { appItem ->
        val searchSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        com.afrouzi.apporigin.ui.components.StoreSearchSheet(
            app = appItem,
            sheetState = searchSheetState,
            onDismiss = {
                appForStoreSearch = null
                if (appDetailBeforeSearch != null) {
                    selectedAppForDetail = appDetailBeforeSearch
                    appDetailBeforeSearch = null
                }
            },
        )
    }
}
