package com.example.a03_architecturemobile.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a03_architecturemobile.ui.reminder.ReminderViewModel
import java.util.Date
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.a03_architecturemobile.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = binding.recyclerViewHistory
        val adapter = HistoryAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Use ReminderViewModel to get all reminders and filter deprecated ones
        val reminderViewModel = ViewModelProvider(this).get(ReminderViewModel::class.java)
        reminderViewModel.reminders.observe(viewLifecycleOwner) { reminders ->
            val now = Date().time
            val pastReminders = reminders.filter {
                val dateTime = it.dateTime
                dateTime != null && dateTime.toLong() <= now
            }.sortedBy { it.dateTime ?: 0L }
            adapter.updateReminders(pastReminders)
        }

        // Button to remove every depreceted reminders. Will purge history AND reminder tabs
        binding.buttonPurgeHistory.setOnClickListener {
            reminderViewModel.purgeDeprecatedReminders()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
