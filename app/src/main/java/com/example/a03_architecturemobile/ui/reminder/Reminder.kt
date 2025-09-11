package com.example.a03_architecturemobile.ui.reminder

// Define the Reminder data class used in the UI layer
data class Reminder(
    var id: Int = 0,
    var title: String,
    var description: String = "",
    var dateTime: Long? = null
)
