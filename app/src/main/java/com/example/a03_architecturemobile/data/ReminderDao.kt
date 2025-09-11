package com.example.a03_architecturemobile.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// Define the Data Access Object for Reminder entity
@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminders WHERE taskId = :taskId")
    fun getRemindersForTask(taskId: Int): Flow<List<Reminder>>

    @Insert
    suspend fun insert(reminder: Reminder)

    @Update
    suspend fun update(reminder: Reminder)

    @Delete
    suspend fun delete(reminder: Reminder)

    @Query("DELETE FROM reminders WHERE taskId = :taskId AND remindAt <= :now")
    suspend fun deleteRemindersBeforeNow(taskId: Int, now: Long)
}
