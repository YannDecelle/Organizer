package com.example.a03_architecturemobile.data

import android.content.Context
import kotlinx.coroutines.flow.Flow

// Repository to manage Task data operations
class TaskRepository(context: Context) {
    private val taskDao = AppDatabase.getDatabase(context).taskDao()

    fun getAllTasks(): Flow<List<Task>> = taskDao.getAll()

    suspend fun insertTask(task: Task) = taskDao.insert(task)

    suspend fun updateTask(task: Task) = taskDao.update(task)

    suspend fun deleteTask(task: Task) = taskDao.delete(task)
}
