package com.example.snipeagame.ui.main.achievements.available

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.AchievementsParameters
import com.example.domain.utils.ErrorMessage
import com.example.snipeagame.R
import com.example.snipeagame.base.BaseFragment
import com.example.snipeagame.databinding.FragmentAvailableAchievementsBinding
import com.example.snipeagame.utils.AlertDialogFragment
import com.example.snipeagame.utils.hideRefresh
import com.example.snipeagame.utils.mapToUI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AvailableAchievementsFragment :
    BaseFragment<FragmentAvailableAchievementsBinding>(FragmentAvailableAchievementsBinding::inflate),
    AvailableAchievementsAdapter.AchievementClickListener {
    private val viewModel: AvailableAchievementsViewModel by viewModels()
    private lateinit var adapter: AvailableAchievementsAdapter
    private lateinit var recyclerView: RecyclerView
    private val TAG = this::class.java.simpleName

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupAdapter()
        setupListener()
        setupLoading()
    }

    private fun setupAdapter() {
        adapter = AvailableAchievementsAdapter(this) { position ->
            viewModel.pendingUnlock(position)
            updateAchievement()
        }
        recyclerView = binding.availableAchievementsRecyclerView
        recyclerView.adapter = adapter

        viewModel.achievements.observe(viewLifecycleOwner) { achievements ->
            adapter.setAchievements(achievements)
        }
    }

    private fun updateAchievement() {
        recyclerView.post {
            adapter.notifyAchievementChange()
        }
    }

    private fun setupObservers() {
        with(viewModel) {
            errorLiveData.observe(viewLifecycleOwner) { error ->
                showAlertDialog(error.message)
            }
        }
    }

    private fun setupListener() {
        with(binding) {
            availableAchievementsSwipeRefresh.setOnRefreshListener {
                viewModel.achievements.observe(viewLifecycleOwner) { achievements ->
                    adapter.setAchievements(achievements)
                }
                availableAchievementsSwipeRefresh.hideRefresh()
            }
        }
    }

    private fun setupLoading() {
        with(binding) {
            viewModel.loadingLiveData.observe(viewLifecycleOwner) { isLoading ->
                availableAchievementsSwipeRefresh.isRefreshing = isLoading.shouldShowLoading
            }
        }
    }

    private fun showAlertDialog(error: ErrorMessage) {
        val alertDialogFragment =
            AlertDialogFragment.newInstance(title = getString(R.string.oops_title),
                description = getString(error.mapToUI()),
                onRetryClick = {})
        alertDialogFragment.show(parentFragmentManager, TAG)
    }

    override fun onAchievementClick(achievement: AchievementsParameters) {
        viewModel.unlockAchievement(achievement)
    }
}
