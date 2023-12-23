package com.example.snipeagame.utils

import com.example.domain.utils.ValidationMessage
import javax.inject.Inject

class ValidateFields @Inject constructor() {
    fun validateFirstName(firstName: String): ValidationMessage {
        return if (firstName.matches(RegexConstants.NAME.toRegex())) ValidationMessage.ERROR_NOT_FOUND
        else ValidationMessage.FIRST_NAME_INVALID
    }

    fun validateLastName(lastName: String): ValidationMessage {
        return if (lastName.matches(RegexConstants.NAME.toRegex())) ValidationMessage.ERROR_NOT_FOUND
        else ValidationMessage.LAST_NAME_INVALID
    }

    fun validateEmail(email: String): ValidationMessage {
        return if (email.matches(RegexConstants.EMAIL.toRegex())) ValidationMessage.ERROR_NOT_FOUND
        else ValidationMessage.EMAIL_INVALID
    }

    fun validatePhoneNumber(phoneNumber: String): ValidationMessage {
        return if (phoneNumber.matches(RegexConstants.PHONE.toRegex())) ValidationMessage.ERROR_NOT_FOUND
        else ValidationMessage.PHONE_INVALID
    }

    fun validatePassword(password: String): ValidationMessage {
        return if (password.matches(RegexConstants.PASSWORD.toRegex())) ValidationMessage.ERROR_NOT_FOUND
        else ValidationMessage.PASSWORD_INVALID
    }

    fun validateConfirmPassword(passwordToConfirm: String, password: String): ValidationMessage {
        return if (passwordToConfirm == password) ValidationMessage.ERROR_NOT_FOUND
        else ValidationMessage.PASSWORDS_UNMATCHED
    }
}