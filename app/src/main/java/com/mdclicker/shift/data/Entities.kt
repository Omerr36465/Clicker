package com.mdclicker.shift.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Shift settings for automation
 */
@Entity(tableName = "shift_settings")
data class ShiftSettings(
    @PrimaryKey
    val id: Int = 1,
    val shift1Start: String = "08:00",
    val shift1End: String = "16:00",
    val shift2Start: String = "16:00",
    val shift2End: String = "00:00",
    val shift3Start: String = "00:00",
    val shift3End: String = "08:00",
    val autoStartEnabled: Boolean = false,
    val bookingDelayMs: Long = 2000,
    val retryCount: Int = 3,
    val retryDelayMs: Long = 5000
)

/**
 * Record of a booked shift
 */
@Entity(tableName = "booked_shifts")
data class BookedShift(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val shiftDate: String,          // yyyy-MM-dd
    val shiftLabel: String,         // e.g., "Shift 1", "Shift 2"
    val startTime: String,
    val endTime: String,
    val bookedAt: Long = System.currentTimeMillis(),
    val status: String = "booked"   // booked, failed, pending
)

/**
 * License record (stored locally instead of Firebase)
 */
@Entity(tableName = "license_records")
data class LicenseRecord(
    @PrimaryKey
    val licenseKey: String,
    val deviceId: String = "",
    val sellerCode: String = "",
    val isActive: Boolean = false,
    val expiresAt: Long = 0L,
    val createdAt: Long = System.currentTimeMillis(),
    val lastVerifiedAt: Long = 0L
)

/**
 * Panel session for admin panel access
 */
@Entity(tableName = "panel_sessions")
data class PanelSession(
    @PrimaryKey
    val sessionId: String,
    val subscriberId: String = "",
    val adminPin: String = "",
    val isActive: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val lastUsedAt: Long = 0L
)

/**
 * Automation log entries
 */
@Entity(tableName = "automation_logs")
data class AutomationLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val level: String = "INFO",     // INFO, WARN, ERROR
    val message: String,
    val details: String = ""
)
