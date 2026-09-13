# AppOrigin (مارکتبان)

[فارسی](README.md) · **English**

A native, zero-root, open-source Android utility to audit multi-store package origins and manage **App Update Ownership** on Android 14, 15, and 16.

<div dir="ltr">

[![Release](https://img.shields.io/github/v/release/mostafaafrouzi/app-origin-android?style=flat-square&color=0284C7)](https://github.com/mostafaafrouzi/app-origin-android/releases)
[![API](https://img.shields.io/badge/API-26%2B%20%28Android%208.0%2B%29-10B981?style=flat-square)](https://android-arsenal.com/api?level=26)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-7F52FF?style=flat-square&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4?style=flat-square&logo=android&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Zero Root](https://img.shields.io/badge/Root-Not%20Required-success?style=flat-square)](https://github.com/mostafaafrouzi/app-origin-android)
[![License](https://img.shields.io/badge/License-MIT-blue?style=flat-square)](LICENSE)

</div>

---

## The Problem: Store Fragmentation in Modern Android

In today's Android ecosystem, users rarely download all apps from a single store:
* **Google Play Store:** Global apps and Google ecosystem tools.
* **Local & Regional Stores (e.g., Cafe Bazaar, Myket):** Banking, fintech, ride-hailing, and local service applications.
* **OEM Stores (e.g., Samsung Galaxy Store, Xiaomi GetApps):** Good Lock modules and One UI customizations.
* **Direct Sideloaded APKs (Telegram, Web, GitHub):** Open-source tools, VPNs, and developer builds.

### The Breaking Change in Android 14+ (App Update Ownership)
Android 14 (API 34) introduced the **App Update Ownership** mechanism (`InstallSourceInfo.getUpdateOwnerPackageName()`):
1. **Unattended Silent Updates:** When an app is installed from Store A, Store A registers as the exclusive update owner and can seamlessly update the app in the background without prompting the user.
2. **Ownership Conflict Dialog:** If Store B attempts to update the app, Android suspends the process and issues an ownership conflict warning dialog.
3. **Orphaned Sideloaded APKs:** Apps installed manually via direct APKs have no registered update owner. They sit orphaned on the phone, never receiving automatic security patches.

**AppOrigin (مارکتبان)** audits these packages, traces their installers, and helps users resolve ownership conflicts.

---

## Key Features

### 1. Comprehensive Global & Regional Store Support
* Native recognition of install source, initiating package, and update owner across global and regional app stores:
  * **Global Ecosystem:** Google Play Store, F-Droid, Aurora Store, Amazon Appstore, APKPure, Aptoide.
  * **OEM Stores:** Samsung Galaxy Store, Huawei AppGallery, Xiaomi GetApps.
  * **Regional Markets:** Cafe Bazaar, Myket.
* **Dynamic Third-Party Store Recognition:** Any custom or emerging installer (e.g. Kimstore, customized package installers) is automatically identified as an independent store rather than lumped into generic sideloads.

### 2. Smart Store Search Picker
* When searching to claim or re-link official ownership of an app, a smart bottom sheet opens with context-aware ordering:
  * **Current Install Source First:** The store that originally installed the app (or owns updates) is prioritized at the top with a `Current Source` recommendation badge.
  * **Installed Stores Prioritized:** Available stores installed on your device are badged and open directly in their respective apps.
  * **Web & Search Fallbacks:** Convenient web search shortcuts for stores not installed on the device, plus direct Google Search for finding official APKs.

### 3. Dynamic Store Filters & Health Overview
* **Dynamic Filter Chips:** In the Apps view, filter chips are generated dynamically based only on stores that actually have installed apps on your device—no empty clutter.
* **Update Ownership Health Score:** Instant visibility into what percentage of your apps enjoy unattended background updates.
* **Orphaned Apps Resolution:** Instantly identifies apps with no update owner and provides 1-tap resolution shortcuts.

### 4. Dual UX: Casual vs Pro Inspect Modes
* **Casual Mode:** Clean, grouped iOS-style cards with squircle icons, store badges, and clear ownership indicators.
* **Pro Mode:** In-depth technical view showing Package Names, Version Codes, Version Names, Target SDKs, and install timestamps.
* **1-Tap ADB Command Copy:** Generates and copies the exact ADB command for inspecting any package:
  ```bash
  adb shell "dumpsys package <package_name> | grep -iE 'installer|originat|initiat|updateOwner'"
  ```

### 5. Persian & English Typography
* Bundled with **IRANSansX** and **IRANYekanX** Eco font families.
* Tuned with `PlatformTextStyle(includeFontPadding = false)` and `LineHeightStyle` to eliminate text clipping on Android.
* Choose between Persian numerals (۱۲۳۴) or English numerals (1234) in Settings.

### 6. Fully Bilingual (RTL & LTR)
* Seamless in-app language switching between **System Default**, **Persian (فارسی)**, and **English**.
* Dynamic `LocalLayoutDirection` switching for accurate right-to-left and left-to-right mirroring.

### 7. Data Audit & Export
* Export complete catalog of installed applications to an Excel-compatible **CSV** spreadsheet.
* Export structured **JSON** for programmatic processing and scripting.

---

## Technical Architecture

AppOrigin follows **Clean Architecture** principles and **MVI/MVVM**:

```
app/src/main/java/com/afrouzi/apporigin/
├── AppOriginApplication.kt          # Application class & dynamic locale management
├── data/
│   ├── model/                      # Data models: AppItem, StoreType, AuditSummary
│   ├── catalog/                    # StoreCatalog, deep links & search intent registry
│   ├── source/                     # PackageManagerDataSource & InstallSourceInfoResolver
│   ├── prefs/                      # DataStore SettingsRepository & AppSettings
│   ├── export/                     # CSV and JSON report exporter
│   └── repository/                 # PackageRepositoryImpl with memory caching
├── domain/
│   ├── repository/                 # PackageRepository interface
│   └── usecase/                    # Scan, filter, and sort use cases
└── ui/
    ├── theme/                      # Typography Fonts.kt, Color.kt & AppOriginTheme
    ├── navigation/                 # NavigationItem enum & bottom bar tabs
    ├── components/                 # SegmentedControl, AppCardItem, AppDetailSheet
    ├── dashboard/                  # DashboardScreen & health donut progress
    ├── apps/                       # AppsListScreen with filters & search
    ├── settings/                   # SettingsScreen for theme, language & export
    └── about/                      # AboutScreen with UTM tracking links
```

---

## Build & Run

### Prerequisites
* **Android Studio Ladybug (or newer)**
* **JDK 17**
* Android Device or Emulator running Android 8.0 (API 26) or higher

### Instructions
```bash
# Clone the repository
git clone https://github.com/mostafaafrouzi/app-origin-android.git
cd app-origin-android

# Build Debug APK
./gradlew assembleDebug

# Install via ADB on connected device
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

---

## Release CI/CD (GitHub Actions)

This repository includes a production-grade automated release workflow at `.github/workflows/release.yml`. Whenever a new version tag is pushed (e.g. `v0.1.0`):
1. Builds release APK and AAB with JDK 17 and Android SDK.
2. Signs artifacts with the project release keystore from GitHub Secrets.
3. Validates signatures using `apksigner`.
4. Creates a GitHub Release with bilingual release notes from `.github/release-notes.md`.

### Required GitHub Secrets
To enable automated signing in your GitHub repository, configure these 4 secrets in `Settings -> Secrets and variables -> Actions`:
* `KEYSTORE_BASE64`: Base64-encoded release `.jks` file
* `KEYSTORE_PASSWORD`: Keystore password
* `KEY_ALIAS`: Key alias (`apporigin`)
* `KEY_PASSWORD`: Key password

---

## Privacy & Permissions

* **Permission:** `<uses-permission android:name="android.permission.QUERY_ALL_PACKAGES" />`
  * Strictly required to inspect installed package metadata and ownership on the device.
* **No Internet Permission:** The application declares **NO** internet permission. All data remains 100% on your device.
* **No analytics, no telemetry, no advertisements.**

---

## Developer & Contact

Crafted with care by **Mostafa Afrouzi**:
* 🌐 **Website (Persian):** [afrouzi.ir](https://afrouzi.ir/?utm_source=apporigin&utm_medium=github_readme_en&utm_campaign=apporigin)
* 🌐 **Website (English):** [afrouzi.ir/en](https://afrouzi.ir/en/?utm_source=apporigin&utm_medium=github_readme_en&utm_campaign=apporigin)
* 🐙 **GitHub:** [github.com/mostafaafrouzi](https://github.com/mostafaafrouzi)
* 💼 **LinkedIn:** [linkedin.com/in/mostafaafrouzi](https://linkedin.com/in/mostafaafrouzi)
* 🛍️ **Cafe Bazaar:** [Developer Page](https://cafebazaar.ir/developer/057657612999)

---

## License

This project is licensed under the [MIT License](LICENSE).
