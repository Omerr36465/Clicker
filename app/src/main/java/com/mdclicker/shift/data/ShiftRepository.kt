package com.mdclicker.shift.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository that centralizes data access.
 * All data is stored locally (Room + SharedPreferences) - no Firebase needed.
 */
class ShiftRepository(
    private val db: AppDatabase,
    private val authManager: LocalAuthManager
) {
    private val settingsDao = db.shiftSettingsDao()
    private val shiftDao = db.bookedShiftDao()
    private val licenseDao = db.licenseDao()
    private val sessionDao = db.panelSessionDao()
    private val logDao = db.automationLogDao()

    // Settings
    fun getSettingsFlow(): Flow<ShiftSettings?> = settingsDao.getSettings()
    suspend fun getSettings(): ShiftSettings? = settingsDao.getSettingsSync()
    suspend fun saveSettings(settings: ShiftSettings) = settingsDao.saveSettings(settings)

    // Booked Shifts
    fun getAllShifts(): Flow<List<BookedShift>> = shiftDao.getAllShifts()
    fun getShiftsByDate(date: String): Flow<List<BookedShift>> = shiftDao.getShiftsByDate(date)
    suspend fun getShiftsByDateSync(date: String): List<BookedShift> = shiftDao.getShiftsByDateSync(date)
    suspend fun bookShift(shift: BookedShift) = shiftDao.insertShift(shift)
    suspend fun updateShift(shift: BookedShift) = shiftDao.updateShift(shift)
    suspend fun clearAllShifts() = shiftDao.clearAll()

    // License
    fun getAllLicenses(): Flow<List<LicenseRecord>> = licenseDao.getAllLicenses()
    suspend fun getActiveLicense(): LicenseRecord? = licenseDao.getActiveLicense()
    fun getActiveLicenseFlow(): Flow<LicenseRecord?> = licenseDao.getActiveLicenseFlow()
    suspend fun saveLicense(license: LicenseRecord) {
        if (license.isActive) {
            licenseDao.deactivateAllLicenses()
        }
        licenseDao.saveLicense(license)
    }

    // Panel Sessions
    fun getAllSessions(): Flow<List<PanelSession>> = sessionDao.getAllSessions()
    suspend fun getActiveSession(): PanelSession? = sessionDao.getActiveSession()
    suspend fun saveSession(session: PanelSession) {
        if (session.isActive) {
            sessionDao.deactivateAll()
        }
        sessionDao.saveSession(session)
    }

    // Logs
    fun getRecentLogs(): Flow<List<AutomationLog>> = logDao.getRecentLogs()
    suspend fun addLog(level: String, message: String, details: String = "") {
        logDao.insertLog(AutomationLog(level = level, message = message, details = details))
    }

    // Auth
    fun isLoggedIn() = authManager.isLoggedIn()
    fun getUsername() = authManager.getUsername()
    fun getDeviceId() = authManager.getDeviceId()
    fun getSellerCode() = authManager.getSellerCode()
    fun getEmail() = authManager.getEmail()
    fun verifyPassword(password: String) = authManager.verifyPassword(password)
    fun setPassword(password: String) = authManager.setPassword(password)
    fun setLoggedIn(loggedIn: Boolean) = authManager.setLoggedIn(loggedIn)
    fun setUsername(username: String) = authManager.setUsername(username)
    fun setEmail(email: String) = authManager.setEmail(email)
    fun setSellerCode(code: String) = authManager.setSellerCode(code)
    fun isPasswordSet() = authManager.isPasswordSet()
    fun clearAll() = authManager.clearAll()
    fun isFirstLaunch() = authManager.isFirstLaunch()
    fun setFirstLaunchComplete() = authManager.setFirstLaunchComplete()
    fun isAutomationRunning() = authManager.isAutomationRunning()
    fun setAutomationRunning(running: Boolean) = authManager.setAutomationRunning(running)
}
