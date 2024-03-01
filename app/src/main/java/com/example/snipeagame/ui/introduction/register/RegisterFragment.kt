package com.example.snipeagame.ui.introduction.register

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.domain.models.Result
import com.example.domain.utils.ErrorMessage
import com.example.snipeagame.R
import com.example.snipeagame.base.BaseFragment
import com.example.snipeagame.databinding.FragmentRegisterBinding
import com.example.snipeagame.utils.AlertDialogFragment
import com.example.snipeagame.utils.ButtonState
import com.example.snipeagame.utils.disable
import com.example.snipeagame.utils.enable
import com.example.snipeagame.utils.hideRefresh
import com.example.snipeagame.utils.mapToUI
import com.example.snipeagame.utils.setError
import com.example.snipeagame.utils.showRefresh
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment :
    BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {

    private val viewModel: RegisterViewModel by viewModels()
    private val TAG = this::class.java.simpleName
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideLoadingAnimation()
        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        with(binding) {
            with(viewModel) {
                firstNameState.observe(viewLifecycleOwner) { textFieldResult ->
                    fistNameInputLayout.setError(
                        textFieldResult.error,
                        textFieldResult.shouldShowErrorIcon
                    )
                }
                lastNameState.observe(viewLifecycleOwner) { textFieldResult ->
                    lastNameInputLayout.setError(
                        textFieldResult.error,
                        textFieldResult.shouldShowErrorIcon
                    )
                }
                emailState.observe(viewLifecycleOwner) { textFieldResult ->
                    emailInputLayout.setError(
                        textFieldResult.error,
                        textFieldResult.shouldShowErrorIcon
                    )
                }
                phoneNumberState.observe(viewLifecycleOwner) { textFieldResult ->
                    phoneNumberInputLayout.setError(
                        textFieldResult.error,
                        textFieldResult.shouldShowErrorIcon
                    )
                }
                passwordState.observe(viewLifecycleOwner) { textFieldResult ->
                    passwordInputLayout.setError(
                        textFieldResult.error,
                        textFieldResult.shouldShowErrorIcon
                    )
                }
                confirmPasswordState.observe(viewLifecycleOwner) { textFieldResult ->
                    confirmPasswordInputLayout.setError(
                        textFieldResult.error,
                        textFieldResult.shouldShowErrorIcon
                    )
                }
                registerButtonState.observe(viewLifecycleOwner) { buttonState ->
                    when (buttonState) {
                        is ButtonState.IsEnabled -> registerButton.enable()
                        is ButtonState.NotEnabled -> registerButton.disable()
                    }
                }
                errorLiveData.observe(viewLifecycleOwner) { error ->
                    showDialog(error.message)
                }
                successLiveData.observe(viewLifecycleOwner) {
                    navigateToSuccessScreen()
                }
                loadingLiveData.observe(viewLifecycleOwner) {
                    updateRefreshAnimation(it)
                }
            }
        }
    }

    private fun setupListeners() {
        with(binding) {
            with(viewModel) {
                firstNameInputText.doAfterTextChanged {
                    onValidateFirstNameEvent(firstNameInputText.text.toString())
                }
                lastNameInputText.doAfterTextChanged {
                    onValidateLastNameEvent(lastNameInputText.text.toString())
                }
                emailInputText.doAfterTextChanged {
                    onValidateEmailEvent(emailInputText.text.toString())
                }
                phoneNumberInputText.doAfterTextChanged {
                    onValidatePhoneEvent(phoneNumberInputText.text.toString())
                }
                passwordInputText.doAfterTextChanged {
                    onValidatePasswordEvent(passwordInputText.text.toString())
                }
                confirmPasswordInputText.doAfterTextChanged {
                    onValidateConfirmPasswordEvent(
                        confirmPasswordInputText.text.toString(),
                        passwordInputText.text.toString()
                    )
                }
                registerButton.setOnClickListener {
                    onRegisterButtonClick(
                        emailInputText.text.toString(),
                        passwordInputText.text.toString()
                    )
                }
            }
        }
    }

    private fun updateRefreshAnimation(value: Result.Loading) {
        when (value.shouldShowLoading) {
            true -> showLoadingAnimation()
            false -> hideLoadingAnimation()
        }
    }

    private fun showLoadingAnimation() = binding.registerSwipeRefresh.showRefresh()

    private fun hideLoadingAnimation() = binding.registerSwipeRefresh.hideRefresh()

    private fun navigateToSuccessScreen() {
        findNavController().navigate(
            RegisterFragmentDirections
                .actionRegisterFragmentToSuccessFragment()
        )
    }

    private fun showDialog(error: ErrorMessage) {
        val alertDialogFragment = AlertDialogFragment.newInstance(
            title = getString(R.string.sign_up_alert_title),
            description = getString(error.mapToUI()),
            onRetryClick = {
                with(binding) {
                    viewModel.onRegisterButtonClick(
                        emailInputText.text.toString(),
                        passwordInputText.text.toString()
                    )
                }
            }
        )
        alertDialogFragment.show(parentFragmentManager, TAG)
    }
}