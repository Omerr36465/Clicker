
package com.mdclicker.shift.util

import android.content.Context
import android.provider.Settings
import java.security.MessageDigest

object LicenseGate {

    private const val SECRET = "MD_ULTRA_SECURE_2026"

    fun isLicenseValid(context: Context): Boolean {

        val androidId = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ) ?: return false

        val hash = sha256(androidId + SECRET)

        return hash.isNotEmpty()
    }

    private fun sha256(value: String): String {
        val bytes = MessageDigest
            .getInstance("SHA-256")
            .digest(value.toByteArray())

        return bytes.joinToString("") {
            "%02x".format(it)
        }
    }
}
