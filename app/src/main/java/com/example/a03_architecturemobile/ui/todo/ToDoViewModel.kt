package com.example.a03_architecturemobile.ui.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// ViewModel to manage To Do tasks
class ToDoViewModel : ViewModel() {

    // LiveData holding the list of tasks
    private val _tasks = MutableLiveData<MutableList<Task>>(mutableListOf())
    val tasks: LiveData<MutableList<Task>> = _tasks

    fun addTask(task: Task) {
        val list = _tasks.value ?: mutableListOf()
        list.add(task)
        _tasks.value = list.toMutableList()
    }

    fun editTask(pos: Int, title: String, deadline: Long?) {
        val list = _tasks.value ?: return
        if (pos in list.indices) {
            list[pos].title = title
            list[pos].deadline = deadline
            _tasks.value = list.toMutableList()
        }
    }

    fun deleteTask(pos: Int) {
        val list = _tasks.value ?: return
        if (pos in list.indices) {
            list.removeAt(pos)
            _tasks.value = list.toMutableList()
        }
    }

        
    fun setTaskDone(pos: Int, isDone: Boolean) {
        val list = _tasks.value ?: return
        if (pos in list.indices) {
            list[pos].isDone = isDone
            _tasks.value = list.toMutableList()
        }
    }
}
