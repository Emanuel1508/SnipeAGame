package com.example.snipeagame.ui.main.games.my_games

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.GameParameters
import com.example.domain.utils.ErrorMessage
import com.example.snipeagame.R
import com.example.snipeagame.base.BaseFragment
import com.example.snipeagame.databinding.FragmentMyGamesBinding
import com.example.snipeagame.ui.main.games.GamesFragmentDirections
import com.example.snipeagame.utils.AlertDialogFragment
import com.example.snipeagame.utils.GameCompleteDialogFragment
import com.example.snipeagame.utils.hideRefresh
import com.example.snipeagame.utils.mapToUI
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
        setupLoading()
        setupListener()
    }

    private fun setupAdapter() {
        adapter = MyGamesAdapter(object : MyGamesAdapter.MyGameClickListener {
            override fun onMyGameClick(game: GameParameters) {
                navigateToGameDetails(game)
            }

            override fun onFinishGameClick(game: GameParameters) {
                showGameCompleteDialog(game)
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
            errorLiveData.observe(viewLifecycleOwner) { error ->
                showAlertDialog(error.message)
            }
        }
    }

    private fun setupLoading() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) { isLoading ->
            binding.myGamesSwipeRefresh.isRefreshing = isLoading.shouldShowLoading
        }
    }

    private fun setupListener() {
        with(binding) {
            myGamesSwipeRefresh.setOnRefreshListener {
                viewModel.onRefresh()
                myGamesSwipeRefresh.hideRefresh()
            }
        }
    }


    private fun navigateToGameDetails(game: GameParameters) {
        var isGameCompleted: Boolean

        with(viewModel) {
            val gameDate = formatDateAndTime(game)
            isGameCompleted = checkGamePastDue(gameDate)
        }
        findNavController().navigate(
            GamesFragmentDirections.actionGamesFragmentToMyGameDetailsFragment(
                game.gameId,
                isGameCompleted
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

    private fun showGameCompleteDialog(game: GameParameters) {
        val gameCompleteDialogFragment =
            GameCompleteDialogFragment.newInstance(
                onSubmitClick = { inputText ->
                    viewModel.onGameFinish(inputText, game)
                    adapter.finishGame(game)
                },
                playerCount = game.currentPlayers
            )
        gameCompleteDialogFragment.show(parentFragmentManager, TAG)
    }
}