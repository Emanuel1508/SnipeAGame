package com.example.snipeagame.ui.main.games.all_games

import android.os.Bundle
import android.view.View
import com.example.snipeagame.base.BaseFragment
import com.example.snipeagame.databinding.FragmentAllGamesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllGamesFragment: BaseFragment<FragmentAllGamesBinding>(FragmentAllGamesBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}