package com.example.a03_architecturemobile.ui.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ToDoViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "To Do List"
    }
    val text: LiveData<String> = _text
}
