package com.autoforcestop

import android.content.Context
import android.content.SharedPreferences

/**
 * Singleton object to manage SharedPreferences for the kill list
 */
object PreferencesManager {
    private const val PREF_NAME = "KillAppPrefs"
    private const val KEY_KILL_LIST = "kill_list"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Save the kill list to SharedPreferences
     */
    fun saveKillList(packageNames: Set<String>) {
        prefs.edit().putStringSet(KEY_KILL_LIST, packageNames).apply()
    }

    /**
     * Get the kill list from SharedPreferences
     */
    fun getKillList(): Set<String> {
        return prefs.getStringSet(KEY_KILL_LIST, emptySet()) ?: emptySet()
    }

    /**
     * Check if a package is in the kill list
     */
    fun isInKillList(packageName: String): Boolean {
        return getKillList().contains(packageName)
    }
}
