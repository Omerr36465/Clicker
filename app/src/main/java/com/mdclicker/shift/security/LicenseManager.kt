
package com.mdclicker.shift.security

object LicenseManager {

    fun verifyLicense(key: String): Boolean {
        return key.length > 10
    }
}
