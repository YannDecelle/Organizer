package com.example.a03_architecturemobile.ui.reminder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import com.example.a03_architecturemobile.R
import java.text.SimpleDateFormat
import java.util.*


class ReminderAdapter(
    private val reminderList: MutableList<Reminder>,
    private val onEdit: (Int) -> Unit,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder>() {

    // Create a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_reminder, parent, false)
        return ReminderViewHolder(itemView)
    }

    // Bind the data to the ViewHolder
    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        holder.bind(reminderList[position])
    }

    // Get the total number of items
    override fun getItemCount(): Int = reminderList.size

    // ViewHolder for reminder items
    inner class ReminderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleText: TextView = view.findViewById(R.id.text_reminder_title)
        private val descriptionText: TextView = view.findViewById(R.id.text_reminder_description)
        private val dateTimeText: TextView = view.findViewById(R.id.text_reminder_datetime)
        private val editButton: ImageButton = view.findViewById(R.id.button_edit_reminder)
        private val deleteButton: ImageButton = view.findViewById(R.id.button_delete_reminder)

        // Bind data to the view
        fun bind(reminder: Reminder) {
            titleText.text = reminder.title
            descriptionText.text = reminder.description
            dateTimeText.text = reminder.dateTime?.let { dateTimeMillis ->
                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                dateFormat.format(Date(dateTimeMillis))
            } ?: ""

            editButton.setOnClickListener { onEdit(adapterPosition) }
            deleteButton.setOnClickListener { onDelete(adapterPosition) }
        }
    }

    // Update the list using DiffUtil
    fun updateReminders(newReminderList: List<Reminder>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize() = reminderList.size
            override fun getNewListSize() = newReminderList.size
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldReminder = reminderList[oldItemPosition]
                val newReminder = newReminderList[newItemPosition]
                return oldReminder.title == newReminder.title && oldReminder.dateTime == newReminder.dateTime
            }
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return reminderList[oldItemPosition] == newReminderList[newItemPosition]
            }
        }
        // Calculate the differences between the old and new lists
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        reminderList.clear()
        reminderList.addAll(newReminderList)
        diffResult.dispatchUpdatesTo(this)
    }
}
