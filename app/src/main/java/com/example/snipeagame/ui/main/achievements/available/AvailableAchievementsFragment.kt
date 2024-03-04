package com.example.snipeagame.ui.main.achievements.available

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.AchievementsParameters
import com.example.domain.models.Result
import com.example.domain.utils.ErrorMessage
import com.example.snipeagame.R
import com.example.snipeagame.base.BaseFragment
import com.example.snipeagame.databinding.FragmentAvailableAchievementsBinding
import com.example.snipeagame.utils.AlertDialogFragment
import com.example.snipeagame.utils.hideRefresh
import com.example.snipeagame.utils.mapToUI
import com.example.snipeagame.utils.showRefresh
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
    }

    private fun setupAdapter() {
        adapter = AvailableAchievementsAdapter(this) { position ->
            viewModel.pendingUnlock(position)
            updateAchievement(position)
        }
        recyclerView = binding.availableAchievementsRecyclerView
        recyclerView.adapter = adapter

        viewModel.achievements.observe(viewLifecycleOwner) { achievements ->
            adapter.setAchievements(achievements)
        }
    }

    private fun updateAchievement(position: Int) {
        recyclerView.post {
            adapter.notifyAchievementChange(position)
        }
    }

    private fun setupObservers() {
        with(viewModel) {
            loadingLiveData.observe(viewLifecycleOwner) {
                updateRefreshAnimation(it)
            }
            errorLiveData.observe(viewLifecycleOwner) { error ->
                showAlertDialog(error.message)
            }
        }
    }

    private fun updateRefreshAnimation(value: Result.Loading) {
        when (value.shouldShowLoading) {
            true -> showLoadingAnimation()
            false -> hideLoadingAnimation()
        }
    }

    private fun showLoadingAnimation() = binding.availableAchievementsSwipeRefresh.showRefresh()
    private fun hideLoadingAnimation() = binding.availableAchievementsSwipeRefresh.hideRefresh()

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
