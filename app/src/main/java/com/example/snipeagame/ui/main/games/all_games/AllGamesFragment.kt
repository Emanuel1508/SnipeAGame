package com.example.snipeagame.ui.main.games.all_games

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.Result
import com.example.domain.utils.ErrorMessage
import com.example.snipeagame.R
import com.example.snipeagame.base.BaseFragment
import com.example.snipeagame.databinding.FragmentAllGamesBinding
import com.example.snipeagame.ui.main.games.GamesFragmentDirections
import com.example.snipeagame.utils.AlertDialogFragment
import com.example.snipeagame.utils.hideRefresh
import com.example.snipeagame.utils.mapToUI
import com.example.snipeagame.utils.showRefresh
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllGamesFragment : BaseFragment<FragmentAllGamesBinding>(FragmentAllGamesBinding::inflate) {
    private val viewModel: AllGamesViewModel by viewModels()
    private lateinit var adapter: AllGamesAdapter
    private lateinit var recyclerView: RecyclerView
    private val TAG = this::class.java.simpleName
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onCreateGameButtonClick()
        setupAdapter()
        setupObservers()
    }

    private fun setupAdapter() {
        adapter = AllGamesAdapter()
        recyclerView = binding.allGamesRecyclerView
        recyclerView.adapter = adapter

        viewModel.games.observe(viewLifecycleOwner) { games ->
            adapter.setGames(games)
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

    private fun navigateToCreateGameFragment() {
        findNavController()
            .navigate(GamesFragmentDirections.actionGamesFragmentToCreateGameFragment())
    }

    private fun onCreateGameButtonClick() {
        binding.createGameButton.setOnClickListener {
            navigateToCreateGameFragment()
        }
    }

    private fun showLoadingAnimation() = binding.allGamesSwipeRefresh.showRefresh()
    private fun hideLoadingAnimation() = binding.allGamesSwipeRefresh.hideRefresh()

    private fun showAlertDialog(error: ErrorMessage) {
        val alertDialogFragment =
            AlertDialogFragment.newInstance(title = getString(R.string.oops_title),
                description = getString(error.mapToUI()),
                onRetryClick = {})
        alertDialogFragment.show(parentFragmentManager, TAG)
    }
}