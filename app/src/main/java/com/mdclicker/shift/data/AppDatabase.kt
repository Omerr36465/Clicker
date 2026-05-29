package com.mdclicker.shift.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ShiftSettings::class,
        BookedShift::class,
        LicenseRecord::class,
        PanelSession::class,
        AutomationLog::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shiftSettingsDao(): ShiftSettingsDao
    abstract fun bookedShiftDao(): BookedShiftDao
    abstract fun licenseDao(): LicenseDao
    abstract fun panelSessionDao(): PanelSessionDao
    abstract fun automationLogDao(): AutomationLogDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mdclicker_shift_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
