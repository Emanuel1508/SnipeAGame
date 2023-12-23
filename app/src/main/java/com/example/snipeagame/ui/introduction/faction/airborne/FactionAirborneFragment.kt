package com.example.snipeagame.ui.introduction.faction.airborne

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.snipeagame.base.BaseFragment
import com.example.snipeagame.databinding.FragmentFactionAirborneBinding
import com.example.snipeagame.ui.introduction.faction.FactionFragmentDirections
import com.example.snipeagame.utils.FactionNavDestination
import com.example.snipeagame.utils.StringConstants

class FactionAirborneFragment :
    BaseFragment<FragmentFactionAirborneBinding>(FragmentFactionAirborneBinding::inflate) {
    private val viewModel: FactionAirborneViewModel by viewModels()
    private val factionName = StringConstants.AIRBORNE

    private val TAG = this::class.java.simpleName
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.selectFactionButton.setOnClickListener {
            viewModel.onSelectButtonClick()
        }
    }

    private fun setupObservers() {
        viewModel.navigation.observe(viewLifecycleOwner) { destination ->
            when (destination) {
                is FactionNavDestination.RolesScreen -> navigateToRolesScreen()
            }
        }
    }

    private fun navigateToRolesScreen() {
        findNavController().navigate(
            FactionFragmentDirections
                .actionFactionFragmentToRolesFragment(factionName)
        )
    }
}