package com.mdclicker.shift.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

/**
 * Manages local authentication and secure data storage
 * Replaces Firebase Authentication entirely
 */
class LocalAuthManager(context: Context) {

    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val securePrefs: SharedPreferences = EncryptedSharedPreferences.create(
        "mdclicker_shift_secure",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val prefs: SharedPreferences = context.getSharedPreferences(
        "mdclicker_shift_prefs",
        Context.MODE_PRIVATE
    )

    // Authentication
    fun isLoggedIn(): Boolean = prefs.getBoolean("is_logged_in", false)

    fun setLoggedIn(loggedIn: Boolean) {
        prefs.edit().putBoolean("is_logged_in", loggedIn).apply()
    }

    fun getUsername(): String = securePrefs.getString("username", "") ?: ""

    fun setUsername(username: String) {
        securePrefs.edit().putString("username", username).apply()
    }

    fun getEmail(): String = securePrefs.getString("email", "") ?: ""

    fun setEmail(email: String) {
        securePrefs.edit().putString("email", email).apply()
    }

    fun isPasswordSet(): Boolean {
        return !securePrefs.getString("password_hash", "").isNullOrEmpty()
    }

    fun setPassword(password: String) {
        securePrefs.edit().putString("password_hash", password.hashCode().toString()).apply()
    }

    fun verifyPassword(password: String): Boolean {
        val storedHash = securePrefs.getString("password_hash", "")
        return storedHash == password.hashCode().toString()
    }

    // Device ID
    fun getDeviceId(): String {
        var deviceId = prefs.getString("device_id", "")
        if (deviceId.isNullOrEmpty()) {
            deviceId = java.util.UUID.randomUUID().toString()
            prefs.edit().putString("device_id", deviceId).apply()
        }
        return deviceId
    }

    // Seller Code
    fun getSellerCode(): String = prefs.getString("seller_code", "") ?: ""

    fun setSellerCode(code: String) {
        prefs.edit().putString("seller_code", code).apply()
    }

    // License key
    fun getLicenseKey(): String = securePrefs.getString("license_key", "") ?: ""

    fun setLicenseKey(key: String) {
        securePrefs.edit().putString("license_key", key).apply()
    }

    // App settings
    fun isFirstLaunch(): Boolean = prefs.getBoolean("first_launch", true)

    fun setFirstLaunchComplete() {
        prefs.edit().putBoolean("first_launch", false).apply()
    }

    // Automation settings
    fun isAutomationRunning(): Boolean = prefs.getBoolean("automation_running", false)

    fun setAutomationRunning(running: Boolean) {
        prefs.edit().putBoolean("automation_running", running).apply()
    }

    // Show password duration
    fun getShowPasswordDuration(): Long =
        prefs.getLong("show_password_duration", 5000L)

    fun setShowPasswordDuration(duration: Long) {
        prefs.edit().putLong("show_password_duration", duration).apply()
    }

    // Clear all data
    fun clearAll() {
        securePrefs.edit().clear().apply()
        prefs.edit().clear().apply()
    }
}
