package com.autoforcestop

import android.graphics.drawable.Drawable

/**
 * Data class representing an installed application
 */
data class AppInfo(
    val packageName: String,
    val appName: String,
    val icon: Drawable,
    var isSelected: Boolean = false
)
