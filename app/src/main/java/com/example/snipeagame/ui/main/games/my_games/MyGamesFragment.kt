package com.example.snipeagame.ui.main.games.my_games

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.GameParameters
import com.example.domain.models.Result
import com.example.domain.utils.ErrorMessage
import com.example.snipeagame.R
import com.example.snipeagame.base.BaseFragment
import com.example.snipeagame.databinding.FragmentMyGamesBinding
import com.example.snipeagame.ui.main.games.GamesFragmentDirections
import com.example.snipeagame.utils.AlertDialogFragment
import com.example.snipeagame.utils.hideRefresh
import com.example.snipeagame.utils.mapToUI
import com.example.snipeagame.utils.showRefresh
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyGamesFragment : BaseFragment<FragmentMyGamesBinding>(FragmentMyGamesBinding::inflate) {
    private val viewModel: MyGamesViewModel by viewModels()
    private lateinit var adapter: MyGamesAdapter
    private lateinit var recyclerView: RecyclerView
    private val TAG = this::class.java.simpleName

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupObservers()
    }

    private fun setupAdapter() {
        adapter = MyGamesAdapter(object : MyGamesAdapter.MyGameClickListener {
            override fun onMyGameClick(game: GameParameters) {
                navigateToGameDetails(game)
            }
        })
        recyclerView = binding.myGamesRecyclerView
        recyclerView.adapter = adapter
        viewModel.myGames.observe(viewLifecycleOwner) { games ->
            adapter.setMyGames(games)
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

    private fun showLoadingAnimation() = binding.myGamesSwipeRefresh.showRefresh()
    private fun hideLoadingAnimation() = binding.myGamesSwipeRefresh.hideRefresh()

    private fun navigateToGameDetails(game: GameParameters) {
        findNavController().navigate(
            GamesFragmentDirections.actionGamesFragmentToMyGameDetailsFragment(
                game.gameId
            )
        )
    }

    private fun showAlertDialog(error: ErrorMessage) {
        val alertDialogFragment =
            AlertDialogFragment.newInstance(title = getString(R.string.oops_title),
                description = getString(error.mapToUI()),
                onRetryClick = {})
        alertDialogFragment.show(parentFragmentManager, TAG)
    }
}