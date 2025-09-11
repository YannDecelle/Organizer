package com.example.a03_architecturemobile.ui.reminder

import android.app.Application
import androidx.lifecycle.*
import com.example.a03_architecturemobile.data.ReminderRepository
import com.example.a03_architecturemobile.data.Reminder as DataReminder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ReminderViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ReminderRepository(application)

    private val _reminders = MutableLiveData<List<Reminder>>(emptyList())
    val reminders: LiveData<List<Reminder>> = _reminders

    // For demo, using taskId = 1. In a real app, this should be dynamic.
    private val taskId = 1

    init {
        viewModelScope.launch {
            repository.getRemindersForTask(taskId).collect { dataReminders ->
                _reminders.postValue(dataReminders.map { it.toUiReminder() })
            }
        }
    }

    fun addReminder(reminder: Reminder) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertReminder(reminder.toDataReminder(taskId))
        }
    }

    fun editReminder(pos: Int, title: String, description: String, dateTime: Long?) {
        val currentList = _reminders.value ?: return
        if (pos !in currentList.indices) return
        val oldReminder = currentList[pos]
        val updatedReminder = oldReminder.copy(title = title, description = description, dateTime = dateTime)
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateReminder(updatedReminder.toDataReminderWithId(oldReminder.id, taskId))
        }
    }

    fun deleteReminder(pos: Int) {
        val currentList = _reminders.value ?: return
        if (pos !in currentList.indices) return
        val reminder = currentList[pos]
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteReminder(reminder.toDataReminderWithId(reminder.id, taskId))
        }
    }

    // Mapping functions
    private fun DataReminder.toUiReminder(): Reminder = Reminder(
        id = this.id,
        title = this.title,
        description = this.description,
        dateTime = this.remindAt
    )

    private fun Reminder.toDataReminder(taskId: Int): DataReminder = DataReminder(
        taskId = taskId,
        description = this.description,
        remindAt = this.dateTime ?: 0L,
        title = this.title,
        id = this.id
    )

    private fun Reminder.toDataReminderWithId(id: Int, taskId: Int): DataReminder = DataReminder(
        id = id,
        taskId = taskId,
        description = this.description,
        remindAt = this.dateTime ?: 0L,
        title = this.title
    )
}
