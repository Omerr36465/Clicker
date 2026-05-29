
package com.mdclicker.shift.service

import android.accessibilityservice.AccessibilityService
import android.os.SystemClock
import android.util.Log
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import java.util.Locale

class ShiftAccessibilityService : AccessibilityService() {

    companion object {
        private const val TAG = "ULTRA_ENGINE"

        private const val REFRESH_MS = 15L
        private const val CLICK_MS = 25L

        private val KEYWORDS = listOf(
            "book",
            "booking",
            "confirm",
            "shift",
            "احجز",
            "حجز",
            "نوبة"
        )
    }

    private var lastRefresh = 0L
    private var lastClick = 0L
    private var overlayState = false

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "AI Ultra Engine Started")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        if (event == null) return

        val packageName =
            event.packageName?.toString()?.lowercase(Locale.getDefault())
                ?: return

        if (!packageName.contains("ninja")) return

        val now = SystemClock.elapsedRealtime()

        if (now - lastRefresh < REFRESH_MS) return

        lastRefresh = now

        when (event.eventType) {

            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED,
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
            AccessibilityEvent.TYPE_VIEW_SCROLLED -> {

                rootInActiveWindow?.let {
                    smartScan(it)
                }
            }
        }
    }

    private fun smartScan(root: AccessibilityNodeInfo) {

        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(root)

        while (queue.isNotEmpty()) {

            val node = queue.removeFirst()

            if (isTargetShift(node)) {
                ultraClick(node)
                return
            }

            for (i in 0 until node.childCount) {
                node.getChild(i)?.let(queue::add)
            }
        }
    }

    private fun isTargetShift(node: AccessibilityNodeInfo): Boolean {

        if (!node.isVisibleToUser) return false
        if (!node.isEnabled) return false

        val text = (
            node.text?.toString()
                ?: node.contentDescription?.toString()
                ?: ""
        ).lowercase(Locale.getDefault())

        val keyword = KEYWORDS.any {
            text.contains(it)
        }

        if (!keyword) return false

        val numbers = Regex("(\d+)")
            .findAll(text)
            .mapNotNull {
                it.groupValues[1].toIntOrNull()
            }
            .toList()

        val validTime = numbers.any {
            it >= 10 || it < 10
        }

        return validTime
    }

    private fun ultraClick(node: AccessibilityNodeInfo) {

        val now = SystemClock.elapsedRealtime()

        if (now - lastClick < CLICK_MS) return

        lastClick = now

        if (node.isClickable) {

            node.performAction(
                AccessibilityNodeInfo.ACTION_CLICK
            )

            Log.d(TAG, "Ultra booking click")
            return
        }

        var parent = node.parent

        while (parent != null) {

            if (parent.isClickable) {

                parent.performAction(
                    AccessibilityNodeInfo.ACTION_CLICK
                )

                Log.d(TAG, "Parent ultra click")
                return
            }

            parent = parent.parent
        }
    }

    override fun onKeyEvent(event: KeyEvent): Boolean {

        if (
            event.action == KeyEvent.ACTION_DOWN &&
            event.keyCode == KeyEvent.KEYCODE_VOLUME_UP
        ) {

            overlayState = !overlayState

            Log.d(TAG, "Overlay: $overlayState")

            return true
        }

        return super.onKeyEvent(event)
    }

    override fun onInterrupt() {
        Log.d(TAG, "Interrupted")
    }
}
