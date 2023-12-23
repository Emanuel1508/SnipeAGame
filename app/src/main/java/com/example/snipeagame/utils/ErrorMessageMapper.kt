package com.example.snipeagame.utils

import com.example.domain.utils.ErrorMessage
import com.example.snipeagame.R

fun ErrorMessage.mapToUI(): Int {
    return when (this) {
        ErrorMessage.NO_NETWORK -> R.string.no_internet_error
        ErrorMessage.UPDATE_USER -> R.string.update_user_error
        ErrorMessage.INVALID_EMAIL -> R.string.email_already_used_error
        ErrorMessage.INCORRECT_ACCOUNT -> R.string.incorrect_account_error
        ErrorMessage.GENERAL -> R.string.general_error
        ErrorMessage.INCORRECT_EMAIL -> R.string.incorrect_email
        ErrorMessage.INVALID_USER -> R.string.invalid_user_error
        else -> R.string.no_error
    }
}