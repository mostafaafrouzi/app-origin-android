package com.afrouzi.apporigin.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.TableChart
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.afrouzi.apporigin.R
import com.afrouzi.apporigin.data.export.ReportExporter
import com.afrouzi.apporigin.data.model.AppItem
import com.afrouzi.apporigin.data.prefs.AppLanguage
import com.afrouzi.apporigin.data.prefs.ThemeMode
import com.afrouzi.apporigin.ui.components.SegmentedControl
import com.afrouzi.apporigin.ui.theme.LocalAppSettings
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    apps: List<AppItem>,
    onUpdateTheme: (ThemeMode) -> Unit,
    onUpdateLanguage: (AppLanguage) -> Unit,
    onToggleShowSystem: (Boolean) -> Unit,
    onRescan: () -> Unit,
) {
    val context = LocalContext.current
    val settings = LocalAppSettings.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    var isExporting by remember { mutableStateOf(false) }
    var exportProgress by remember { mutableStateOf(0 to 0) }
    var exportJob by remember { mutableStateOf<Job?>(null) }

    // Export Progress Dialog with Cancel button
    if (isExporting) {
        Dialog(
            onDismissRequest = {
                exportJob?.cancel()
                isExporting = false
            }
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center,
                    ) {
                        val (cur, tot) = exportProgress
                        val fraction = if (tot > 0) cur.toFloat() / tot else 0f
                        CircularProgressIndicator(
                            progress = { fraction },
                            modifier = Modifier.size(64.dp),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 4.dp,
                            trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                        )
                        Icon(
                            imageVector = Icons.Outlined.FileDownload,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp),
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = stringResource(R.string.export_progress_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    val (current, total) = exportProgress
                    val percent = if (total > 0) ((current * 100) / total) else 0
                    Text(
                        text = stringResource(R.string.export_progress_status, current, total, percent),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedButton(
                        onClick = {
                            exportJob?.cancel()
                            isExporting = false
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(stringResource(R.string.action_cancel))
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 18.dp, vertical = 14.dp)
            .padding(bottom = 96.dp),
    ) {
        Text(
            text = stringResource(R.string.settings_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Section 1: Appearance & Language
        SectionHeader(icon = Icons.Outlined.Palette, title = stringResource(R.string.settings_section_appearance))
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Theme Mode
                Text(text = stringResource(R.string.settings_theme), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                SegmentedControl(
                    items = listOf(ThemeMode.SYSTEM, ThemeMode.LIGHT, ThemeMode.DARK),
                    selectedItem = settings.themeMode,
                    onItemSelected = onUpdateTheme,
                    labelProvider = { mode ->
                        when (mode) {
                            ThemeMode.SYSTEM -> stringResource(R.string.theme_system)
                            ThemeMode.LIGHT -> stringResource(R.string.theme_light)
                            ThemeMode.DARK -> stringResource(R.string.theme_dark)
                        }
                    },
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Language
                Text(text = stringResource(R.string.settings_language), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                SegmentedControl(
                    items = listOf(AppLanguage.SYSTEM, AppLanguage.PERSIAN, AppLanguage.ENGLISH),
                    selectedItem = settings.language,
                    onItemSelected = onUpdateLanguage,
                    labelProvider = { lang ->
                        when (lang) {
                            AppLanguage.SYSTEM -> stringResource(R.string.language_system)
                            AppLanguage.PERSIAN -> "فارسی"
                            AppLanguage.ENGLISH -> "English"
                        }
                    },
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Section 2: Display & Preferences
        SectionHeader(icon = Icons.Outlined.Tune, title = stringResource(R.string.settings_section_preferences))
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Show System Apps Switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = stringResource(R.string.settings_show_system), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Text(text = stringResource(R.string.settings_show_system_sub), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Switch(
                        checked = settings.showSystemApps,
                        onCheckedChange = onToggleShowSystem,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Section 3: Data & Export
        SectionHeader(icon = Icons.Outlined.TableChart, title = stringResource(R.string.settings_section_data))
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Re-scan button
                OutlinedButton(
                    onClick = onRescan,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                ) {
                    Icon(Icons.Outlined.Refresh, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.rescan_packages))
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Export CSV
                OutlinedButton(
                    onClick = {
                        isExporting = true
                        exportProgress = 0 to apps.size
                        exportJob = scope.launch {
                            try {
                                val csv = ReportExporter.generateCsv(apps) { cur, tot ->
                                    exportProgress = cur to tot
                                }
                                ReportExporter.shareReport(
                                    context = context,
                                    fileName = "AppOrigin_Audit_Report.csv",
                                    content = csv,
                                    mimeType = "text/csv",
                                    title = "AppOrigin_Audit_Report.csv",
                                )
                            } finally {
                                isExporting = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                ) {
                    Icon(Icons.Outlined.TableChart, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(stringResource(R.string.export_csv), style = MaterialTheme.typography.titleSmall)
                        Text(stringResource(R.string.export_csv_sub), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Export JSON
                OutlinedButton(
                    onClick = {
                        isExporting = true
                        exportProgress = 0 to apps.size
                        exportJob = scope.launch {
                            try {
                                val json = ReportExporter.generateJson(apps) { cur, tot ->
                                    exportProgress = cur to tot
                                }
                                ReportExporter.shareReport(
                                    context = context,
                                    fileName = "AppOrigin_Audit_Report.json",
                                    content = json,
                                    mimeType = "application/json",
                                    title = "AppOrigin_Audit_Report.json",
                                )
                            } finally {
                                isExporting = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                ) {
                    Icon(Icons.Outlined.Code, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(stringResource(R.string.export_json), style = MaterialTheme.typography.titleSmall)
                        Text(stringResource(R.string.export_json_sub), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    }
}
