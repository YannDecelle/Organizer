package com.example.a03_architecturemobile.ui.todo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import java.text.SimpleDateFormat
import java.util.*
import com.example.a03_architecturemobile.R

class TaskAdapter(
    private val taskList: MutableList<Task>,
    private val onEdit: (Int) -> Unit,
    private val onDelete: (Int) -> Unit,
    private val onDoneChanged: (Int, Boolean) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    // Create a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    // Bind the data to the ViewHolder
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(taskList[position])
    }

    // Get the total number of items
    override fun getItemCount(): Int = taskList.size

    // ViewHolder for task items
    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val doneCheckBox: CheckBox = view.findViewById(R.id.checkbox_done)
        private val titleText: TextView = view.findViewById(R.id.text_task_title)
        private val deadlineText: TextView = view.findViewById(R.id.text_task_deadline)
        private val editButton: ImageButton = view.findViewById(R.id.button_edit)
        private val deleteButton: ImageButton = view.findViewById(R.id.button_delete)

        // Bind data to the view
        fun bind(taskItem: Task) {
            titleText.text = taskItem.title
            doneCheckBox.isChecked = taskItem.isDone
            deadlineText.text = taskItem.deadline?.let { deadlineMillis ->
                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                dateFormat.format(Date(deadlineMillis))
            } ?: ""

            doneCheckBox.setOnCheckedChangeListener(null)
            doneCheckBox.setOnCheckedChangeListener { _, isChecked ->
                onDoneChanged(adapterPosition, isChecked)
            }
            editButton.setOnClickListener { onEdit(adapterPosition) }
            deleteButton.setOnClickListener { onDelete(adapterPosition) }
        }
    }

    // Update the list using DiffUtil
    fun updateTasks(newTaskList: List<Task>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize() = taskList.size
            override fun getNewListSize() = newTaskList.size
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldTask = taskList[oldItemPosition]
                val newTask = newTaskList[newItemPosition]
                return oldTask.id == newTask.id
            }
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return taskList[oldItemPosition] == newTaskList[newItemPosition]
            }
        }
        // Calculate the differences between the old and new lists
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        taskList.clear()
        taskList.addAll(newTaskList)
        diffResult.dispatchUpdatesTo(this)
    }
}
