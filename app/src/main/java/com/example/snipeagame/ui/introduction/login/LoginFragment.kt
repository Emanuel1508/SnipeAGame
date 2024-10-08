package com.example.snipeagame.ui.introduction.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.domain.models.Result
import com.example.domain.utils.ErrorMessage
import com.example.snipeagame.R
import com.example.snipeagame.base.BaseFragment
import com.example.snipeagame.databinding.FragmentLoginBinding
import com.example.snipeagame.ui.introduction.login.LoginViewModel.NavDestination
import com.example.snipeagame.ui.main.MainActivity
import com.example.snipeagame.utils.AlertDialogFragment
import com.example.snipeagame.utils.ForgotPasswordDialogFragment
import com.example.snipeagame.utils.hideRefresh
import com.example.snipeagame.utils.mapToUI
import com.example.snipeagame.utils.showRefresh
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    private val viewModel: LoginViewModel by viewModels()
    private val TAG = this::class.java.simpleName

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideLoadingAnimation()
        setupObservers()
        setupListeners()
        setupToolbar()
    }

    private fun setupObservers() {
        with(viewModel) {
            errorLiveData.observe(viewLifecycleOwner) { error ->
                showDialog(error.message)
            }
            navigation.observe(viewLifecycleOwner) { navDestination ->
                when (navDestination) {
                    is NavDestination.FactionScreen -> navigateToFactionScreen()
                    is NavDestination.MainScreen -> navigateToMainActivity()
                }
            }
            loadingLiveData.observe(viewLifecycleOwner) {
                updateRefreshAnimation(it)
            }
        }
    }

    private fun setupListeners() {
        with(binding) {
            loginButton.setOnClickListener {
                viewModel.onLoginButtonClick(
                    emailInputText.text.toString(),
                    passwordInputText.text.toString()
                )
            }
            forgotPasswordTextView.setOnClickListener {
                showForgotPasswordDialog()
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

    private fun updateRefreshAnimation(value: Result.Loading) {
        when (value.shouldShowLoading) {
            true -> showLoadingAnimation()
            false -> hideLoadingAnimation()
        }
    }

    private fun showLoadingAnimation() = binding.loginSwipeRefresh.showRefresh()
    private fun hideLoadingAnimation() = binding.loginSwipeRefresh.hideRefresh()

    private fun showDialog(error: ErrorMessage) {
        val alertDialogFragment = AlertDialogFragment.newInstance(
            title = getString(R.string.sign_in_alert_title),
            description = getString(error.mapToUI()),
            onRetryClick = {
                with(binding) {
                    viewModel.onLoginButtonClick(
                        emailInputText.text.toString(),
                        passwordInputText.text.toString()
                    )
                }
            }
        )
        alertDialogFragment.show(parentFragmentManager, TAG)
    }

    private fun showForgotPasswordDialog() {
        val forgotPasswordDialog = ForgotPasswordDialogFragment.newInstance(
            onSubmitClick = { text ->
                with(viewModel) {
                    viewModelScope.launch {
                        onForgotPasswordSubmit(text)
                    }
                }
            }
        )
        forgotPasswordDialog.show(parentFragmentManager, TAG)
    }

    private fun navigateToFactionScreen() =
        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToFactionFragment())

    private fun navigateToMainActivity() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}


