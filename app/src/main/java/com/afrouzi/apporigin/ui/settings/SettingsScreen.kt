package com.afrouzi.apporigin.ui.settings

import android.widget.Toast
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.TableChart
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.afrouzi.apporigin.R
import com.afrouzi.apporigin.data.export.ReportExporter
import com.afrouzi.apporigin.data.model.AppItem
import com.afrouzi.apporigin.data.prefs.AppFont
import com.afrouzi.apporigin.data.prefs.AppLanguage
import com.afrouzi.apporigin.data.prefs.AppSettings
import com.afrouzi.apporigin.data.prefs.DigitStyle
import com.afrouzi.apporigin.data.prefs.ThemeMode
import com.afrouzi.apporigin.data.prefs.ViewMode
import com.afrouzi.apporigin.ui.components.SegmentedControl
import com.afrouzi.apporigin.ui.theme.LocalAppSettings

@Composable
fun SettingsScreen(
    apps: List<AppItem>,
    onUpdateTheme: (ThemeMode) -> Unit,
    onUpdateLanguage: (AppLanguage) -> Unit,
    onUpdateFont: (AppFont) -> Unit,
    onUpdateDigits: (DigitStyle) -> Unit,
    onUpdateViewMode: (ViewMode) -> Unit,
    onToggleShowSystem: (Boolean) -> Unit,
    onRescan: () -> Unit,
) {
    val context = LocalContext.current
    val settings = LocalAppSettings.current
    val scrollState = rememberScrollState()

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

        // Appearance & Language
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

                Spacer(modifier = Modifier.height(16.dp))

                // Persian Font Selection
                Text(text = stringResource(R.string.settings_font), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                RadioRow(
                    label = stringResource(R.string.font_iran_sans),
                    subtitle = stringResource(R.string.font_sans_hint),
                    selected = settings.font == AppFont.IRAN_SANS,
                    onClick = { onUpdateFont(AppFont.IRAN_SANS) },
                )
                RadioRow(
                    label = stringResource(R.string.font_iran_yekan),
                    subtitle = stringResource(R.string.font_yekan_hint),
                    selected = settings.font == AppFont.IRAN_YEKAN,
                    onClick = { onUpdateFont(AppFont.IRAN_YEKAN) },
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Digits Style
                Text(text = stringResource(R.string.settings_digits), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                SegmentedControl(
                    items = listOf(DigitStyle.PERSIAN, DigitStyle.ENGLISH),
                    selectedItem = settings.digitStyle,
                    onItemSelected = onUpdateDigits,
                    labelProvider = { style ->
                        when (style) {
                            DigitStyle.PERSIAN -> stringResource(R.string.digits_persian)
                            DigitStyle.ENGLISH -> stringResource(R.string.digits_english)
                        }
                    },
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Display & Preferences
        SectionHeader(icon = Icons.Outlined.Tune, title = stringResource(R.string.settings_section_preferences))
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Default View Mode
                Text(text = stringResource(R.string.settings_default_mode), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                SegmentedControl(
                    items = listOf(ViewMode.CASUAL, ViewMode.PRO),
                    selectedItem = settings.viewMode,
                    onItemSelected = onUpdateViewMode,
                    labelProvider = { mode ->
                        when (mode) {
                            ViewMode.CASUAL -> stringResource(R.string.mode_casual)
                            ViewMode.PRO -> stringResource(R.string.mode_pro)
                        }
                    },
                )

                Spacer(modifier = Modifier.height(14.dp))

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

        // Data & Export
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
                        val csv = ReportExporter.generateCsv(apps)
                        ReportExporter.shareReport(context, csv, "text/csv", "AppOrigin_Audit_Report.csv")
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
                        val json = ReportExporter.generateJson(apps)
                        ReportExporter.shareReport(context, json, "application/json", "AppOrigin_Audit_Report.json")
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

@Composable
private fun RadioRow(
    label: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Spacer(modifier = Modifier.width(4.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
