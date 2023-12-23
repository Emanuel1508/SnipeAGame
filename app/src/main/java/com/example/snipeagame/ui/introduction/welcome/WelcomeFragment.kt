package com.example.snipeagame.ui.introduction.welcome

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.snipeagame.base.BaseFragment
import com.example.snipeagame.databinding.FragmentWelcomeBinding
import com.example.snipeagame.ui.introduction.welcome.WelcomeViewModel.NavDestination
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>(FragmentWelcomeBinding::inflate) {
    private val viewModel: WelcomeViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupListener()
    }

    private fun setupObserver() {
        viewModel.navigation.observe(viewLifecycleOwner) { destination ->
            when (destination) {
                is NavDestination.Register -> navigateToRegister()
                is NavDestination.Login -> navigateToLogin()
            }
        }
    }

    private fun setupListener() {
        with(binding) {
            with(viewModel) {
                welcomeRegisterButton.setOnClickListener {
                    onRegisterButtonPress()
                }
                welcomeLoginButton.setOnClickListener {
                    onLoginButtonPress()
                }
            }
        }
    }

    private fun navigateToRegister() {
        findNavController().navigate(
            WelcomeFragmentDirections
                .actionWelcomeFragmentToRegisterFragment()
        )
    }

    private fun navigateToLogin() {
        findNavController().navigate(
            WelcomeFragmentDirections
                .actionWelcomeFragmentToLoginFragment()
        )
    }
}