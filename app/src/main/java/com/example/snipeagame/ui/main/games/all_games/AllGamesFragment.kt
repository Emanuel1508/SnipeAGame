package com.example.snipeagame.ui.main.games.all_games

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.GameParameters
import com.example.domain.utils.ErrorMessage
import com.example.snipeagame.R
import com.example.snipeagame.base.BaseFragment
import com.example.snipeagame.databinding.FragmentAllGamesBinding
import com.example.snipeagame.ui.main.games.GamesFragmentDirections
import com.example.snipeagame.ui.main.games.all_games.AllGamesViewModel.GameListState
import com.example.snipeagame.utils.AlertDialogFragment
import com.example.snipeagame.utils.hide
import com.example.snipeagame.utils.hideKeyboard
import com.example.snipeagame.utils.mapToUI
import com.example.snipeagame.utils.show
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
        setupListener()
        setupLoading()
        setupObservers()
    }

    private fun setupAdapter() {
        adapter = AllGamesAdapter(object : AllGamesAdapter.GameClickListener {
            override fun onGameClick(game: GameParameters) {
                viewModel.onJoinGameButtonPress(game)
            }
        })
        recyclerView = binding.allGamesRecyclerView
        recyclerView.adapter = adapter
        viewModel.games.observe(viewLifecycleOwner) { games ->
            adapter.setGames(games)
        }
    }

    private fun setupListener() {
        with(binding) {
            allGamesSearchBar.setOnKeyListener { _, id, _ ->
                if (isDonePressed(id))
                    hideKeyboard()
                true
            }
            allGamesConstraintLayout.setOnClickListener {
                hideKeyboard()
            }
            allGamesSwipeRefresh.setOnRefreshListener {
                viewModel.onRefresh()
            }
            allGamesSearchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null) {
                        setupFilter(newText)
                    }
                    return true
                }
            })
        }
    }

    private fun setupLoading() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) { isLoading ->
            binding.allGamesSwipeRefresh.isRefreshing = isLoading.shouldShowLoading
        }
    }

    private fun setupObservers() {
        with(viewModel) {
            errorLiveData.observe(viewLifecycleOwner) { error ->
                showAlertDialog(error.message)
            }
            gameListStatus.observe(viewLifecycleOwner) { status ->
                when (status) {
                    is GameListState.IsPopulated -> binding.noGamesTextView.hide()
                    is GameListState.NotPopulated -> binding.noGamesTextView.show()
                }
            }
        }
    }

    private fun setupFilter(filteringString: String) {
        with(viewModel) {
            onFilterGames(filteringString)
            games.observe(viewLifecycleOwner) { filteredGames ->
                filteredGames.let {
                    adapter.updateGames()
                }
            }
        }
    }

    private fun navigateToCreateGameFragment() {
        findNavController().navigate(GamesFragmentDirections.actionGamesFragmentToCreateGameFragment())
    }

    private fun onCreateGameButtonClick() {
        binding.createGameButton.setOnClickListener {
            navigateToCreateGameFragment()
        }
    }


    private fun showAlertDialog(error: ErrorMessage) {
        val alertDialogFragment =
            AlertDialogFragment.newInstance(title = getString(R.string.oops_title),
                description = getString(error.mapToUI()),
                onRetryClick = {})
        alertDialogFragment.show(parentFragmentManager, TAG)
    }

    private fun isDonePressed(id: Int) = id == EditorInfo.IME_ACTION_DONE
}