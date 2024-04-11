package com.example.snipeagame.ui.introduction.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.usecases.CreateUserUseCase
import com.example.domain.usecases.UserRegisterUseCase
import com.example.domain.utils.ErrorMessage
import com.example.domain.utils.UseCaseResponse
import com.example.domain.utils.ValidationMessage
import com.example.snipeagame.base.BaseViewModel
import com.example.snipeagame.utils.ButtonState
import com.example.snipeagame.utils.CheckboxState
import com.example.snipeagame.utils.TextFieldStatus
import com.example.snipeagame.utils.ValidateFields
import com.example.snipeagame.utils.isValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private var validateFields: ValidateFields,
    private val registerUseCase: UserRegisterUseCase,
    private val createUserUseCase: CreateUserUseCase
) : BaseViewModel() {
    private val _firstNameState = MutableLiveData<TextFieldStatus>()
    val firstNameState: LiveData<TextFieldStatus> = _firstNameState

    private val _lastNameState = MutableLiveData<TextFieldStatus>()
    val lastNameState: LiveData<TextFieldStatus> = _lastNameState

    private val _emailState = MutableLiveData<TextFieldStatus>()
    val emailState: LiveData<TextFieldStatus> = _emailState

    private val _phoneNumberState = MutableLiveData<TextFieldStatus>()
    val phoneNumberState: LiveData<TextFieldStatus> = _phoneNumberState

    private val _passwordState = MutableLiveData<TextFieldStatus>()
    val passwordState: LiveData<TextFieldStatus> = _passwordState

    private val _confirmPasswordState = MutableLiveData<TextFieldStatus>()
    val confirmPasswordState: LiveData<TextFieldStatus> = _confirmPasswordState

    private val _checkedPrivacyAndPolicyState = MutableLiveData<CheckboxState>()
    val checkedPrivacyAndPolicyState: LiveData<CheckboxState> get() = _checkedPrivacyAndPolicyState

    val registerButtonState = MediatorLiveData<ButtonState>()

    private val TAG: String = this::class.java.simpleName

    init {
        val errorSources = listOf(
            firstNameState,
            lastNameState,
            emailState,
            phoneNumberState,
            passwordState,
            confirmPasswordState,
            checkedPrivacyAndPolicyState
        )
        errorSources.forEach { errorLiveData ->
            registerButtonState.addSource(errorLiveData) {
                isRegisterButtonActive()
            }
        }
    }

    fun onRegisterButtonClick(email: String, password: String) {
        viewModelScope.launch {
            registerUser(email, password)
        }
    }

    fun onValidateFirstNameEvent(firstName: String) {
        val result = validateFields.validateFirstName(firstName)
        setFirstNameState(firstName, result)
    }

    fun onValidateLastNameEvent(lastName: String) {
        val result = validateFields.validateLastName(lastName)
        setLastNameState(lastName, result)
    }

    fun onValidateEmailEvent(email: String) {
        val result = validateFields.validateEmail(email)
        setEmailState(email, result)
    }

    fun onValidatePhoneEvent(phoneNumber: String) {
        val result = validateFields.validatePhoneNumber(phoneNumber)
        setPhoneNumberState(phoneNumber, result)
    }

    fun onValidatePasswordEvent(password: String) {
        val result = validateFields.validatePassword(password)
        setPasswordState(password, result)
    }

    fun onValidateConfirmPasswordEvent(confirmPassword: String, password: String) {
        val result = validateFields.validateConfirmPassword(confirmPassword, password)
        setConfirmPasswordState(confirmPassword, result)
    }

    fun onPrivacyAndPolicyCheckEvent(isChecked: Boolean) {
        setPrivacyAndPolicyState(isChecked)
    }

    private fun setFirstNameState(firstName: String, result: ValidationMessage) {
        _firstNameState.value = TextFieldStatus(
            text = firstName,
            error = result,
            shouldShowErrorIcon = showErrorIcon(firstName, result)
        )
    }

    private fun setLastNameState(lastName: String, result: ValidationMessage) {
        _lastNameState.value = TextFieldStatus(
            text = lastName,
            error = result,
            shouldShowErrorIcon = showErrorIcon(lastName, result)
        )
    }

    private fun setEmailState(email: String, result: ValidationMessage) {
        _emailState.value = TextFieldStatus(
            text = email,
            error = result,
            shouldShowErrorIcon = showErrorIcon(email, result)
        )
    }

    private fun setPhoneNumberState(phoneNumber: String, result: ValidationMessage) {
        _phoneNumberState.value = TextFieldStatus(
            text = phoneNumber,
            error = result,
            shouldShowErrorIcon = showErrorIcon(phoneNumber, result)
        )
    }

    private fun setPasswordState(password: String, result: ValidationMessage) {
        _passwordState.value = TextFieldStatus(
            text = password,
            error = result,
            shouldShowErrorIcon = showErrorIcon(password, result)
        )
    }

    private fun setConfirmPasswordState(confirmPassword: String, result: ValidationMessage) {
        _confirmPasswordState.value = TextFieldStatus(
            text = confirmPassword,
            error = result,
            shouldShowErrorIcon = showErrorIcon(confirmPassword, result)
        )
    }

    private fun setPrivacyAndPolicyState(privacyAndPolicy: Boolean) {
        _checkedPrivacyAndPolicyState.value = CheckboxState(privacyAndPolicy)
    }

    private fun showErrorIcon(text: String, result: ValidationMessage) =
        (result != ValidationMessage.ERROR_NOT_FOUND && text.isNotEmpty())

    private fun isRegisterButtonActive() {
        registerButtonState.value = if (
            firstNameState.value?.isValid() == true &&
            lastNameState.value?.isValid() == true &&
            emailState.value?.isValid() == true &&
            phoneNumberState.value?.isValid() == true &&
            passwordState.value?.isValid() == true &&
            confirmPasswordState.value?.isValid() == true &&
            checkedPrivacyAndPolicyState.value?.isChecked == true
        ) ButtonState.IsEnabled
        else ButtonState.NotEnabled
    }

    private suspend fun registerUser(email: String, password: String) {
        showLoading()
        when (val result = registerUseCase(email, password)) {
            is UseCaseResponse.Success -> updateUser(result.body)
            is UseCaseResponse.Failure -> showErrorMessage(
                errorMessage = result.error,
                logMessage = "User register failed with error: $result.error"
            )
        }
    }

    private suspend fun updateUser(userId: String) {
        if (isDataNotNull()) {
            val updateUserResult = createUserUseCase(
                userId = userId,
                firstName = _firstNameState.value!!.text,
                lastName = _lastNameState.value!!.text,
                email = _emailState.value!!.text,
                phone = _phoneNumberState.value!!.text
            )
            showUpdateResponse(updateUserResult)
        } else {
            showErrorMessage(
                errorMessage = ErrorMessage.INVALID_USER,
                logMessage = "Update user failed: invalid fields"
            )
        }
    }

    private fun isDataNotNull(): Boolean {
        return _firstNameState.value != null &&
                _lastNameState.value != null &&
                _emailState.value != null &&
                _phoneNumberState.value != null
    }

    private fun showUpdateResponse(response: UseCaseResponse<String>) {
        when (response) {
            is UseCaseResponse.Success -> onSuccess(response)
            is UseCaseResponse.Failure -> showErrorMessage(
                errorMessage = response.error,
                logMessage = "Update user failed with exception: ${response.error}"
            )
        }
    }

    private fun showErrorMessage(errorMessage: ErrorMessage, logMessage: String) {
        hideLoading()
        showError(errorMessage)
        Log.v(TAG, logMessage)
    }

    private fun onSuccess(response: UseCaseResponse<String>) {
        hideLoading()
        showSuccess(response)
    }
}