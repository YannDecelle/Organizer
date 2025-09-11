package com.example.a03_architecturemobile.ui.reminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.a03_architecturemobile.databinding.FragmentReminderBinding

class ReminderFragment : Fragment() {

    private lateinit var adapter: ReminderAdapter
    private var _binding: FragmentReminderBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // Sets up view binding and observes ReminderViewModel text updates
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val reminderViewModel = ViewModelProvider(this).get(ReminderViewModel::class.java)
        _binding = FragmentReminderBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set up RecyclerView
        val recyclerView = binding.recyclerViewReminders
        adapter = ReminderAdapter(mutableListOf(),
            onEdit = { pos -> showReminderDialog(reminderViewModel, pos) },
            onDelete = { pos -> reminderViewModel.deleteReminder(pos) }
        )
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Observe reminders LiveData
        reminderViewModel.reminders.observe(viewLifecycleOwner) { reminders ->
            adapter.updateReminders(reminders)
        }

        // Button to add a new reminder
        val fab: FloatingActionButton = binding.fabAddReminder
        fab.setOnClickListener {
            showReminderDialog(reminderViewModel, null)
        }

        // Page title
        binding.textReminder.text = "Reminders"

        return root
    }

    // Show dialog to add or edit a reminder
    private fun showReminderDialog(viewModel: ReminderViewModel, editPos: Int?) {
        val context = requireContext()
        val inputTitle = EditText(context)
        inputTitle.hint = "Reminder title"
    val inputDescription = EditText(context)
    inputDescription.hint = "Description"
    val dateTimeButton = EditText(context)
    dateTimeButton.hint = "Select date & time"
    dateTimeButton.isFocusable = false

    val layout = androidx.appcompat.widget.LinearLayoutCompat(context)
    layout.orientation = androidx.appcompat.widget.LinearLayoutCompat.VERTICAL
    layout.addView(inputTitle)
    layout.addView(inputDescription)
    layout.addView(dateTimeButton)

        var selectedDateTime: Long? = null
        var selectedYear = -1
        var selectedMonth = -1
        var selectedDay = -1
        var selectedHour = 0
        var selectedMinute = 0
        if (editPos != null) {
            val reminder = viewModel.reminders.value?.get(editPos)
            inputTitle.setText(reminder?.title ?: "")
            inputDescription.setText(reminder?.description ?: "")
            if (reminder?.dateTime != null) {
                val cal = java.util.Calendar.getInstance().apply { timeInMillis = reminder.dateTime!! }
                selectedYear = cal.get(java.util.Calendar.YEAR)
                selectedMonth = cal.get(java.util.Calendar.MONTH)
                selectedDay = cal.get(java.util.Calendar.DAY_OF_MONTH)
                selectedHour = cal.get(java.util.Calendar.HOUR_OF_DAY)
                selectedMinute = cal.get(java.util.Calendar.MINUTE)
                dateTimeButton.setText("%02d/%02d/%04d %02d:%02d".format(selectedDay, selectedMonth+1, selectedYear, selectedHour, selectedMinute))
                selectedDateTime = reminder.dateTime
            }
        }

        dateTimeButton.setOnClickListener {
            val cal = java.util.Calendar.getInstance()
            if (selectedDateTime != null) {
                cal.timeInMillis = selectedDateTime!!
            }
            val dateListener = android.app.DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                selectedYear = year
                selectedMonth = month
                selectedDay = dayOfMonth
                val timeListener = android.app.TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    selectedHour = hourOfDay
                    selectedMinute = minute
                    cal.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute, 0)
                    selectedDateTime = cal.timeInMillis
                    dateTimeButton.setText("%02d/%02d/%04d %02d:%02d".format(selectedDay, selectedMonth+1, selectedYear, selectedHour, selectedMinute))
                }
                android.app.TimePickerDialog(context, timeListener, cal.get(java.util.Calendar.HOUR_OF_DAY), cal.get(java.util.Calendar.MINUTE), true).show()
            }
            android.app.DatePickerDialog(context, dateListener, cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH), cal.get(java.util.Calendar.DAY_OF_MONTH)).show()
        }

        AlertDialog.Builder(context)
            .setTitle(if (editPos == null) "Add Reminder" else "Edit Reminder")
            .setView(layout)
            .setPositiveButton("Save") { _, _ ->
                val title = inputTitle.text.toString().trim()
                val description = inputDescription.text.toString().trim()
                if (title.isEmpty()) {
                    Toast.makeText(context, "Reminder title cannot be empty", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                if (editPos == null) {
                    viewModel.addReminder(Reminder(0, title, description, selectedDateTime))
                } else {
                    viewModel.editReminder(editPos, title, description, selectedDateTime)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // Clean up binding
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
