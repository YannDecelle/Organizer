package com.example.a03_architecturemobile.ui.todo

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a03_architecturemobile.databinding.FragmentToDoBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*


// Fragment to display and manage the To Do list
class ToDoFragment : Fragment() {
    private var _binding: FragmentToDoBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private lateinit var adapter: TaskAdapter
    private lateinit var reminderViewModel: com.example.a03_architecturemobile.ui.reminder.ReminderViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Get ViewModel instance
    val toDoViewModel = ViewModelProvider(this).get(ToDoViewModel::class.java)
    reminderViewModel = ViewModelProvider(this).get(com.example.a03_architecturemobile.ui.reminder.ReminderViewModel::class.java)
        _binding = FragmentToDoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set up RecyclerView 
        val recyclerView = binding.recyclerViewTasks
        adapter = TaskAdapter(mutableListOf(),
            onEdit = { pos -> showTaskDialog(toDoViewModel, pos) },
            onDelete = { pos -> toDoViewModel.deleteTask(pos) },
            onDoneChanged = { pos, isDone -> toDoViewModel.setTaskDone(pos, isDone) }
        )
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Observe tasks LiveData
        toDoViewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            adapter.updateTasks(tasks)
        }

        // Button to add a new task
        val fab: FloatingActionButton = binding.fabAddTask
        fab.setOnClickListener {
            showTaskDialog(toDoViewModel, null)
        }

        // Page title
        binding.textToDo.text = "To Do List"

        return root
    }

    // Show dialog to add or edit a task
    private fun showTaskDialog(viewModel: ToDoViewModel, editPos: Int?) {

        // Get context and create input fields
        val context = requireContext()
        val input = EditText(context)
        input.hint = "Task title"
        val deadlineButton = EditText(context)
        deadlineButton.hint = "Select deadline (optional)"
        deadlineButton.isFocusable = false

        // Layout to hold the input fields
        val layout = androidx.appcompat.widget.LinearLayoutCompat(context)
        layout.orientation = androidx.appcompat.widget.LinearLayoutCompat.VERTICAL
        layout.addView(input)
        layout.addView(deadlineButton)

        // If editing, pre-fill the fields
        var selectedDeadline: Long? = null
        var selectedYear = -1
        var selectedMonth = -1
        var selectedDay = -1
        var selectedHour = 0
        var selectedMinute = 0
        if (editPos != null) {
            val task = viewModel.tasks.value?.get(editPos)
            input.setText(task?.title ?: "")
            if (task?.deadline != null) {
                val cal = Calendar.getInstance().apply { timeInMillis = task.deadline!! }
                selectedYear = cal.get(Calendar.YEAR)
                selectedMonth = cal.get(Calendar.MONTH)
                selectedDay = cal.get(Calendar.DAY_OF_MONTH)
                selectedHour = cal.get(Calendar.HOUR_OF_DAY)
                selectedMinute = cal.get(Calendar.MINUTE)
                deadlineButton.setText("%02d/%02d/%04d %02d:%02d".format(selectedDay, selectedMonth+1, selectedYear, selectedHour, selectedMinute))
                selectedDeadline = task.deadline
            }
        }

        // Date and time picker for deadline
        deadlineButton.setOnClickListener {
            val cal = Calendar.getInstance()
            if (selectedDeadline != null) {
                cal.timeInMillis = selectedDeadline!!
            }
            val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                selectedYear = year
                selectedMonth = month
                selectedDay = dayOfMonth

                val timeListener = android.app.TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    selectedHour = hourOfDay
                    selectedMinute = minute
                    cal.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute, 0)
                    selectedDeadline = cal.timeInMillis
                    deadlineButton.setText("%02d/%02d/%04d %02d:%02d".format(selectedDay, selectedMonth+1, selectedYear, selectedHour, selectedMinute))
                }
                android.app.TimePickerDialog(context, timeListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
            }
            DatePickerDialog(context, dateListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Build and show the dialog
        AlertDialog.Builder(context)
            .setTitle(if (editPos == null) "Add Task" else "Edit Task")
            .setView(layout)
            .setPositiveButton("Save") { _, _ ->
                val title = input.text.toString().trim()
                if (title.isEmpty()) {
                    Toast.makeText(context, "Task title cannot be empty", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                if (editPos == null) {
                    viewModel.addTask(Task(0, title, false, selectedDeadline))
                    // Make it so a reminder is set if a deadline is specified in the task
                    if (selectedDeadline != null) {
                        val allNotifications = com.example.a03_architecturemobile.data.NotificationTime.values().toList()
                        reminderViewModel.addReminder(
                            com.example.a03_architecturemobile.ui.reminder.Reminder(
                                id = 0,
                                title = "Deadline",
                                description = title,
                                dateTime = selectedDeadline,
                                notifications = allNotifications
                            )
                        )
                    }
                } else {
                    viewModel.editTask(editPos, title, selectedDeadline)
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
