package com.example.a03_architecturemobile.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a03_architecturemobile.R
import com.example.a03_architecturemobile.ui.reminder.Reminder
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter(private var reminders: List<Reminder>) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reminder, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(reminders[position])
    }

    override fun getItemCount(): Int = reminders.size

    fun updateReminders(newReminders: List<Reminder>) {
        reminders = newReminders
        notifyDataSetChanged()
    }

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val title: TextView = itemView.findViewById(R.id.text_reminder_title)
    private val description: TextView = itemView.findViewById(R.id.text_reminder_description)
    private val dateTime: TextView = itemView.findViewById(R.id.text_reminder_datetime)
    private val editButton: View? = itemView.findViewById(R.id.button_edit_reminder)
    private val deleteButton: View? = itemView.findViewById(R.id.button_delete_reminder)

        fun bind(reminder: Reminder) {
            title.text = reminder.title
            description.text = reminder.description
            dateTime.text = reminder.dateTime?.let {
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                sdf.format(Date(it))
            } ?: ""
            // Hide edit/delete buttons in history
            editButton?.visibility = View.GONE
            deleteButton?.visibility = View.GONE
        }
    }
}
