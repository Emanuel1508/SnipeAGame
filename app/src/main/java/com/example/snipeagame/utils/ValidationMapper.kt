package com.example.snipeagame.utils

import com.example.domain.utils.ValidationMessage
import com.example.snipeagame.R

fun ValidationMessage.mapToUI(): Int {
    return when (this) {
        ValidationMessage.PASSWORD_EMPTY -> R.string.empty_password
        ValidationMessage.EMAIL_INVALID -> R.string.invalid_email
        ValidationMessage.FIRST_NAME_INVALID -> R.string.invalid_first_name
        ValidationMessage.LAST_NAME_INVALID -> R.string.invalid_last_name
        ValidationMessage.PASSWORDS_UNMATCHED -> R.string.not_matching_passwords
        ValidationMessage.PHONE_INVALID -> R.string.invalid_phone
        ValidationMessage.PASSWORD_INVALID -> R.string.invalid_password
        else -> R.string.no_error
    }
}