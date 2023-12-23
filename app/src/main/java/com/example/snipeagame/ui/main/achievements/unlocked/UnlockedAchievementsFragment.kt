package com.example.snipeagame.ui.main.achievements.unlocked

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.Result
import com.example.domain.utils.ErrorMessage
import com.example.snipeagame.R
import com.example.snipeagame.base.BaseFragment
import com.example.snipeagame.databinding.FragmentUnlockedAchievementsBinding
import com.example.snipeagame.utils.AlertDialogFragment
import com.example.snipeagame.utils.hideRefresh
import com.example.snipeagame.utils.mapToUI
import com.example.snipeagame.utils.showRefresh
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UnlockedAchievementsFragment :
    BaseFragment<FragmentUnlockedAchievementsBinding>(FragmentUnlockedAchievementsBinding::inflate) {
    private val viewModel: UnlockedAchievementsViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView

    private val TAG = this::class.java.simpleName
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupObservers()
    }

    private fun setupAdapter() {
        val adapter = UnlockedAchievementsAdapter()
        viewModel.unlockedAchievements.observe(viewLifecycleOwner) { achievements ->
            adapter.setUnlockedAchievements(achievements)
        }
        recyclerView = binding.unlockedAchievementsRecyclerView
        recyclerView.adapter = adapter
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

    private fun showLoadingAnimation() = binding.unlockedAchievementsSwipeRefresh.showRefresh()
    private fun hideLoadingAnimation() = binding.unlockedAchievementsSwipeRefresh.hideRefresh()

    private fun showAlertDialog(error: ErrorMessage) {
        val alertDialogFragment =
            AlertDialogFragment.newInstance(title = getString(R.string.oops_title),
                description = getString(error.mapToUI()),
                onRetryClick = {})
        alertDialogFragment.show(parentFragmentManager, TAG)
    }
}