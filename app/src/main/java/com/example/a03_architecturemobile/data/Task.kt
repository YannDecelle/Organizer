package com.example.a03_architecturemobile.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Define the Task entity
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String? = null,
    val isCompleted: Boolean = false
)
