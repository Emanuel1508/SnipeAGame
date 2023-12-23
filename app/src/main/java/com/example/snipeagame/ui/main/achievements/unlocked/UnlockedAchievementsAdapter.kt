package com.example.snipeagame.ui.main.achievements.unlocked

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.AchievementsParameters
import com.example.domain.utils.NumberConstants
import com.example.snipeagame.R
import com.example.snipeagame.databinding.ItemAchievementBinding

class UnlockedAchievementsAdapter : RecyclerView.Adapter<UnlockedAchievementsAdapter.ViewHolder>() {
    private var unlockedAchievementsList = mutableListOf<AchievementsParameters>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemAchievementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val achievement = unlockedAchievementsList[position]
        setViews(holder, achievement)
    }

    override fun getItemCount() = unlockedAchievementsList.size

    fun setUnlockedAchievements(achievements: List<AchievementsParameters>) {
        unlockedAchievementsList.clear()
        unlockedAchievementsList.addAll(achievements)
        notifyItemChanged(NumberConstants.ZERO, unlockedAchievementsList.size)
    }

    class ViewHolder(binding: ItemAchievementBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageView = binding.achievementImageView
        val titleTextView = binding.achievementTitleTextView
        val descriptionTextView = binding.achievementDescriptionTextView
        val button = binding.achievementUnlockButton
    }

    private fun setViews(holder: ViewHolder, achievement: AchievementsParameters) {
        holder.apply {
            achievement.apply {
                imageView.setImageResource(R.drawable.check_mark)
                titleTextView.text = title
                descriptionTextView.text = description
                button.isVisible = false
            }
        }
    }
}