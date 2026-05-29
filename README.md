# MD Clicker Shift Booking - Offline Version

## وصف المشروع

نسخة معدلة من تطبيق MD Clicker Shift Booking تعمل بشكل كامل بدون الحاجة للاتصال بـ Firebase.
جميع البيانات مخزنة محلياً على الجهاز باستخدام Room Database.

## التغييرات الرئيسية

- ✅ **تمت إزالة Firebase بالكامل** - لا حاجة للاتصال بالإنترنت
- ✅ **LicenseGate User ID: a26b9** - نظام تحقق محلي
- ✅ **تخزين محلي** - Room Database + EncryptedSharedPreferences
- ✅ **دعم كامل للغة العربية** - واجهة مستخدم عربية
- ✅ **نفس وظائف التطبيق الأصلي** - حجز النوبات بشكل آلي

## نظام التحقق LicenseGate

- LicenseGate User ID: **a26b9**
- يتم إنشاء مفتاح الترخيص محلياً بناءً على معرف الجهاز
- لا حاجة للتحقق من الخادم - كل شيء يعمل محلياً

## هيكل المشروع

```
app/src/main/java/com/mdclicker/shift/
├── ShiftApp.kt                 # Application class
├── data/
│   ├── AppDatabase.kt          # Room database
│   ├── Daos.kt                 # Data Access Objects
│   ├── Entities.kt             # Database entities
│   ├── LicenseGate.kt          # License verification
│   ├── LocalAuthManager.kt     # Local auth (encrypted)
│   └── ShiftRepository.kt      # Data repository
├── samurai/
│   └── SamuraiAutomationEngine.kt  # Automation engine
├── service/
│   ├── ShiftAccessibilityService.kt  # Accessibility service
│   └── BookingForegroundService.kt   # Foreground service
├── ui/
│   ├── LoginScreen.kt          # Login screen
│   ├── MainActivity.kt         # Main entry point
│   ├── MainScreen.kt           # Main screen
│   ├── MainViewModel.kt        # ViewModel
│   └── SettingsScreen.kt       # Settings
├── ui/theme/
│   ├── Color.kt                # Colors
│   └── Theme.kt                # Theme
└── util/
    └── PermissionUtils.kt      # Permissions helper
```

## المتطلبات

- Android Studio Hedgehog (2023.1.1) أو أحدث
- Min SDK: 26
- Target SDK: 34
- Kotlin 1.9.20
- Jetpack Compose

## البناء

1. افتح المشروع في Android Studio
2. انتظر حتى يتم تحميل dependencies
3. اضغط Build > Build Bundle(s) / APK(s) > Build APK(s)

أو عبر سطر الأوامر:
```bash
./gradlew assembleDebug
```

## LicenseGate User ID

LicenseGate User ID: **a26b9**
