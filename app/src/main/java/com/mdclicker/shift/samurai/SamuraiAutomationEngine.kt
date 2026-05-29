package com.mdclicker.shift.samurai

import android.util.Log
import com.mdclicker.shift.data.ShiftRepository
import com.mdclicker.shift.service.ShiftAccessibilityService

/**
 * Core automation engine for shift booking.
 * Operates fully offline - detects screens, parses shift cards,
 * and performs booking actions via the accessibility service.
 */
class SamuraiAutomationEngine(
    private val repository: ShiftRepository,
    private var accessibilityService: ShiftAccessibilityService? = null
) {
    companion object {
        private const val TAG = "SamuraiEngine"
    }

    private var isRunning = false
    private var currentScreenType = ScreenType.UNKNOWN
    
    enum class ScreenType {
        UNKNOWN,
        LOGIN_SCREEN,
        MAIN_MENU,
        SHIFT_LIST,
        SHIFT_DETAIL,
        CONFIRMATION_DIALOG,
        BOOKING_COMPLETE
    }

    /**
     * Start the automation engine
     */
    suspend fun start() {
        isRunning = true
        repository.addLog("INFO", "Samurai Engine started", "Automation engine initialized")
        Log.d(TAG, "Engine started")
    }

    /**
     * Stop the automation engine
     */
    suspend fun stop() {
        isRunning = false
        repository.addLog("INFO", "Samurai Engine stopped", "Automation engine shutdown")
        Log.d(TAG, "Engine stopped")
    }

    /**
     * Process a screen change detected by the accessibility service
     */
    suspend fun processScreenChange(packageName: String?, rootViewDump: String) {
        if (!isRunning) return

        val previousScreen = currentScreenType
        currentScreenType = detectScreen(packageName, rootViewDump)

        if (currentScreenType != previousScreen) {
            Log.d(TAG, "Screen changed: $previousScreen -> $currentScreenType")
            repository.addLog("INFO", "Screen: $currentScreenType")
        }

        when (currentScreenType) {
            ScreenType.SHIFT_LIST -> handleShiftList(rootViewDump)
            ScreenType.CONFIRMATION_DIALOG -> handleConfirmation()
            ScreenType.BOOKING_COMPLETE -> handleBookingComplete()
            else -> { /* No action needed */ }
        }
    }

    private fun detectScreen(packageName: String?, rootViewDump: String): ScreenType {
        val lowerDump = rootViewDump.lowercase()
        val lowerPkg = packageName?.lowercase() ?: ""

        return when {
            "confirm" in lowerDump && ("book" in lowerDump || "حجز" in lowerDump) ->
                ScreenType.CONFIRMATION_DIALOG
            lowerDump.contains("shift") && (lowerDump.contains("book") || lowerDump.contains("حجز")) ->
                ScreenType.SHIFT_LIST
            "تم الحجز" in lowerDump || "booking complete" in lowerDump || "booked" in lowerDump ->
                ScreenType.BOOKING_COMPLETE
            lowerPkg.contains("login") || "تسجيل الدخول" in lowerDump ->
                ScreenType.LOGIN_SCREEN
            else -> ScreenType.UNKNOWN
        }
    }

    private suspend fun handleShiftList(rootViewDump: String) {
        repository.addLog("INFO", "Shift list detected, scanning available shifts...")
        // The accessibility service will handle the actual UI interaction
    }

    private suspend fun handleConfirmation() {
        repository.addLog("INFO", "Confirmation dialog detected, confirming booking...")
        // Click confirm - handled by accessibility service
    }

    private suspend fun handleBookingComplete() {
        repository.addLog("SUCCESS", "Shift booked successfully!")
        // Record the booking locally
    }

    fun setAccessibilityService(service: ShiftAccessibilityService?) {
        accessibilityService = service
    }

    fun isEngineRunning(): Boolean = isRunning
}
