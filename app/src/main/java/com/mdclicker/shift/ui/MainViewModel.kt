package com.mdclicker.shift.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mdclicker.shift.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val authManager = LocalAuthManager(application)
    val repository = ShiftRepository(db, authManager)

    // UI State
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // Settings state
    private val _settings = MutableStateFlow(ShiftSettings())
    val settings: StateFlow<ShiftSettings> = _settings.asStateFlow()

    // Logs
    val logs: Flow<List<AutomationLog>> = repository.getRecentLogs()

    // Booked shifts for today
    val todayShifts: Flow<List<BookedShift>> = repository.getShiftsByDate(
        LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
    )

    init {
        // Load settings
        viewModelScope.launch {
            repository.getSettingsFlow().collect { s ->
                if (s != null) _settings.value = s
            }
        }
        // Check login state
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoggedIn = repository.isLoggedIn(),
                    username = repository.getUsername(),
                    email = repository.getEmail(),
                    deviceId = repository.getDeviceId(),
                    sellerCode = repository.getSellerCode(),
                    automationRunning = repository.isAutomationRunning(),
                    licenseGateId = LicenseGate.getLicenseGateUserId()
                )
            }
        }
    }

    // === Authentication ===

    fun login(username: String, password: String, callback: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            if (!repository.isPasswordSet()) {
                // First time - register
                repository.setUsername(username)
                repository.setPassword(password)
                repository.setEmail("$username@local")
                repository.setLoggedIn(true)
                repository.setFirstLaunchComplete()
                _uiState.update {
                    it.copy(isLoggedIn = true, username = username, email = "$username@local")
                }
                addLog("INFO", "تم تسجيل المستخدم: $username")
                callback(true, "تم التسجيل بنجاح")
            } else {
                // Verify password
                if (repository.verifyPassword(password)) {
                    repository.setLoggedIn(true)
                    _uiState.update { it.copy(isLoggedIn = true) }
                    addLog("INFO", "تم تسجيل الدخول: $username")
                    callback(true, "تم تسجيل الدخول بنجاح")
                } else {
                    callback(false, "كلمة المرور غير صحيحة")
                }
            }
        }
    }

    fun logout() {
        repository.setLoggedIn(false)
        _uiState.update { it.copy(isLoggedIn = false) }
        addLog("INFO", "تم تسجيل الخروج")
    }

    // === License Verification ===

    fun activateWithLicenseGate(licenseKey: String, callback: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val deviceId = repository.getDeviceId()
            val isValid = LicenseGate.verifyLicenseKey(licenseKey, deviceId)
            
            if (isValid) {
                repository.saveLicense(
                    LicenseRecord(
                        licenseKey = licenseKey,
                        deviceId = deviceId,
                        isActive = true,
                        expiresAt = Long.MAX_VALUE, // No expiry for offline mode
                        lastVerifiedAt = System.currentTimeMillis()
                    )
                )
                _uiState.update { it.copy(isLicenseActive = true) }
                addLog("INFO", "تم تفعيل الترخيص بنجاح")
                callback(true, "تم تفعيل الترخيص بنجاح")
            } else {
                // Check if it's a direct LicenseGate ID match
                if (LicenseGate.verifyLicenseGateId(licenseKey)) {
                    repository.saveLicense(
                        LicenseRecord(
                            licenseKey = "LICENSEGATE-$licenseKey",
                            deviceId = deviceId,
                            isActive = true,
                            expiresAt = Long.MAX_VALUE,
                            lastVerifiedAt = System.currentTimeMillis()
                        )
                    )
                    _uiState.update { it.copy(isLicenseActive = true) }
                    addLog("INFO", "تم التفعيل عبر LicenseGate ID")
                    callback(true, "تم التفعيل عبر LicenseGate ID")
                } else {
                    callback(false, "مفتاح الترخيص غير صالح. الرجاء استخدام LicenseGate ID: ${LicenseGate.getLicenseGateUserId()}")
                }
            }
        }
    }

    // === Settings ===

    fun saveSettings(settings: ShiftSettings) {
        viewModelScope.launch {
            repository.saveSettings(settings)
            addLog("INFO", "تم حفظ الإعدادات")
        }
    }

    fun setSellerCode(code: String) {
        repository.setSellerCode(code)
        _uiState.update { it.copy(sellerCode = code) }
    }

    // === Automation ===

    fun startAutomation() {
        repository.setAutomationRunning(true)
        _uiState.update { it.copy(automationRunning = true) }
        addLog("INFO", "بدء التشغيل الآلي")
    }

    fun stopAutomation() {
        repository.setAutomationRunning(false)
        _uiState.update { it.copy(automationRunning = false) }
        addLog("INFO", "إيقاف التشغيل الآلي")
    }

    // === Utility ===

    private fun addLog(level: String, message: String, details: String = "") {
        viewModelScope.launch {
            repository.addLog(level, message, details)
        }
    }

    fun addCustomLog(level: String, message: String) {
        addLog(level, message)
    }
}

data class UiState(
    val isLoggedIn: Boolean = false,
    val username: String = "",
    val email: String = "",
    val deviceId: String = "",
    val sellerCode: String = "",
    val isLicenseActive: Boolean = false,
    val automationRunning: Boolean = false,
    val licenseGateId: String = "a26b9"
)
