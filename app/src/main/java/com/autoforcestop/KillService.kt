package com.autoforcestop

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

/**
 * Accessibility Service that monitors app switching and automatically
 * force stops apps that are in the kill list
 */
class KillService : AccessibilityService() {

    companion object {
        private const val TAG = "KillService"
        private const val DELAY_OPEN_SETTINGS = 800L
        private const val DELAY_FIND_BUTTON = 600L
        private const val DELAY_CLICK_OK = 500L
        private const val DELAY_GO_BACK = 300L
    }

    private var previousPackageName: String? = null
    private var targetPackageToKill: String? = null
    private var isKillSequenceRunning = false
    
    private val handler = Handler(Looper.getMainLooper())

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "KillService connected")
        PreferencesManager.init(this)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event ?: return
        
        // Only process window state changes
        if (event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            return
        }

        val currentPackageName = event.packageName?.toString() ?: return
        
        // Ignore empty package names
        if (currentPackageName.isEmpty()) {
            return
        }

        Log.d(TAG, "Window changed: $previousPackageName -> $currentPackageName")

        // If we're currently running a kill sequence, check if we're in Settings
        if (isKillSequenceRunning && currentPackageName.contains("settings", ignoreCase = true)) {
            Log.d(TAG, "Settings opened, proceeding with kill sequence")
            // Wait a bit for the settings screen to fully load
            handler.postDelayed({
                clickForceStopButton()
            }, DELAY_FIND_BUTTON)
            return
        }

        // Don't trigger new kills while a sequence is running
        if (isKillSequenceRunning) {
            return
        }

        // Check if we should kill the previous app
        if (previousPackageName != null && 
            previousPackageName != currentPackageName &&
            !currentPackageName.contains("settings", ignoreCase = true) &&
            PreferencesManager.isInKillList(previousPackageName!!)) {
            
            Log.d(TAG, "Triggering kill sequence for: $previousPackageName")
            executeKillSequence(previousPackageName!!)
        }

        // Update previous package
        previousPackageName = currentPackageName
    }

    /**
     * Execute the kill sequence for a target app
     */
    private fun executeKillSequence(packageName: String) {
        isKillSequenceRunning = true
        targetPackageToKill = packageName
        
        Log.d(TAG, "Starting kill sequence for: $packageName")
        
        // Step A: Open app settings
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:$packageName")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
            Log.d(TAG, "Opened settings for: $packageName")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to open settings", e)
            resetKillSequence()
        }
    }

    /**
     * Find and click the Force Stop button
     */
    private fun clickForceStopButton() {
        try {
            val rootNode = rootInActiveWindow
            if (rootNode == null) {
                Log.e(TAG, "Root node is null")
                resetKillSequence()
                return
            }

            // Search for Force Stop button with various text variations
            val forceStopTexts = listOf(
                "Force stop",
                "FORCE STOP",
                "Paksa berhenti",
                "PAKSA BERHENTI",
                "Paksa henti",
                "PAKSA HENTI",
                "Hentikan paksa",
                "HENTIKAN PAKSA"
            )

            val forceStopButton = findNodeByTexts(rootNode, forceStopTexts)
            
            if (forceStopButton != null && forceStopButton.isClickable) {
                Log.d(TAG, "Found Force Stop button, clicking...")
                forceStopButton.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                
                // Wait for confirmation dialog and click OK
                handler.postDelayed({
                    clickOkButton()
                }, DELAY_CLICK_OK)
            } else {
                Log.e(TAG, "Force Stop button not found or not clickable")
                resetKillSequence()
            }
            
            rootNode.recycle()
        } catch (e: Exception) {
            Log.e(TAG, "Error clicking Force Stop button", e)
            resetKillSequence()
        }
    }

    /**
     * Find and click the OK button in confirmation dialog
     */
    private fun clickOkButton() {
        try {
            val rootNode = rootInActiveWindow
            if (rootNode == null) {
                Log.e(TAG, "Root node is null when looking for OK button")
                performBackAction()
                return
            }

            // Search for OK button with various text variations
            val okTexts = listOf(
                "OK",
                "Ok",
                "Force stop",
                "FORCE STOP",
                "Hentikan",
                "HENTIKAN",
                "Ya",
                "YA"
            )

            val okButton = findNodeByTexts(rootNode, okTexts)
            
            if (okButton != null && okButton.isClickable) {
                Log.d(TAG, "Found OK button, clicking...")
                okButton.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                
                // Go back after clicking OK
                handler.postDelayed({
                    performBackAction()
                }, DELAY_GO_BACK)
            } else {
                Log.w(TAG, "OK button not found, performing back action")
                performBackAction()
            }
            
            rootNode.recycle()
        } catch (e: Exception) {
            Log.e(TAG, "Error clicking OK button", e)
            performBackAction()
        }
    }

    /**
     * Find a node by searching for any of the given text variations
     */
    private fun findNodeByTexts(
        rootNode: AccessibilityNodeInfo,
        texts: List<String>
    ): AccessibilityNodeInfo? {
        for (text in texts) {
            val nodes = rootNode.findAccessibilityNodeInfosByText(text)
            if (nodes.isNotEmpty()) {
                // Find the first clickable node or just return the first one
                val clickableNode = nodes.firstOrNull { it.isClickable }
                if (clickableNode != null) {
                    Log.d(TAG, "Found clickable node with text: $text")
                    return clickableNode
                }
                
                // If no clickable node, try to find clickable parent
                for (node in nodes) {
                    var parent = node.parent
                    var depth = 0
                    while (parent != null && depth < 5) {
                        if (parent.isClickable) {
                            Log.d(TAG, "Found clickable parent for text: $text")
                            return parent
                        }
                        parent = parent.parent
                        depth++
                    }
                }
                
                // Last resort: return first node
                if (nodes.isNotEmpty()) {
                    Log.d(TAG, "Returning first node with text: $text")
                    return nodes[0]
                }
            }
        }
        return null
    }

    /**
     * Perform back action to return user to previous screen
     */
    private fun performBackAction() {
        try {
            Log.d(TAG, "Performing back action")
            performGlobalAction(GLOBAL_ACTION_BACK)
        } catch (e: Exception) {
            Log.e(TAG, "Error performing back action", e)
        } finally {
            // Reset sequence after a short delay
            handler.postDelayed({
                resetKillSequence()
            }, 500)
        }
    }

    /**
     * Reset the kill sequence state
     */
    private fun resetKillSequence() {
        Log.d(TAG, "Resetting kill sequence")
        isKillSequenceRunning = false
        targetPackageToKill = null
    }

    override fun onInterrupt() {
        Log.d(TAG, "KillService interrupted")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "KillService destroyed")
        handler.removeCallbacksAndMessages(null)
    }
}
