# MDClickerShiftOffline

## رفع المشروع إلى GitHub من Termux

### 1) تثبيت Git

داخل Termux:

```bash
pkg update -y
pkg install git -y
```

### 2) الدخول للمشروع

```bash
cd ~/MDClickerShiftOffline
```

### 3) تهيئة Git

```bash
git init
git add .
git commit -m "Initial upload"
```

### 4) ربط GitHub

```bash
git remote add origin https://github.com/Omerr36465/Clicker.git
```

### 5) رفع المشروع

```bash
git branch -M main
git push -u origin main
```

إذا طلب GitHub كلمة مرور استخدم Personal Access Token.

## GitHub Actions

بعد الرفع سيبدأ GitHub تلقائياً ببناء APK داخل Actions.

يمكن تحميل APK من:

GitHub → Actions → آخر Build → Artifacts
