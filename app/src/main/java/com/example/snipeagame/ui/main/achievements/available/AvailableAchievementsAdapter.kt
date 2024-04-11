package com.example.snipeagame.ui.main.achievements.available

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.AchievementsParameters
import com.example.domain.utils.NumberConstants
import com.example.snipeagame.R
import com.example.snipeagame.databinding.ItemAchievementBinding

class AvailableAchievementsAdapter(
    private val achievementClickListener: AchievementClickListener,
    private var getItemInfo: ((Int) -> Unit)
) :
    RecyclerView.Adapter<AvailableAchievementsAdapter.ViewHolder>() {
    private val availableAchievements = mutableListOf<AchievementsParameters>()

    private val TAG = this::class.java.simpleName
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            ItemAchievementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val achievement = availableAchievements[position]
        setViews(holder, achievement)
        setListeners(holder, achievement)
        getAchievementInfo(position)
    }

    fun notifyAchievementChange() {
        notifyItemChanged(NumberConstants.ZERO, availableAchievements.size)
    }

    fun setAchievements(achievements: Collection<AchievementsParameters>) {
        availableAchievements.clear()
        availableAchievements.addAll(achievements)
        availableAchievements.sortBy { it.condition }
        notifyItemChanged(NumberConstants.ZERO, availableAchievements.size)
    }

    override fun getItemCount() = availableAchievements.size

    class ViewHolder(binding: ItemAchievementBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageView = binding.achievementImageView
        val titleTextView = binding.achievementTitleTextView
        val descriptionTextView = binding.achievementDescriptionTextView
        val button = binding.achievementUnlockButton
    }

    private fun setViews(holder: ViewHolder, achievement: AchievementsParameters) {
        holder.apply {
            achievement.apply {
                imageView.setImageResource(R.drawable.lock)
                titleTextView.text = title
                descriptionTextView.text = description
                button.isVisible = isButtonVisible
            }
        }
    }

    private fun setListeners(holder: ViewHolder, achievement: AchievementsParameters) {
        val position = availableAchievements.indexOf(achievement)
        holder.button.setOnClickListener {
            achievementClickListener.onAchievementClick(achievement)
            availableAchievements.remove(achievement)
            notifyItemChanged(position, availableAchievements.size)
        }
    }

    private fun getAchievementInfo(position: Int) {
        getItemInfo.invoke(position)
    }

    interface AchievementClickListener {
        fun onAchievementClick(achievement: AchievementsParameters)
    }
}