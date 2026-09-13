## فارسی

نسخهٔ **۱.۰.۰** — انتشار اولیه اپلیکیشن **مارکتبان (AppOrigin)**: پایش منبع دانلود و مدیریت مالکیت بروزرسانی (Update Ownership) در اندروید.

**مدیریت منبع نصب و مالکیت بروزرسانی (Zero-Root)**
- استخراج رسمی `InstallSourceInfo` و فیلد حیاتی `updateOwnerPackageName` در اندروید ۱۴، ۱۵ و ۱۶
- تفکیک دقیق مارکت‌ها: گوگل‌پلی استور، کافه بازار، مایکت، گلکسی استور سامسونگ، اف‌دروید و سایدلود دستی
- شناسایی برنامه‌های سرگردان (Orphaned Apps) که به دلیل نصب دستی، فاقد آپدیت خودکار هستند
- دکمه‌های پرش مستقیم و جستجو در استورها جهت جایگزینی با نسخه‌های رسمی

**رابط کاربری لوکس و دوگانه (سبک iOS)**
- پیشخوان گرافیکی با دونات چارت سلامت آپدیت و کارت‌های تعاملی مارکت‌ها
- حالت دوگانه: **نمای ساده و کاربرپسند** برای کاربران عادی و **نمای مهندسی Pro** (شامل Package ID، Target SDK، لاگ تاریخ‌ها و کپی دستور تخصصی ADB) برای توسعه‌دهندگان
- کنترل‌های کپسولی (Segmented Control Pill) با انیمیشن‌های روان فلوید
- پالت رنگی مدرن Dark Glassmorphic و Light Mode

**تایپوگرافی اصیل و پشتیبانی کامل دوزبانه**
- تجهیز به فونت‌های فاخر **ایران‌سنس X** و **ایران‌یکان X** سری Eco
- رفع ریشه‌ای مشکل بریدگی حروف در اندروید با اعمال خط کرسی دقیق و حذف پدینگ ناخواسته
- پشتیبانی کامل دوزبانه (فارسی و انگلیسی) با چیدمان اصولی و خودکار RTL و LTR
- انتخاب سبک نمایش ارقام (فارسی / انگلیسی) در تنظیمات

**خروجی داده‌ها**
- خروجی کامل لیست برنامه‌ها به فرمت‌های اکسل (CSV) و داده‌های ساختاریافته (JSON) جهت پردازش در سیستم

---

## English

**1.0.0** — Initial release of **AppOrigin (مارکتبان)**: Multi-store source tracking and App Update Ownership manager for modern Android.

**Update Ownership & Store Tracking (100% Non-Root)**
- Official utilization of `InstallSourceInfo` and `getUpdateOwnerPackageName()` on Android 14, 15, and 16
- Granular store recognition: Google Play Store, Cafe Bazaar, Myket, Samsung Galaxy Store, F-Droid, Good Lock, and Manual APK Sideloads
- Automatic detection of orphaned sideloaded APKs missing background security patches
- Deep-linking and store search actions to claim official update ownership

**iOS-Inspired Dual User Experience**
- Modern Health Overview dashboard with donut score and store distribution breakdown
- Dual UX Modes: **Casual Mode** (clean visual badges, simplified store origins) and **Pro Mode** (package names, version codes, target SDKs, install timestamps, ADB inspect commands)
- Fluid segmented control pills and squircle grouped cards
- Sleek dark glassmorphic and minimal light themes

**Typography & Full Localization**
- Bundled with **IRANSansX** and **IRANYekanX** typography
- Zero text clipping with customized line height styles and disabled font padding
- Full bilingual support (Persian & English) with seamless dynamic RTL & LTR mirroring
- Choice of Persian (۱۲۳۴) or English (1234) numerals

**Data Export**
- Export complete installed package catalog to CSV (Excel) and JSON

---

## Downloads

- **APK** for direct installation on Android devices
- **AAB** for Google Play Console submission

Both artifacts are signed with the project official release key.

`com.afrouzi.apporigin` · versionName 1.0.0 · versionCode 1 · minSdk 26 · targetSdk 35

---

Full documentation: [فارسی](https://github.com/mostafaafrouzi/app-origin-android/blob/main/README.md) · [English](https://github.com/mostafaafrouzi/app-origin-android/blob/main/README.en.md)

Built by [Mostafa Afrouzi](https://afrouzi.ir/?utm_source=github&utm_medium=release_notes&utm_campaign=apporigin)
