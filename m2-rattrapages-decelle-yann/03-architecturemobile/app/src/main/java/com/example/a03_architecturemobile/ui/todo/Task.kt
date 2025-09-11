package com.example.a03_architecturemobile.ui.todo

data class Task(
    var id: Int = 0,
    var title: String,
    var isDone: Boolean = false,
    var deadline: Long? = null
)
