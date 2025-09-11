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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Get ViewModel instance
        val toDoViewModel = ViewModelProvider(this).get(ToDoViewModel::class.java)
        _binding = FragmentToDoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set up RecyclerView 
        val recyclerView = binding.recyclerViewTasks
        adapter = TaskAdapter(mutableListOf(),
            onEdit = { pos -> showTaskDialog(toDoViewModel, adapter, pos) },
            onDelete = { pos -> toDoViewModel.deleteTask(pos) },
            onDoneChanged = { pos, isDone -> toDoViewModel.setTaskDone(pos, isDone) }
        )
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Observe tasks LiveData
        toDoViewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            adapter.updateTasks(tasks)
        }

        // FloatingActionButton to add a new task
        val fab: FloatingActionButton = binding.fabAddTask
        fab.setOnClickListener {
            showTaskDialog(toDoViewModel, adapter, null)
        }

        // Set the page title
        binding.textToDo.text = "To Do List"

        return root
    }

    // Show dialog to add or edit a task
    private fun showTaskDialog(viewModel: ToDoViewModel, adapter: TaskAdapter, editPos: Int?) {

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
        if (editPos != null) {
            val task = viewModel.tasks.value?.get(editPos)
            input.setText(task?.title ?: "")
            if (task?.deadline != null) {
                val cal = Calendar.getInstance().apply { timeInMillis = task.deadline!! }
                deadlineButton.setText("%02d/%02d/%04d".format(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH)+1, cal.get(Calendar.YEAR)))
                selectedDeadline = task.deadline
            }
        }

        // Date picker for deadline
        deadlineButton.setOnClickListener {
            val cal = Calendar.getInstance()
            val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                cal.set(year, month, dayOfMonth, 0, 0, 0)
                selectedDeadline = cal.timeInMillis
                deadlineButton.setText("%02d/%02d/%04d".format(dayOfMonth, month+1, year))
            }
            DatePickerDialog(context, listener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
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
                    viewModel.addTask(Task(title, false, selectedDeadline))
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
