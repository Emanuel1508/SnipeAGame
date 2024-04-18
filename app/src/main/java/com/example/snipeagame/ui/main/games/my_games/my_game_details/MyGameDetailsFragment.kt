package com.example.snipeagame.ui.main.games.my_games.my_game_details

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.utils.ErrorMessage
import com.example.snipeagame.R
import com.example.snipeagame.base.BaseFragment
import com.example.snipeagame.databinding.FragmentMyGameDetailsBinding
import com.example.snipeagame.ui.main.games.my_games.my_game_details.MyGameDetailsViewModel.UiState
import com.example.snipeagame.utils.AlertDialogFragment
import com.example.snipeagame.utils.hide
import com.example.snipeagame.utils.mapToUI
import com.example.snipeagame.utils.show
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
        setupListeners()
        setupObservers()
        setupToolbar()
        setupLoading()
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
        with(binding) {
            with(viewModel) {
                leaveGameButton.setOnClickListener {
                    onLeaveGame()
                }
                myGameDetailsSwipeRefresh.setOnRefreshListener {
                    onRefresh()
                }
                inviteFriendButton.setOnClickListener {
                    inviteFriend()
                }
            }
        }
    }

    private fun setupObservers() {
        with(viewModel) {
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
                    is UiState.GameInProgress -> {
                        playerList.observe(
                            viewLifecycleOwner
                        ) { players ->
                            adapter.setPlayers(players)
                        }
                    }

                    is UiState.GameCompleted -> {
                        with(binding) {
                            gameCompleteTextView.show()
                            leaveGameButton.hide()
                            gameDetailHeaderTextView.hide()
                            inviteFriendButton.hide()
                        }
                    }
                }
            }
        }
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = binding.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
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

    private fun setupLoading() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) { isLoading ->
            binding.myGameDetailsSwipeRefresh.isRefreshing = isLoading.shouldShowLoading
        }
    }

    private fun inviteFriend() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "${getString(R.string.invite_string)}$gameId"
        )
        context?.startActivity(Intent.createChooser(intent, getString(R.string.invite_through)))
    }

    private fun showAlertDialog(error: ErrorMessage) {
        val alertDialogFragment =
            AlertDialogFragment.newInstance(title = getString(R.string.oops_title),
                description = getString(error.mapToUI()),
                onRetryClick = {})
        alertDialogFragment.show(parentFragmentManager, TAG)
    }
}