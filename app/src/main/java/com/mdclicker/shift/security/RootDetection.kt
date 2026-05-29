
package com.mdclicker.shift.security

import java.io.File

object RootDetection {

    fun isRooted(): Boolean {
        val paths = arrayOf(
            "/system/bin/su",
            "/system/xbin/su",
            "/sbin/su"
        )

        return paths.any { File(it).exists() }
    }
}
