package com.example.snipeagame.ui.main.journal.journal_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.utils.NumberConstants
import com.example.snipeagame.databinding.ItemJournalImageBinding
import com.squareup.picasso.Picasso

class JournalDetailsAdapter : RecyclerView.Adapter<JournalDetailsAdapter.ViewHolder>() {
    private val images = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemJournalImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = images.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = images[position]
        setupViews(holder, image)
    }

    class ViewHolder(binding: ItemJournalImageBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.journalImageView
    }

    private fun setupViews(holder: ViewHolder, imageResource: String) {
        Picasso.get().load(imageResource)
            .resize(350,350)
            .centerInside()
            .rotate(90F)
            .into(holder.image)
    }

    fun setupJournalImages(imageResources: List<String>) {
        images.clear()
        images.addAll(imageResources)
        notifyItemChanged(NumberConstants.ZERO, images.size)
    }
}