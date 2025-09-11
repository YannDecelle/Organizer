package com.example.a03_architecturemobile.ui.reminder

data class Reminder(
    var title: String,
    var description: String,
    var dateTime: Long? = null
)
