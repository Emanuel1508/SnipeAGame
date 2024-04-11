package com.example.snipeagame.ui.introduction.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.usecases.ForgotPasswordUseCase
import com.example.domain.usecases.GetFactionUseCase
import com.example.domain.usecases.GetUserIdUseCase
import com.example.domain.usecases.UserLoginUseCase
import com.example.domain.utils.ErrorMessage
import com.example.domain.utils.UseCaseResponse
import com.example.snipeagame.base.BaseViewModel
import com.example.snipeagame.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private var userLoginUseCase: UserLoginUseCase,
    private var getFactionUseCase: GetFactionUseCase,
    private var getUserIdUseCase: GetUserIdUseCase,
    private var forgotPasswordUseCase: ForgotPasswordUseCase
) : BaseViewModel() {
    private val _navigation = SingleLiveEvent<NavDestination>()
    val navigation: LiveData<NavDestination> get() = _navigation

    private val TAG: String = this::class.java.simpleName

    fun onLoginButtonClick(email: String, password: String) {
        viewModelScope.launch {
            loginUser(
                email = email,
                password = password
            )
        }
    }

    private suspend fun loginUser(email: String, password: String) {
        showLoading()
        when (val result = userLoginUseCase(
            email = email,
            password = password
        )
        ) {
            is UseCaseResponse.Success -> getId()
            is UseCaseResponse.Failure -> showErrorMessage(
                errorMessage = result.error,
                logMessage = "User login failed with error: $result.error"
            )
        }
    }

    private fun getId() {
        viewModelScope.launch {
            when (val response = getUserIdUseCase()) {
                is UseCaseResponse.Success -> getFaction(response.body)
                is UseCaseResponse.Failure -> showErrorMessage(
                    errorMessage = response.error,
                    logMessage = "User fetch id failed with error: $response.error"
                )
            }
        }
    }

    private suspend fun getFaction(userId: String) {
        when (val factionResponse = getFactionUseCase(userId)) {
            is UseCaseResponse.Success -> _navigation.value =
                if (factionResponse.body.isNullOrEmpty())
                    NavDestination.FactionScreen
                else
                    NavDestination.MainScreen

            is UseCaseResponse.Failure -> showErrorMessage(
                errorMessage = factionResponse.error,
                logMessage = "User get faction failed with error: ${factionResponse.error}"
            )
        }
        hideLoading()
    }

    suspend fun onForgotPasswordSubmit(email: String) {
        showLoading()
        when (val result = forgotPasswordUseCase(email)) {
            is UseCaseResponse.Success -> hideLoading()
            is UseCaseResponse.Failure -> showError(result.error)
        }
    }

    private fun showErrorMessage(errorMessage: ErrorMessage, logMessage: String) {
        hideLoading()
        showError(errorMessage)
        Log.v(TAG, logMessage)
    }

    sealed class NavDestination {
        data object FactionScreen : NavDestination()
        data object MainScreen : NavDestination()
    }
}