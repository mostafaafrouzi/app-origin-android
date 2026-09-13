package com.afrouzi.apporigin.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.afrouzi.apporigin.R

enum class NavigationItem(
    val route: String,
    @StringRes val titleRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    DASHBOARD(
        route = "dashboard",
        titleRes = R.string.nav_dashboard,
        selectedIcon = Icons.Filled.Dashboard,
        unselectedIcon = Icons.Outlined.Dashboard,
    ),
    APPS(
        route = "apps",
        titleRes = R.string.nav_apps,
        selectedIcon = Icons.Filled.Apps,
        unselectedIcon = Icons.Outlined.Apps,
    ),
    SETTINGS(
        route = "settings",
        titleRes = R.string.nav_settings,
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
    ),
    ABOUT(
        route = "about",
        titleRes = R.string.nav_about,
        selectedIcon = Icons.Filled.Info,
        unselectedIcon = Icons.Outlined.Info,
    )
}
