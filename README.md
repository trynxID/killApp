# Auto Force Stop - Android App

Aplikasi Android ringan yang secara otomatis melakukan "Force Stop" pada aplikasi yang dipilih setelah Anda keluar dari aplikasi tersebut. Menggunakan AccessibilityService untuk mensimulasikan tap otomatis tanpa memerlukan root access atau ADB/Shizuku.

## 🎯 Fitur Utama

- ✅ Tidak perlu ROOT access
- ✅ Tidak perlu ADB atau Shizuku
- ✅ Menggunakan AccessibilityService (Robot Finger method)
- ✅ UI minimalis dengan RecyclerView
- ✅ Pilih aplikasi mana saja yang ingin di-force stop otomatis
- ✅ Kompatibel dengan ColorOS (Oppo)

## 📋 Persyaratan

- Android 7.0 (API 24) atau lebih tinggi
- AccessibilityService permission (harus diaktifkan manual)

## 🚀 Cara Penggunaan

### 1. Instalasi
- Build project menggunakan Android Studio
- Install APK ke perangkat Oppo Anda

### 2. Setup AccessibilityService
1. Buka aplikasi "Auto Force Stop"
2. Klik tombol "Aktifkan Accessibility Service"
3. Di Settings, cari "Auto Force Stop" dan aktifkan
4. Berikan izin yang diperlukan

### 3. Pilih Aplikasi Target
1. Kembali ke aplikasi "Auto Force Stop"
2. Centang aplikasi yang ingin di-force stop otomatis (contoh: Roblox)
3. Pilihan akan tersimpan otomatis

### 4. Penggunaan Normal
- Sekarang ketika Anda:
  1. Membuka aplikasi target (misal: Roblox)
  2. Keluar dan pindah ke aplikasi lain (misal: WhatsApp)
  3. Aplikasi akan otomatis membuka Settings dan force stop aplikasi target
  4. Anda akan dikembalikan ke aplikasi sebelumnya

## 🔧 Cara Kerja (The "Sniper" Logic)

### Detection Phase:
1. Service memantau `TYPE_WINDOW_STATE_CHANGED`
2. Mendeteksi perpindahan dari aplikasi target ke aplikasi lain
3. Jika aplikasi sebelumnya ada di "Kill List", trigger kill sequence

### Execution Phase (Kill Sequence):
1. **Step A**: Buka Settings untuk aplikasi target
2. **Step B**: Tunggu Settings screen muncul (800ms)
3. **Step C**: Cari tombol dengan teks: "Force stop", "Paksa berhenti", "Paksa henti"
4. **Step D**: Klik tombol tersebut
5. **Step E**: Tunggu dialog konfirmasi (500ms)
6. **Step F**: Cari dan klik tombol "OK" atau "Hentikan"
7. **Step G**: Tekan BACK untuk kembali ke aplikasi sebelumnya

## 🏗️ Struktur Project

```
killApps/
├── app/
│   ├── src/main/
│   │   ├── java/com/autoforcestop/
│   │   │   ├── MainActivity.kt          # UI dan app list
│   │   │   ├── KillService.kt           # Accessibility Service logic
│   │   │   ├── AppInfo.kt               # Data model
│   │   │   ├── PreferencesManager.kt    # SharedPreferences manager
│   │   │   └── AppListAdapter.kt        # RecyclerView adapter
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_main.xml
│   │   │   │   └── item_app.xml
│   │   │   ├── values/
│   │   │   │   ├── strings.xml
│   │   │   │   └── colors.xml
│   │   │   └── xml/
│   │   │       └── accessibility_service_config.xml
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## 📝 File-File Penting

### 1. AndroidManifest.xml
- Deklarasi AccessibilityService dengan permission `BIND_ACCESSIBILITY_SERVICE`
- Permission `QUERY_ALL_PACKAGES` untuk mendapatkan list aplikasi

### 2. accessibility_service_config.xml
- Event type: `typeWindowStateChanged`
- Capability: Retrieve window content

### 3. MainActivity.kt
- Menampilkan list aplikasi yang ter-install (exclude system apps)
- Checkbox untuk memilih aplikasi target
- Cek status AccessibilityService
- Simpan/load kill list dari SharedPreferences

### 4. KillService.kt
- Monitor window state changes
- Deteksi perpindahan aplikasi
- Execute kill sequence dengan node search
- Support multiple text variations (Indonesia & English)
- Error handling yang robust

## ⚙️ ColorOS Specific Notes

### Variasi Teks yang Didukung:
- **Force Stop**: "Force stop", "FORCE STOP", "Paksa berhenti", "PAKSA BERHENTI", "Paksa henti", "PAKSA HENTI"
- **OK Button**: "OK", "Ok", "Hentikan", "HENTIKAN", "Ya", "YA"

### Battery Optimization:
ColorOS mungkin membunuh AccessibilityService di background. Untuk mengatasi:
1. Buka Settings → Battery → Battery Optimization
2. Cari "Auto Force Stop"
3. Pilih "Don't optimize"

### Autostart:
1. Buka Settings → App Management → Auto Force Stop
2. Enable "Autostart"

## 🐛 Troubleshooting

### Service tidak berjalan:
- Pastikan AccessibilityService sudah enabled
- Nonaktifkan battery optimization
- Enable autostart permission

### Force Stop tidak bekerja:
- Cek Logcat untuk melihat apakah tombol ditemukan
- Bahasa sistem mungkin berbeda, tambahkan variasi teks di `KillService.kt`
- Beberapa aplikasi mungkin memblokir force stop

### Aplikasi crash:
- Pastikan minSdk adalah 24 atau sesuaikan
- Build ulang dengan Android Studio
- Cek LogCat untuk error details

## 📱 Testing

### Test Scenario:
1. Install aplikasi
2. Enable AccessibilityService
3. Centang Roblox (atau aplikasi lain)
4. Buka Roblox
5. Switch ke WhatsApp atau Home
6. Verify Settings terbuka otomatis
7. Verify Force Stop terclick
8. Verify kembali ke previous screen

## 🔐 Privacy & Security

- Aplikasi TIDAK mengirim data ke server
- Hanya membaca list aplikasi yang ter-install
- AccessibilityService hanya digunakan untuk force stop
- Pilihan aplikasi disimpan lokal di SharedPreferences

## 📄 License

Project ini dibuat untuk penggunaan personal. Feel free to modify sesuai kebutuhan.

## 👨‍💻 Developer Notes

- **Language**: Kotlin
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Architecture**: Simple MVC pattern
- **No external dependencies** (only AndroidX)

## ⚠️ Disclaimer

Penggunaan AccessibilityService untuk automation mungkin melanggar terms of service beberapa aplikasi. Gunakan dengan bijak dan untuk keperluan personal saja.
