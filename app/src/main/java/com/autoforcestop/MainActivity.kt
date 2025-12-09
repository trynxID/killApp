package com.autoforcestop

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Main Activity - Displays list of installed apps with checkboxes
 * Allows user to select which apps should be auto force-stopped
 */
class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvServiceStatus: TextView
    private lateinit var btnEnableService: Button
    private lateinit var adapter: AppListAdapter
    private val appList = mutableListOf<AppInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize PreferencesManager
        PreferencesManager.init(this)

        // Initialize views
        recyclerView = findViewById(R.id.recyclerViewApps)
        tvServiceStatus = findViewById(R.id.tvServiceStatus)
        btnEnableService = findViewById(R.id.btnEnableService)

        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        // Load installed apps
        loadInstalledApps()
        
        // Setup adapter with selection callback
        adapter = AppListAdapter(appList) { packageName, isSelected ->
            updateKillList(packageName, isSelected)
        }
        recyclerView.adapter = adapter

        // Setup button click
        btnEnableService.setOnClickListener {
            showEnableServiceDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        updateServiceStatus()
    }

    /**
     * Load all user-installed apps (exclude system apps)
     */
    private fun loadInstalledApps() {
        appList.clear()
        val packageManager = packageManager
        val packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        
        // Get existing kill list
        val killList = PreferencesManager.getKillList()
        
        // Filter non-system apps and sort alphabetically
        packages
            .filter { app ->
                // Exclude system apps and our own app
                (app.flags and ApplicationInfo.FLAG_SYSTEM) == 0 && 
                app.packageName != packageName
            }
            .sortedBy { app ->
                packageManager.getApplicationLabel(app).toString()
            }
            .forEach { app ->
                val appInfo = AppInfo(
                    packageName = app.packageName,
                    appName = packageManager.getApplicationLabel(app).toString(),
                    icon = packageManager.getApplicationIcon(app),
                    isSelected = killList.contains(app.packageName)
                )
                appList.add(appInfo)
            }
    }

    /**
     * Update the kill list when user checks/unchecks an app
     */
    private fun updateKillList(packageName: String, isSelected: Boolean) {
        val currentKillList = PreferencesManager.getKillList().toMutableSet()
        
        if (isSelected) {
            currentKillList.add(packageName)
        } else {
            currentKillList.remove(packageName)
        }
        
        PreferencesManager.saveKillList(currentKillList)
    }

    /**
     * Check if Accessibility Service is enabled
     */
    private fun isAccessibilityServiceEnabled(): Boolean {
        val expectedComponentName = "$packageName/${KillService::class.java.name}"
        val enabledServicesSetting = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false
        
        val colonSplitter = TextUtils.SimpleStringSplitter(':')
        colonSplitter.setString(enabledServicesSetting)
        
        while (colonSplitter.hasNext()) {
            val componentName = colonSplitter.next()
            if (componentName.equals(expectedComponentName, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    /**
     * Update service status display
     */
    private fun updateServiceStatus() {
        if (isAccessibilityServiceEnabled()) {
            tvServiceStatus.text = getString(R.string.service_enabled)
            tvServiceStatus.setTextColor(getColor(android.R.color.holo_green_dark))
            btnEnableService.isEnabled = false
            btnEnableService.alpha = 0.5f
        } else {
            tvServiceStatus.text = getString(R.string.service_disabled)
            tvServiceStatus.setTextColor(getColor(android.R.color.holo_red_dark))
            btnEnableService.isEnabled = true
            btnEnableService.alpha = 1.0f
        }
    }

    /**
     * Show dialog to guide user to enable Accessibility Service
     */
    private fun showEnableServiceDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_enable_service_title)
            .setMessage(R.string.dialog_enable_service_message)
            .setPositiveButton(R.string.open_settings) { _, _ ->
                openAccessibilitySettings()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    /**
     * Open Accessibility Settings
     */
    private fun openAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)
    }
}
