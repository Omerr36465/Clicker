package com.mdclicker.shift.data

/**
 * LicenseGate - Simple local license verification system.
 * 
 * Instead of Firebase Authentication, this uses a local
 * verification mechanism. The LicenseGate User ID is used
 * as the root of trust for license validation.
 * 
 * LicenseGate User ID: a26b9
 */
object LicenseGate {

    // The master LicenseGate User ID for this application
    private const val LICENSE_GATE_USER_ID = "a26b9"
    
    // Hash salt for license key generation
    private const val SALT = "MDClickerShift2024"

    /**
     * Generate a license key based on device ID and LicenseGate User ID
     */
    fun generateLicenseKey(deviceId: String): String {
        val raw = "$LICENSE_GATE_USER_ID-$deviceId-$SALT"
        return raw.hashCode().toUInt().toString(16).padStart(8, '0').take(8).uppercase()
    }

    /**
     * Verify a license key against the device ID
     */
    fun verifyLicenseKey(licenseKey: String, deviceId: String): Boolean {
        val expected = generateLicenseKey(deviceId)
        return licenseKey.trim().uppercase() == expected
    }

    /**
     * Verify if a user-entered LicenseGate ID matches
     */
    fun verifyLicenseGateId(inputId: String): Boolean {
        return inputId.trim().lowercase() == LICENSE_GATE_USER_ID
    }

    /**
     * Get the current LicenseGate User ID
     */
    fun getLicenseGateUserId(): String = LICENSE_GATE_USER_ID

    /**
     * Generate a simple offline activation code
     */
    fun generateActivationCode(deviceId: String): String {
        val seed = (LICENSE_GATE_USER_ID + deviceId).hashCode()
        val part1 = (seed and 0xFFFF).toUInt().toString(16).padStart(4, '0')
        val part2 = ((seed shr 16) and 0xFFFF).toUInt().toString(16).padStart(4, '0')
        return "$part1-$part2".uppercase()
    }

    /**
     * Verify activation code
     */
    fun verifyActivationCode(code: String, deviceId: String): Boolean {
        val expected = generateActivationCode(deviceId)
        return code.trim().uppercase() == expected
    }
}
