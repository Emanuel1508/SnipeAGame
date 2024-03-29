package com.example.snipeagame.ui.main.games.my_games.my_game_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.Result
import com.example.domain.utils.ErrorMessage
import com.example.snipeagame.R
import com.example.snipeagame.base.BaseFragment
import com.example.snipeagame.databinding.FragmentMyGameDetailsBinding
import com.example.snipeagame.utils.AlertDialogFragment
import com.example.snipeagame.utils.hide
import com.example.snipeagame.utils.hideRefresh
import com.example.snipeagame.utils.mapToUI
import com.example.snipeagame.utils.show
import com.example.snipeagame.utils.showRefresh
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyGameDetailsFragment :
    BaseFragment<FragmentMyGameDetailsBinding>(FragmentMyGameDetailsBinding::inflate) {
    private val viewModel: MyGameDetailsViewModel by viewModels()
    private lateinit var adapter: MyGameDetailsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var gameId: String
    private var isGameCompleted: Boolean = false
    private val TAG = this::class.java.simpleName

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getGameId()
        checkIsGameCompleted()
        passGameId(gameId)
        setUI()
        setupAdapter()
        setupObservers()
        setupListeners()
    }

    private fun setUI() {
        viewModel.updateMyGamesUI(isGameCompleted)
    }

    private fun setupAdapter() {
        adapter = MyGameDetailsAdapter()
        recyclerView = binding.gameDetailRecyclerView
        recyclerView.adapter = adapter
    }

    private fun getGameId() {
        val args: MyGameDetailsFragmentArgs by navArgs()
        gameId = args.gameId
    }

    private fun checkIsGameCompleted() {
        val args: MyGameDetailsFragmentArgs by navArgs()
        isGameCompleted = args.gameComplete
    }

    private fun setupListeners() {
        binding.leaveGameButton.setOnClickListener {
            viewModel.leaveGame()
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
            navigation.observe(viewLifecycleOwner) { destination ->
                when (destination) {
                    is MyGameDetailsViewModel.LeaveGameRedirect.GameFragment -> findNavController()
                        .navigate(
                            MyGameDetailsFragmentDirections.actionMyGameDetailsFragmentToGamesFragment()
                        )
                }
            }
            gameState.observe(viewLifecycleOwner) { gameStatus ->
                when (gameStatus) {
                    is MyGameDetailsViewModel.UiState.GameInProgress -> {
                        playerList.observe(
                            viewLifecycleOwner
                        ) { players ->
                            adapter.setPlayers(players)
                        }
                    }

                    is MyGameDetailsViewModel.UiState.GameCompleted -> {
                        with(binding) {
                            gameCompleteTextView.show()
                            leaveGameButton.hide()
                            gameDetailHeaderTextView.hide()
                        }
                    }
                }
            }
        }
    }

    private fun passGameId(gameId: String) {
        with(viewModel) {
            setGameId(gameId)
            lifecycleScope.launch(Dispatchers.IO) {
                getPlayers()
            }
        }
    }

    private fun updateRefreshAnimation(value: Result.Loading) {
        when (value.shouldShowLoading) {
            true -> showLoadingAnimation()
            false -> hideLoadingAnimation()
        }
    }

    private fun showLoadingAnimation() = binding.myGameDetailsSwipeRefresh.showRefresh()
    private fun hideLoadingAnimation() = binding.myGameDetailsSwipeRefresh.hideRefresh()

    private fun showAlertDialog(error: ErrorMessage) {
        val alertDialogFragment =
            AlertDialogFragment.newInstance(title = getString(R.string.oops_title),
                description = getString(error.mapToUI()),
                onRetryClick = {})
        alertDialogFragment.show(parentFragmentManager, TAG)
    }
}