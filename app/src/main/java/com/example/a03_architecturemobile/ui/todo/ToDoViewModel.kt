package com.example.a03_architecturemobile.ui.todo


import android.app.Application
import androidx.lifecycle.*
import com.example.a03_architecturemobile.data.TaskRepository
import com.example.a03_architecturemobile.data.Task as DataTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ToDoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = TaskRepository(application)

    private val _tasks = MutableLiveData<List<Task>>(emptyList())
    val tasks: LiveData<List<Task>> = _tasks

    init {
        viewModelScope.launch {
            repository.getAllTasks().collect { dataTasks ->
                _tasks.postValue(dataTasks.map { it.toUiTask() })
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertTask(task.toDataTask())
        }
    }

    fun editTask(pos: Int, title: String, deadline: Long?) {
        val currentList = _tasks.value ?: return
        if (pos !in currentList.indices) return
        val oldTask = currentList[pos]
        val updatedTask = oldTask.copy(title = title, deadline = deadline)
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(updatedTask.toDataTaskWithId(oldTask.id))
        }
    }

    fun deleteTask(pos: Int) {
        val currentList = _tasks.value ?: return
        if (pos !in currentList.indices) return
        val task = currentList[pos]
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTask(task.toDataTaskWithId(task.id))
        }
    }

    fun setTaskDone(pos: Int, isDone: Boolean) {
        val currentList = _tasks.value ?: return
        if (pos !in currentList.indices) return
        val oldTask = currentList[pos]
        val updatedTask = oldTask.copy(isDone = isDone)
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(updatedTask.toDataTaskWithId(oldTask.id))
        }
    }

    // Mapping functions
    private fun DataTask.toUiTask(): Task = Task(
        id = this.id,
        title = this.title,
        isDone = this.isCompleted,
        deadline = this.description?.toLongOrNull() // crude mapping, adjust as needed
    )

    private fun Task.toDataTask(): DataTask = DataTask(
        title = this.title,
        isCompleted = this.isDone,
        description = this.deadline?.toString()
    )

    private fun Task.toDataTaskWithId(id: Int): DataTask = DataTask(
        id = id,
        title = this.title,
        isCompleted = this.isDone,
        description = this.deadline?.toString()
    )
}
