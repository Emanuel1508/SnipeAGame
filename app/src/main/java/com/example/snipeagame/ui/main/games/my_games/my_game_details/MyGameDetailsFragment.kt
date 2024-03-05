package com.example.snipeagame.ui.main.games.my_games.my_game_details

import android.os.Bundle
import android.view.View
import com.example.snipeagame.base.BaseFragment
import com.example.snipeagame.databinding.FragmentMyGameDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyGameDetailsFragment :
    BaseFragment<FragmentMyGameDetailsBinding>(FragmentMyGameDetailsBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}