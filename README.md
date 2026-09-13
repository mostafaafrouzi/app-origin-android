# مارکتبان (AppOrigin)

**فارسی** · [English](README.en.md)

اپلیکیشن نیتیو، مدرن و متن‌باز اندروید برای ردیابی منبع دانلود برنامه‌ها، تفکیک مارکت‌ها و مدیریت مالکیت بروزرسانی (**App Update Ownership**) در اندروید ۱۴، ۱۵ و ۱۶ — **کاملاً بدون نیاز به روت (Zero-Root)**.

<div dir="ltr">

[![Release](https://img.shields.io/github/v/release/mostafaafrouzi/app-origin-android?style=flat-square&color=0284C7)](https://github.com/mostafaafrouzi/app-origin-android/releases)
[![API](https://img.shields.io/badge/API-26%2B%20%28Android%208.0%2B%29-10B981?style=flat-square)](https://android-arsenal.com/api?level=26)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-7F52FF?style=flat-square&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4?style=flat-square&logo=android&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Zero Root](https://img.shields.io/badge/Root-Not%20Required-success?style=flat-square)](https://github.com/mostafaafrouzi/app-origin-android)
[![License](https://img.shields.io/badge/License-MIT-blue?style=flat-square)](LICENSE)

</div>

---

## صورت مسئله و فلسفه وجودی

در گوشی‌های مدرن اندرویدی، کاربران برنامه‌های خود را از منابع گوناگون در سراسر جهان نصب می‌کنند:
* **گوگل پلی استور (Google Play):** منبع اصلی برنامه‌های استاندارد اندروید و ابزارهای گوگل.
* **استورهای متن‌باز (F-Droid و Aurora Store):** مخازن اپلیکیشن‌های آزاد، امنیتی و حفظ حریم خصوصی.
* **استورهای سازندگان گوشی (OEMs):** گلکسی استور سامسونگ (Good Lock و One UI)، شیائومی GetApps، اپ‌گالری هواوی.
* **استورهای جهانی متفرقه:** آمازون اپ‌استور (Amazon Appstore)، ای‌پی‌کی‌پیور (APKPure)، اپتوید (Aptoide).
* **استورهای منطقه‌ای و محلی (کافه بازار و مایکت):** برای برنامه‌های بانکی، فین‌تک و خدمات محلی.
* **نصب مستقیم فایل APK (سایدلود، تلگرام، گیت‌هاب):** ابزارهای تخصصی، نسخه‌های بتا و برنامه‌های اوپن‌سورس.

### تغییر سرنوشت‌ساز در اندروید ۱۴ به بعد (App Update Ownership)
سیستم‌عامل اندروید از نسخهٔ ۱۴ (API 34) سازوکار مالکیت انحصاری بروزرسانی (`InstallSourceInfo.getUpdateOwnerPackageName()`) را معرفی کرد:
1. **بروزرسانی بدون تأیید کاربر (Silent Unattended Updates):** استوری که مالکیت برنامه را در اختیار دارد، می‌تواند آپدیت‌ها را در پس‌زمینه بدون نیاز به تأیید مکرر کاربر نصب کند.
2. **تداخل مالکیت (Ownership Conflict):** اگر مارکت دیگری بخواهد همان برنامه را آپدیت کند، اندروید فرآیند را متوقف کرده و خطای تداخل مالکیت می‌دهد.
3. **اپلیکیشن‌های سرگردان (Orphaned Sideloaded Apps):** برنامه‌هایی که با فایل مستقیم APK نصب شده‌اند فاقد استور ثبت‌شده هستند؛ در نتیجه هیچ آپدیت خودکاری دریافت نکرده و به مرور زمان دچار ریسک‌های امنیتی می‌شوند.

**مارکتبان (AppOrigin)** ساخته شده تا این سردرگمی را برطرف کند، سلامت پکیج‌ها را بسنجد و مدیریت مارکت‌ها را در دست شما بگذارد.

---

## امکانات کلیدی

### ۱. پشتیبانی فراگیر از تمامی مارکت‌های جهانی و منطقه‌ای
* شناسایی رسمی منبع نصب و مالک آپدیت در استورهای گوگل‌پلی، اف‌دروید، آورورا استور، گلکسی استور، آمازون اپ‌استور، اپ‌گالری هواوی، گت‌اپس شیائومی، ای‌پی‌کی‌پیور، اپتوید، کافه بازار، مایکت و...
* **شناسایی پویای استورهای متفرقه (Dynamic Store Recognition):** در صورت نصب برنامه با هر استور سفارشی دیگر، عنوان و شناسه آن به عنوان استور مستقل شناسایی می‌شود.

### ۲. پنجره هوشمند جستجوی استور (Smart Store Search Picker)
* هنگام جستجو برای دریافت نسخه رسمی یک برنامه، پنجره‌ای اختصاصی باز شده و گزینه‌ها را با هوشمندی می‌چیند:
  * **منبع نصب کنونی (Current Source):** استوری که برنامه از آن نصب شده در صدر گزینه‌ها به عنوان انتخاب پیشنهادی قرار می‌گیرد.
  * **استورهای نصب‌شده روی دستگاه:** استورهایی که روی گوشی شما نصب هستند با بج سبز مشخص شده و با یک کلیک مستقیماً درون اپلیکیشن استور باز می‌شوند.
  * **استورهای وب و جستجوی فایل APK:** امکان جستجوی سریع در وب‌سایت استورها و موتور جستجوی گوگل برای فایل‌های بدون استور.

### ۳. فیلترهای پویا و پیشخوان سلامت
* **فیلترهای کپسولی پویا:** چیپ‌های فیلتر در صفحه برنامه‌ها تنها برای استورهایی نمایش داده می‌شوند که واقعاً روی گوشی شما برنامه دارند.
* محاسبه آنی **امتیاز سلامت بروزرسانی (Update Ownership Health Score)**.

### ۲. رابط کاربری دوگانه برای کاربران عادی و حرفه‌ای (Casual vs Pro Mode)
* **نمای ساده و زیبا (Casual Mode):** کارت‌های محصور سبک iOS با آیکون‌های استاندارد Squircle، برچسب مارکت و نشانگرهای ساده آپدیت خودکار.
* **نمای مهندسی (Pro Mode):** نمایش دقیق Package Name، مقادیر Version Code و Version Name، نسخه هدف Target SDK، تاریخ‌های دقیق نصب و آپدیت اولیه.
* **کپی تک‌کلیک دستور ADB:** تولید و کپی خودکار دستور تخصصی بررسی پکیج:
  ```bash
  adb shell "dumpsys package <package_name> | grep -iE 'installer|originat|initiat|updateOwner'"
  ```

### ۳. تایپوگرافی اصیل و حل مشکل حروف فارسی در اندروید
* تجهیز به فونت‌های فاخر **ایران‌سنس X** و **ایران‌یکان X** سری Eco.
* استفاده از `PlatformTextStyle(includeFontPadding = false)` و تنظیم دقیق `LineHeightStyle` که بریدگی کلمات فارسی در اندروید را به صورت ریشه‌ای حل می‌کند.
* انتخاب سبک نمایش ارقام (ارقام فارسی ۱۲۳۴ یا ارقام انگلیسی 1234) در تنظیمات.

### ۴. پشتیبانی کامل دوزبانه با چیدمان خودکار RTL و LTR
* تغییر آنی زبان برنامه میان **فارسی**، **English** و **پیش‌فرض سیستم**.
* چیدمان بی‌نقص راست‌چین (RTL) در زبان فارسی و چپ‌چین (LTR) در زبان انگلیسی بدون هیچ به‌هم‌ریختگی ظاهری.

### ۵. خروجی کامل و گزارش‌گیری داده‌ها
* امکان تولید و اشتراک‌گذاری گزارش کامل تمامی پکیج‌های نصب‌شده به فرمت **CSV (سازگار با اکسل)**.
* خروجی ساختاریافته به فرمت **JSON** برای برنامه‌نویسان و تحلیل داده‌ها با پایتون.

---

## معماری و زیرساخت فنی

این پروژه بر اساس اصول **Clean Architecture** و الگوی **MVI/MVVM** توسعه داده شده است:

```
app/src/main/java/com/afrouzi/apporigin/
├── AppOriginApplication.kt          # راه‌اندازی سراسری و مدیریت زبان
├── data/
│   ├── model/                      # مدل‌های داده AppItem، StoreType، AuditSummary
│   ├── catalog/                    # کاتالوگ استورها، الگوهای Deep Link و جستجو
│   ├── source/                     # رابط PackageManager و کلاس InstallSourceInfoResolver
│   ├── prefs/                      # ذخیره‌سازی ترجیحات با DataStore Preferences
│   ├── export/                     # موتور تولید گزارش‌های CSV و JSON
│   └── repository/                 # پیاده‌سازی PackageRepositoryImpl با کش حافظه
├── domain/
│   ├── repository/                 # اینترفیس PackageRepository
│   └── usecase/                    # یوزکیس‌های اسکن، فیلتر ترکیبی و مرتب‌سازی
└── ui/
    ├── theme/                      # تایپوگرافی Fonts.kt، رنگ‌ها و AppOriginTheme
    ├── navigation/                 # تعریف تب‌های NavigationItem
    ├── components/                 # کامپوننت‌های SegmentedControl، AppCardItem، AppDetailSheet
    ├── dashboard/                  # صفحه پیشخوان و دونات چارت سلامت
    ├── apps/                       # لیست برنامه‌ها با فیلترهای کپسولی
    ├── settings/                   # تنظیمات تم، زبان، فونت و خروجی داده
    └── about/                      # صفحه درباره ما با لینک‌های UTM
```

---

## کامپایل و اجرای پروژه (Build & Run)

### پیش‌نیازها
* **Android Studio Ladybug (یا نسخه جدیدتر)**
* **JDK 17**
* دستگاه یا شبیه‌ساز با اندروید ۸.۰ (API 26) به بالا

### مراحل اجرا
```bash
# کلون کردن ریپازیتوری
git clone https://github.com/mostafaafrouzi/app-origin-android.git
cd app-origin-android

# بیلد نسخه دیباگ
./gradlew assembleDebug

# نصب مستقیم روی گوشی متصل با ADB
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

---

## امضای ریلیز و انتشار خودکار (GitHub Actions CI/CD)

این ریپازیتوری مجهز به ورک‌فلو خودکار `.github/workflows/release.yml` است. با پوش هر تگ نسخه (مانند `v0.1.0`)، عملیات زیر انجام می‌شود:
1. راه‌اندازی محیط ساخت ابری با JDK 17 و Android SDK
2. استخراج Keystore امن از Secretهای گیت‌هاب
3. کامپایل نسخه‌های امضاشده APK و AAB
4. اعتبارسنجی خودکار امضا با `apksigner`
5. ایجاد خودکار GitHub Release همراه با Changelog دوزبانه از فایل `.github/release-notes.md`

### تنظیم سکرت‌های گیت‌هاب (Repository Secrets)
برای فعال‌سازی فرآیند ساخت ابری، ۴ مقدار زیر را در مسیر `Settings -> Secrets and variables -> Actions` ریپازیتوری خود اضافه کنید:
* `KEYSTORE_BASE64`: محتوای Base64 کلید ریلیز
* `KEYSTORE_PASSWORD`: رمز عبور مخزن کلید
* `KEY_ALIAS`: نام مستعار کلید (`apporigin`)
* `KEY_PASSWORD`: رمز عبور کلید

---

## حریم خصوصی و مجوزها

* **مجوز دسترسی:** `<uses-permission android:name="android.permission.QUERY_ALL_PACKAGES" />`
  * این مجوز فقط برای شناسایی منبع و سلامت برنامه‌های نصب‌شده روی دستگاه استفاده می‌شود.
* **بدون اتصال اینترنت:** این برنامه هیچ درخواستی به سرورهای خارجی ارسال نمی‌کند و داده‌های شما روی گوشی باقی می‌ماند.
* **بدون تبلیغات و بدون ردیابی.**

---

## توسعه‌دهنده و راه‌های ارتباطی

توسعه‌داده‌شده با افتخار توسط **مصطفی افروزی (Mostafa Afrouzi)**:
* 🌐 **وب‌سایت شخصی (فارسی):** [afrouzi.ir](https://afrouzi.ir/?utm_source=apporigin&utm_medium=github_readme&utm_campaign=apporigin)
* 🌐 **وب‌سایت شخصی (انگلیسی):** [afrouzi.ir/en](https://afrouzi.ir/en/?utm_source=apporigin&utm_medium=github_readme&utm_campaign=apporigin)
* 🐙 **گیت‌هاب:** [github.com/mostafaafrouzi](https://github.com/mostafaafrouzi)
* 💼 **لینکدین:** [linkedin.com/in/mostafaafrouzi](https://linkedin.com/in/mostafaafrouzi)
* 🛍️ **کافه بازار:** [صفحه برنامه‌ها در کافه‌بازار](https://cafebazaar.ir/developer/057657612999)

---

## لایسنس

این پروژه تحت مجوز [MIT License](LICENSE) منتشر شده است.
