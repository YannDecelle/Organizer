package com.example.a03_architecturemobile.ui.todo

data class Task(
    var title: String,
    var isDone: Boolean = false,
    var deadline: Long? = null // Store as timestamp (milliseconds)
)
