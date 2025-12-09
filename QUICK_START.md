# 🚀 Quick Start - GitHub Actions Build

## Step-by-Step (15 Menit)

### 1️⃣ Buat Repository di GitHub (3 menit)
1. Buka https://github.com → Login
2. Klik **New repository**
3. Nama: `auto-force-stop`
4. Private repository ✅
5. **Create repository**

### 2️⃣ Push Code (5 menit)

```bash
cd c:\xampp\htdocs\killApps

# Initialize git
git init
git add .
git commit -m "Initial commit"

# Push ke GitHub (ganti YOUR_USERNAME)
git remote add origin https://github.com/YOUR_USERNAME/auto-force-stop.git
git branch -M main
git push -u origin main
```

**Atau pakai GitHub Desktop:**
- Download: https://desktop.github.com
- Add local repo → Publish

### 3️⃣ Wait for Build (5-10 menit)
1. Buka repo di GitHub
2. Tab **Actions**
3. Wait sampai ✅ hijau

### 4️⃣ Download APK (2 menit)
1. Klik workflow yang ✅ sukses
2. Scroll bawah → **Artifacts**
3. Download **app-debug**
4. Extract ZIP → `app-debug.apk`

### 5️⃣ Install di HP
1. Transfer APK ke HP
2. Install
3. Enable Accessibility Service
4. Select apps
5. ✅ Done!

---

## 📋 Commands Reference

### First Time Push
```bash
cd c:\xampp\htdocs\killApps
git init
git add .
git commit -m "Initial commit"
git remote add origin https://github.com/YOUR_USERNAME/auto-force-stop.git
git branch -M main
git push -u origin main
```

### Update Code & Rebuild
```bash
# Edit files...
git add .
git commit -m "Update: description"
git push
# GitHub Actions auto build!
```

### Check Status
```bash
git status
git log --oneline
```

---

## ⚡ Tips

- **First build**: 5-10 menit
- **Subsequent builds**: 2-3 menit (dengan cache)
- **APK size**: ~2-3 MB
- **Download APK**: Actions tab → Artifacts
- **Retention**: APK disimpan 30 hari

---

## 🔗 Helpful Links

- **GitHub Actions Docs**: https://docs.github.com/en/actions
- **Git Cheat Sheet**: https://education.github.com/git-cheat-sheet-education.pdf
- **Android Developers**: https://developer.android.com

---

## ❓ Quick Troubleshooting

| Issue | Solution |
|-------|----------|
| Build failed | Re-run workflow |
| No Actions tab | Settings → Enable Actions |
| Artifact not found | Check build completed ✅ |
| APK won't install | Enable Unknown Sources |

---

## 📞 Need Help?

Lihat dokumentasi lengkap:
- **GITHUB_ACTIONS_GUIDE.md** - Detailed setup
- **walkthrough.md** - Complete project guide
- **README.md** - Technical documentation

**Ready?** Let's push to GitHub! 🚀
