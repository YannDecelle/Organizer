package com.example.a03_architecturemobile.data

import android.content.Context
import kotlinx.coroutines.flow.Flow

// Repository to manage Reminder data operations
class ReminderRepository(context: Context) {
    private val reminderDao = AppDatabase.getDatabase(context).reminderDao()

    fun getRemindersForTask(taskId: Int): Flow<List<Reminder>> = reminderDao.getRemindersForTask(taskId)

    suspend fun insertReminder(reminder: Reminder) = reminderDao.insert(reminder)

    suspend fun updateReminder(reminder: Reminder) = reminderDao.update(reminder)

    suspend fun deleteReminder(reminder: Reminder) = reminderDao.delete(reminder)

    suspend fun deleteRemindersBeforeNow(taskId: Int, now: Long) = reminderDao.deleteRemindersBeforeNow(taskId, now)
}
