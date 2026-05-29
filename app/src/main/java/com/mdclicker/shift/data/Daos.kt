package com.mdclicker.shift.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ShiftSettingsDao {
    @Query("SELECT * FROM shift_settings WHERE id = 1")
    fun getSettings(): Flow<ShiftSettings?>

    @Query("SELECT * FROM shift_settings WHERE id = 1")
    suspend fun getSettingsSync(): ShiftSettings?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSettings(settings: ShiftSettings)
}

@Dao
interface BookedShiftDao {
    @Query("SELECT * FROM booked_shifts ORDER BY bookedAt DESC")
    fun getAllShifts(): Flow<List<BookedShift>>

    @Query("SELECT * FROM booked_shifts WHERE shiftDate = :date ORDER BY bookedAt DESC")
    fun getShiftsByDate(date: String): Flow<List<BookedShift>>

    @Query("SELECT * FROM booked_shifts WHERE shiftDate = :date ORDER BY bookedAt DESC")
    suspend fun getShiftsByDateSync(date: String): List<BookedShift>

    @Insert
    suspend fun insertShift(shift: BookedShift): Long

    @Update
    suspend fun updateShift(shift: BookedShift)

    @Delete
    suspend fun deleteShift(shift: BookedShift)

    @Query("DELETE FROM booked_shifts")
    suspend fun clearAll()
}

@Dao
interface LicenseDao {
    @Query("SELECT * FROM license_records ORDER BY createdAt DESC")
    fun getAllLicenses(): Flow<List<LicenseRecord>>

    @Query("SELECT * FROM license_records WHERE licenseKey = :key")
    suspend fun getLicenseByKey(key: String): LicenseRecord?

    @Query("SELECT * FROM license_records WHERE isActive = 1 LIMIT 1")
    suspend fun getActiveLicense(): LicenseRecord?

    @Query("SELECT * FROM license_records WHERE isActive = 1 LIMIT 1")
    fun getActiveLicenseFlow(): Flow<LicenseRecord?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLicense(license: LicenseRecord)

    @Query("UPDATE license_records SET isActive = 0")
    suspend fun deactivateAllLicenses()

    @Delete
    suspend fun deleteLicense(license: LicenseRecord)
}

@Dao
interface PanelSessionDao {
    @Query("SELECT * FROM panel_sessions ORDER BY createdAt DESC")
    fun getAllSessions(): Flow<List<PanelSession>>

    @Query("SELECT * FROM panel_sessions WHERE isActive = 1 LIMIT 1")
    suspend fun getActiveSession(): PanelSession?

    @Query("SELECT * FROM panel_sessions WHERE sessionId = :id")
    suspend fun getSessionById(id: String): PanelSession?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSession(session: PanelSession)

    @Query("UPDATE panel_sessions SET isActive = 0")
    suspend fun deactivateAll()
}

@Dao
interface AutomationLogDao {
    @Query("SELECT * FROM automation_logs ORDER BY timestamp DESC LIMIT 100")
    fun getRecentLogs(): Flow<List<AutomationLog>>

    @Insert
    suspend fun insertLog(log: AutomationLog)

    @Query("DELETE FROM automation_logs WHERE timestamp < :before")
    suspend fun deleteOldLogs(before: Long)
}
