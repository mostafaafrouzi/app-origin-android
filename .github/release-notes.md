## فارسی

نسخهٔ **۰.۱.۰** — انتشار عمومی اولیه اپلیکیشن **مارکتبان (AppOrigin)**: پایش جامع منابع نصب، تفکیک مارکت‌های جهانی و مدیریت مالکیت بروزرسانی (Update Ownership) در اندروید.

**پشتیبانی جامع از مارکت‌های بین‌المللی و منطقه‌ای (Zero-Root)**
- سازگاری کامل با استورهای جهانی: **Google Play Store**، **F-Droid**، **Aurora Store**، **Samsung Galaxy Store**، **Amazon Appstore**، **Huawei AppGallery**، **Xiaomi GetApps**، **APKPure**، **Aptoide**، **کافه بازار** و **مایکت**
- شناسایی داینامیک استورهای متفرقه و اختصاصی (Dynamic Third-Party Store Recognition)
- استخراج رسمی `InstallSourceInfo` و فیلد حیاتی `updateOwnerPackageName` در اندروید ۱۴، ۱۵ و ۱۶
- شناسایی برنامه‌های سرگردان (Orphaned Sideloaded APKs) که به دلیل نصب دستی فاقد آپدیت خودکار هستند

**پنجرهٔ هوشمند جستجوی استور (Smart Store Search Picker)**
- انتخاب استور هدف جهت جستجو و جایگزینی با نسخه رسمی
- اولویت‌بندی هوشمند: منبع نصب فعلی برنامه در صدر گزینه‌ها به عنوان انتخاب پیشنهادی قرار می‌گیرد
- تفکیک و اولویت‌دهی به استورهای نصب‌شده روی گوشی کاربر با باز شدن مستقیم درون اپلیکیشن استور
- جستجوی مستقیم در وب‌سایت استورهای جهانی و جستجوی رسمی گوگل برای فایل APK

**فیلترهای پویا و رابط کاربری سبک iOS**
- فیلترهای کپسولی پویا در صفحه برنامه‌ها: نمایش خودکار چیپ‌های استورهایی که روی دستگاه کاربر برنامه دارند
- پیشخوان گرافیکی با دونات چارت سلامت آپدیت و تفکیک توزیع استورها
- حالت دوگانه: **نمای ساده و کاربرپسند** برای عموم و **نمای مهندسی Pro** (Package ID، Target SDK، لاگ تاریخ‌ها و کپی دستور تخصصی ADB)
- کنترل‌های کپسولی (Segmented Control Pill) با انیمیشن‌های روان فلوید

**تایپوگرافی اصیل و پشتیبانی کامل دوزبانه**
- تجهیز به فونت‌های فاخر **ایران‌سنس X** و **ایران‌یکان X** سری Eco
- رفع ریشه‌ای مشکل بریدگی حروف در اندروید با حذف پدینگ ناخواسته و تنظیم خط کرسی
- پشتیبانی کامل دوزبانه (فارسی و انگلیسی) با چیدمان اصولی RTL و LTR
- انتخاب سبک نمایش ارقام (فارسی / انگلیسی) در تنظیمات

**خروجی داده‌ها**
- خروجی کامل لیست برنامه‌ها به فرمت‌های اکسل (CSV) و داده‌های ساختاریافته (JSON)

---

## English

**0.1.0** — Initial public release of **AppOrigin (مارکتبان)**: Multi-store package source auditor and App Update Ownership manager for modern Android.

**Comprehensive Global & Regional Store Support (100% Non-Root)**
- Full support for major international stores: **Google Play Store**, **F-Droid**, **Aurora Store**, **Samsung Galaxy Store**, **Amazon Appstore**, **Huawei AppGallery**, **Xiaomi GetApps**, **APKPure**, **Aptoide**, **Cafe Bazaar**, and **Myket**
- Dynamic third-party store recognition for custom installers
- Official utilization of `InstallSourceInfo` and `getUpdateOwnerPackageName()` on Android 14, 15, and 16
- Automatic detection of orphaned sideloaded APKs missing background security patches

**Smart Store Search Picker**
- Target store selector to find official updates and claim ownership
- Smart Prioritization: The current install source of the package is placed first as the recommended option
- Highlights stores already installed on the user's device for instant in-app deep linking
- Web store fallback and Google APK web search

**Dynamic Filters & iOS-Inspired Dual UX**
- Dynamic store filter chips based on stores present on the device
- Modern Health Overview dashboard with donut score and store distribution breakdown
- Dual UX Modes: **Casual Mode** (clean visual badges, simplified origins) and **Pro Mode** (package names, version codes, target SDKs, install timestamps, ADB inspect commands)
- Fluid segmented control pills and squircle grouped cards

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

`com.afrouzi.apporigin` · versionName 0.1.0 · versionCode 1 · minSdk 26 · targetSdk 35

---

Full documentation: [فارسی](https://github.com/mostafaafrouzi/app-origin-android/blob/main/README.md) · [English](https://github.com/mostafaafrouzi/app-origin-android/blob/main/README.en.md)

Built by [Mostafa Afrouzi](https://afrouzi.ir/?utm_source=github&utm_medium=release_notes&utm_campaign=apporigin)
