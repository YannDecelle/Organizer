package com.example.a03_architecturemobile.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Date
import com.example.a03_architecturemobile.data.NotificationTime
import com.example.a03_architecturemobile.data.NotificationTimeListConverter

// Define the Reminder entity
@Entity(tableName = "reminders")
@TypeConverters(NotificationTimeListConverter::class)
data class Reminder(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val taskId: Int,
    val title: String = "",
    val description: String = "",
    val remindAt: Long,
    val notifications: List<NotificationTime> = emptyList()
)

enum class NotificationTime {
    MINUTE,
    HOUR,
    DAY,
    WEEK
}

class NotificationTimeListConverter {
    @androidx.room.TypeConverter
    fun fromList(list: List<NotificationTime>): String = list.joinToString(",") { it.name }

    @androidx.room.TypeConverter
    fun toList(data: String): List<NotificationTime> =
        if (data.isEmpty()) emptyList() else data.split(",").map { NotificationTime.valueOf(it) }
}
