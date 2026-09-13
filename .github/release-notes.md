## فارسی

نسخهٔ **۰.۱.۱** — بهبود جامع تجربه ناوبری، استانداردسازی دکمه برگشت (Back Navigation) و پیشگیری از خروج ناخواسته در اپلیکیشن **مارکتبان (AppOrigin)**.

**استانداردسازی عملکرد دکمه برگشت (Back Gesture & Button UX)**
- **ناوبری هوشمند بین تب‌ها (Tab Backstack):** فشردن دکمه برگشت در تب‌های برنامه‌ها، تنظیمات یا درباره، کاربر را به صورت استاندارد و روان به پیشخوان (Dashboard) بازمی‌گرداند.
- **پاکسازی هوشمند جستجو و فیلترها:** در صفحه برنامه‌ها، اولین بار فشردن دکمه برگشت متن جستجو را پاک کرده و فیلترهای اعمال‌شده را ریست می‌کند.
- **سلسله‌مراتب شیت‌های بازشو:** بازگشت چندمرحله‌ای هنگام باز بودن شیت‌های جزئیات و جستجوی استور (بازگشت به شیت قبلی بدون خروج از برنامه).
- **محافظت در برابر خروج ناخواسته (Double Back to Exit):** نمایش پیام هشدار («برای خروج، دوباره دکمه برگشت را بزنید») در صورت فشردن دکمه برگشت در صفحه پیشخوان، و خروج ایمن تنها در صورت فشردن مجدد ظرف ۲ ثانیه.

---

## English

**0.1.1** — Navigation UX overhaul, standardized Back Button/Gesture handling, and accidental exit protection in **AppOrigin (مارکتبان)**.

**Standardized Back Navigation Architecture**
- **Tab Backstack & Seamless Navigation:** Pressing back from any secondary tab (Apps, Settings, About) now returns to the root Dashboard instead of terminating the app.
- **Smart Search & Filter Clearance:** Pressing back on the Apps screen seamlessly clears active search queries and resets filters before navigating back.
- **Hierarchical Modal Dismissal:** Smooth multi-level sheet dismissal (closing store search sheets restores app inspection details without unexpected exits).
- **Accidental Exit Protection (Double-Tap to Exit):** Root Dashboard now prompts a localized safety toast ("Press back again to exit"), gracefully exiting only on a second back press within 2 seconds.

---

## Downloads

- **APK** for direct installation on Android devices
- **AAB** for Google Play Console submission

Both artifacts are signed with the project official release key.

`com.afrouzi.apporigin` · versionName 0.1.1 · versionCode 2 · minSdk 26 · targetSdk 35

---

Full documentation: [فارسی](https://github.com/mostafaafrouzi/app-origin-android/blob/main/README.md) · [English](https://github.com/mostafaafrouzi/app-origin-android/blob/main/README.en.md)

Built by [Mostafa Afrouzi](https://afrouzi.ir/?utm_source=github&utm_medium=release_notes&utm_campaign=apporigin)
