package com.example.a03_architecturemobile.ui.reminder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReminderViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Reminder"
    }
    val text: LiveData<String> = _text
}
