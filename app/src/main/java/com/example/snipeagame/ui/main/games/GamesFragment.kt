package com.example.snipeagame.ui.main.games

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.example.snipeagame.R
import com.example.snipeagame.base.BaseFragment
import com.example.snipeagame.databinding.FragmentGamesBinding
import com.example.snipeagame.ui.main.games.all_games.AllGamesFragment
import com.example.snipeagame.ui.main.games.my_games.MyGamesFragment
import com.example.snipeagame.utils.FragmentTabAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class GamesFragment : BaseFragment<FragmentGamesBinding>(FragmentGamesBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupGamesAdapter()
        setupViewPagerAdapter()
    }

    private fun setupViewPagerAdapter() {
        val adapter = setupGamesAdapter()
        val viewPager = setupViewPager(adapter)
        setupTabLayout(adapter, viewPager)
    }

    private fun setupGamesAdapter() = FragmentTabAdapter(requireActivity()).apply {
        addFragment(AllGamesFragment(), R.string.all_games)
        addFragment(MyGamesFragment(), R.string.my_games)
    }

    private fun setupViewPager(adapter: FragmentTabAdapter): ViewPager2 {
        with(binding) {
            val viewPager = gamesViewPager
            gamesViewPager.adapter = adapter
            return viewPager
        }
    }

    private fun setupTabLayout(adapter: FragmentTabAdapter, viewPager: ViewPager2) {
        val tabs: TabLayout = binding.gamesTabLayout
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = getString(adapter.getTabTitle(position))
        }.attach()
    }
}