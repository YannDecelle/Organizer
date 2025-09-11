package com.example.a03_architecturemobile.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

// Define the Reminder entity
@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val taskId: Int,
    val title: String = "",
    val description: String = "",
    val remindAt: Long
)
