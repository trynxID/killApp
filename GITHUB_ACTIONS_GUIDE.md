# GitHub Actions Build Guide

## 🚀 Cara Build APK dengan GitHub Actions

### Step 1: Buat Repository di GitHub

1. **Buka GitHub** → https://github.com
2. **Login** (atau buat akun jika belum punya)
3. **Klik tombol "+" di kanan atas** → New repository
4. **Isi form:**
   - Repository name: `auto-force-stop` atau nama lain
   - Description: "Auto force stop app for Android"
   - Public atau Private: pilih **Private** (untuk personal use)
   - ❌ **JANGAN** centang "Add a README file"
   - Klik **Create repository**

### Step 2: Push Project ke GitHub

Buka terminal di folder project (`c:\xampp\htdocs\killApps`) dan jalankan:

```bash
# Initialize git repository
git init

# Add semua files
git add .

# Commit
git commit -m "Initial commit - Auto Force Stop App"

# Add remote (ganti USERNAME dengan username GitHub Anda)
git remote add origin https://github.com/USERNAME/auto-force-stop.git

# Push ke GitHub
git branch -M main
git push -u origin main
```

**⚠️ Jangan lupa ganti `USERNAME` dengan username GitHub Anda!**

### Step 3: GitHub Actions Akan Otomatis Berjalan

Setelah push:
1. Buka repository di GitHub
2. Klik tab **"Actions"**
3. Akan ada workflow "Android CI - Build APK" yang sedang running
4. Tunggu ~5-10 menit sampai selesai (icon ✅ hijau)

### Step 4: Download APK

1. **Di tab Actions**, klik pada workflow run yang sudah ✅ sukses
2. Scroll ke bawah ke section **"Artifacts"**
3. Klik **"app-debug"** untuk download ZIP
4. Extract ZIP → akan ada file `app-debug.apk`
5. Transfer APK ke HP Oppo dan install

---

## 🔄 Build Ulang (Rebuild)

Setiap kali Anda push update ke GitHub, workflow akan otomatis jalan lagi:

```bash
# Edit files...
git add .
git commit -m "Update: description perubahan"
git push
```

Atau trigger manual build:
1. Buka repository di GitHub
2. Tab **Actions**
3. Pilih workflow "Android CI - Build APK"
4. Klik **"Run workflow"** → Run workflow

---

## 📝 Workflow File yang Sudah Dibuat

File: `.github/workflows/android-build.yml`

**Isi workflow:**
- ✅ Checkout code dari repository
- ✅ Setup Java 11
- ✅ Build debug APK dengan Gradle
- ✅ Upload APK sebagai artifact
- ✅ Retention 30 hari (APK tersedia di GitHub selama 30 hari)

**Triggers:**
- Push ke branch `main` atau `master`
- Pull request
- Manual trigger via GitHub UI

---

## ⚙️ Alternative: Setup Git via GitHub Desktop

Jika tidak familiar dengan command line:

1. **Download GitHub Desktop**: https://desktop.github.com
2. **Install & Login**
3. **Add repository**:
   - File → Add local repository
   - Pilih folder: `c:\xampp\htdocs\killApps`
   - Klik "create a repository"
4. **Publish repository**:
   - Klik "Publish repository"
   - Uncheck "Keep this code private" jika mau public
   - Klik "Publish repository"

GitHub Actions akan otomatis jalan setelah publish!

---

## 🐛 Troubleshooting

### Build Gagal (❌ Red X)

1. Klik workflow yang failed
2. Klik job "build"
3. Lihat error di logs
4. Biasanya issue:
   - Gradle wrapper permission (sudah diatasi di workflow)
   - Dependency download issue (retry biasanya fix)

### Tidak Ada Tab Actions

- Repository mungkin Private dan Actions disabled
- Settings → Actions → General → Enable "Allow all actions"

### Artifact Tidak Muncul

- Build harus ✅ sukses dulu
- Scroll sampai bawah di workflow run page
- Jika masih tidak ada, check logs

---

## 📊 Build Status Badge (Optional)

Tambahkan badge di README untuk show build status:

```markdown
![Android CI](https://github.com/USERNAME/auto-force-stop/workflows/Android%20CI%20-%20Build%20APK/badge.svg)
```

Ganti `USERNAME` dengan username GitHub Anda.

---

## 🎯 Summary

**What You Need to Do:**
1. ✅ Create GitHub repository
2. ✅ Push project code (via git atau GitHub Desktop)
3. ✅ Wait for Actions to build (~5-10 menit)
4. ✅ Download APK dari Artifacts
5. ✅ Install ke HP

**Benefits:**
- 🚀 No need Android Studio di PC
- 🔄 Auto build on every push
- 💾 Artifact stored 30 days
- 📱 Bisa build dari mana saja (push from any device)
- 🔐 Private repository (code aman)

---

## 📞 Need Help?

Jika ada issue saat setup atau build gagal, screenshot error nya dan saya akan bantu troubleshoot!
