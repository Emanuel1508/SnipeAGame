package com.example.snipeagame.ui.main.achievements

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.snipeagame.R
import com.example.snipeagame.base.BaseFragment
import com.example.snipeagame.databinding.FragmentAchievementsBinding
import com.example.snipeagame.ui.main.achievements.available.AvailableAchievementsFragment
import com.example.snipeagame.ui.main.achievements.unlocked.UnlockedAchievementsFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AchievementsFragment :
    BaseFragment<FragmentAchievementsBinding>(FragmentAchievementsBinding::inflate) {
    private val viewModel: AchievementsViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAchievementsAdapter()
        setupViewPagerAdapter()
    }

    private fun setupViewPagerAdapter() {
        val adapter = setupAchievementsAdapter()
        val viewPager = setupViewPager(adapter)
        setupTabLayout(adapter, viewPager)
    }

    private fun setupAchievementsAdapter() = AchievementsAdapter(requireActivity()).apply {
        addFragment(AvailableAchievementsFragment(), R.string.available_achievements)
        addFragment(UnlockedAchievementsFragment(), R.string.unlocked_achievements)
    }

    private fun setupViewPager(adapter: AchievementsAdapter): ViewPager2 {
        with(binding) {
            val viewPager = achievementsViewPager
            achievementsViewPager.adapter = adapter
            return viewPager
        }
    }

    private fun setupTabLayout(adapter: AchievementsAdapter, viewPager: ViewPager2) {
        val tabs: TabLayout = binding.achievementsTabLayout
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = getString(adapter.getTabTitle(position))
        }.attach()
    }
}