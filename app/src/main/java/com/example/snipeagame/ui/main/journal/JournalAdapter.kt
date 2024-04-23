package com.example.snipeagame.ui.main.journal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.JournalParameters
import com.example.domain.utils.NumberConstants
import com.example.snipeagame.databinding.ItemJournalBinding

class JournalAdapter(private var journalItemClickListener: JournalItemClickListener) :
    RecyclerView.Adapter<JournalAdapter.ViewHolder>() {
    private val journalEntries = mutableListOf<JournalParameters>()
    private val TAG = this::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemJournalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = journalEntries.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val journal = journalEntries[position]
        setupViews(holder, journal)
        setupListeners(holder, journal)
    }

    class ViewHolder(binding: ItemJournalBinding) : RecyclerView.ViewHolder(binding.root) {
        val locationTextView = binding.locationTextView
        val dateTextView = binding.dateTextView
    }

    private fun setupViews(holder: ViewHolder, journal: JournalParameters) {
        holder.apply {
            journal.apply {
                locationTextView.text = location
                dateTextView.text = date
            }
        }
    }

    private fun setupListeners(holder: ViewHolder, journal: JournalParameters) {
        holder.itemView.setOnClickListener {
            journalItemClickListener.onJournalItemClick(journal)
        }
    }

    fun setMyJournalEntries(journalList: List<JournalParameters>) {
        journalEntries.clear()
        journalEntries.addAll(journalList)
        notifyItemChanged(NumberConstants.ZERO, journalEntries.size)
    }

    interface JournalItemClickListener {
        fun onJournalItemClick(journal: JournalParameters)
    }
}
