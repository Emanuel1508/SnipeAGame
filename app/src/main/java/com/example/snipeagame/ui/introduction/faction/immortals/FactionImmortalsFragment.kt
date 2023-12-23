package com.example.snipeagame.ui.introduction.faction.immortals

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.snipeagame.base.BaseFragment
import com.example.snipeagame.databinding.FragmentFactionImmortalsBinding
import com.example.snipeagame.ui.introduction.faction.FactionFragmentDirections
import com.example.snipeagame.utils.FactionNavDestination
import com.example.snipeagame.utils.StringConstants

class FactionImmortalsFragment :
    BaseFragment<FragmentFactionImmortalsBinding>(FragmentFactionImmortalsBinding::inflate) {
    private val viewModel: FactionImmortalsViewModel by viewModels()
    private val factionName = StringConstants.IMMORTALS
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
