package com.example.a03_architecturemobile.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Define the database with both Task and Reminder entities
@Database(entities = [Task::class, Reminder::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun reminderDao(): ReminderDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                .fallbackToDestructiveMigration() // Handle migrations by recreating the database
                .build() // Build the database 
                INSTANCE = instance
                instance
            }
        }
    }
}
