package com.example.snipeagame.ui.main.games.all_games

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.snipeagame.base.BaseFragment
import com.example.snipeagame.databinding.FragmentAllGamesBinding
import com.example.snipeagame.ui.main.games.GamesFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllGamesFragment : BaseFragment<FragmentAllGamesBinding>(FragmentAllGamesBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onCreateGameButtonClick()
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
}