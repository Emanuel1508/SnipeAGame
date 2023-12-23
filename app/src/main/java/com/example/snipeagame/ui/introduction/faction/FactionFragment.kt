package com.example.snipeagame.ui.introduction.faction

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.example.snipeagame.base.BaseFragment
import com.example.snipeagame.databinding.FragmentFactionBinding
import com.example.snipeagame.ui.introduction.faction.airborne.FactionAirborneFragment
import com.example.snipeagame.ui.introduction.faction.immortals.FactionImmortalsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FactionFragment : BaseFragment<FragmentFactionBinding>(FragmentFactionBinding::inflate) {
    private val TAG = this::class.java.simpleName
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFactionAdapter()
        setupViewPagerAdapter()
    }

    private fun setupFactionAdapter() = FactionAdapter(requireActivity()).apply {
        addFragment(FactionItemOneFragment())
        addFragment(FactionAirborneFragment())
        addFragment(FactionImmortalsFragment())
    }

    private fun setupViewPager(adapter: FactionAdapter): ViewPager2 {
        with(binding) {
            val viewPager = factionViewPager
            factionViewPager.adapter = adapter
            return viewPager
        }
    }

    private fun setupViewPagerAdapter() {
        val adapter = setupFactionAdapter()
        val viewPager = setupViewPager(adapter)
        setupCircleIndicator(viewPager)
    }

    private fun setupCircleIndicator(viewPager: ViewPager2) {
        binding.factionCircularIndicator.setViewPager(viewPager)
    }
}