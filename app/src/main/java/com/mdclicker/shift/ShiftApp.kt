package com.mdclicker.shift

import android.app.Application
import com.mdclicker.shift.data.AppDatabase
import com.mdclicker.shift.data.LocalAuthManager

class ShiftApp : Application() {

    lateinit var database: AppDatabase
        private set
    lateinit var authManager: LocalAuthManager
        private set

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getInstance(this)
        authManager = LocalAuthManager(this)
    }
}
