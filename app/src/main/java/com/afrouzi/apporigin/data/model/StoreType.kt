package com.afrouzi.apporigin.data.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.afrouzi.apporigin.R

enum class StoreType(
    val id: String,
    val nameEn: String,
    val nameFa: String,
    val colorHex: Long,
    @StringRes val nameRes: Int,
) {
    GOOGLE_PLAY(
        id = "google_play",
        nameEn = "Google Play",
        nameFa = "گوگل پلی استور",
        colorHex = 0xFF00875A,
        nameRes = R.string.store_google_play,
    ),
    CAFE_BAZAAR(
        id = "cafe_bazaar",
        nameEn = "Cafe Bazaar",
        nameFa = "کافه بازار",
        colorHex = 0xFFF57C00,
        nameRes = R.string.store_cafe_bazaar,
    ),
    MYKET(
        id = "myket",
        nameEn = "Myket",
        nameFa = "مایکت",
        colorHex = 0xFFE53935,
        nameRes = R.string.store_myket,
    ),
    GALAXY_STORE(
        id = "galaxy_store",
        nameEn = "Galaxy Store",
        nameFa = "گلکسی استور",
        colorHex = 0xFF1976D2,
        nameRes = R.string.store_galaxy_store,
    ),
    GOOD_LOCK(
        id = "good_lock",
        nameEn = "Good Lock",
        nameFa = "افزونه‌های Good Lock",
        colorHex = 0xFF7B1FA2,
        nameRes = R.string.store_good_lock,
    ),
    FDROID(
        id = "fdroid",
        nameEn = "F-Droid",
        nameFa = "اف‌دروید (F-Droid)",
        colorHex = 0xFF0097A7,
        nameRes = R.string.store_fdroid,
    ),
    AURORA(
        id = "aurora",
        nameEn = "Aurora Store",
        nameFa = "آورورا استور",
        colorHex = 0xFF0288D1,
        nameRes = R.string.store_aurora,
    ),
    SIDELOAD(
        id = "sideload",
        nameEn = "Manual APK (Sideload)",
        nameFa = "نصب دستی فایل APK",
        colorHex = 0xFFE65100,
        nameRes = R.string.store_sideload,
    ),
    WEB_APK(
        id = "web_apk",
        nameEn = "Web App (PWA)",
        nameFa = "وب‌اپلیکیشن (PWA)",
        colorHex = 0xFF2E7D32,
        nameRes = R.string.store_pwa,
    ),
    SYSTEM(
        id = "system",
        nameEn = "System / Pre-installed",
        nameFa = "پیش‌فرض سیستمی",
        colorHex = 0xFF546E7A,
        nameRes = R.string.store_system,
    ),
    DIRECT_ADB(
        id = "direct_adb",
        nameEn = "Direct ADB / Unknown",
        nameFa = "نصب مستقیم با ADB",
        colorHex = 0xFF455A64,
        nameRes = R.string.store_adb_unknown,
    );

    val color: Color get() = Color(colorHex)
}
