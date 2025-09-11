package com.example.a03_architecturemobile.ui.reminder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReminderViewModel : ViewModel() {
    private val _reminders = MutableLiveData<MutableList<Reminder>>(mutableListOf())
    val reminders: LiveData<MutableList<Reminder>> = _reminders

    fun addReminder(reminder: Reminder) {
        val list = _reminders.value ?: mutableListOf()
        list.add(reminder)
        _reminders.value = list.toMutableList()
    }

    fun editReminder(pos: Int, title: String, description: String, dateTime: Long?) {
        val list = _reminders.value ?: return
        if (pos in list.indices) {
            list[pos].title = title
            list[pos].description = description
            list[pos].dateTime = dateTime
            _reminders.value = list.toMutableList()
        }
    }

    fun deleteReminder(pos: Int) {
        val list = _reminders.value ?: return
        if (pos in list.indices) {
            list.removeAt(pos)
            _reminders.value = list.toMutableList()
        }
    }
}
