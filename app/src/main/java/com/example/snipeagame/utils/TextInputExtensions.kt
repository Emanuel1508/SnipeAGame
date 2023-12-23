package com.example.snipeagame.utils

import com.example.domain.utils.ValidationMessage
import com.example.snipeagame.R
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.showErrorIcon(showIcon: Boolean) {
    if (!showIcon) {
        errorIconDrawable = null
    } else {
        setErrorIconDrawable(R.drawable.ic_error_foreground)
    }
}

fun TextInputLayout.setError(errorMessage: ValidationMessage, showIcon: Boolean) {
    this.error = if (errorMessage == ValidationMessage.ERROR_NOT_FOUND
        || errorMessage == ValidationMessage.NOT_CHECKED
    ) null
    else {
        context.getString(errorMessage.mapToUI())
    }
    this.showErrorIcon(showIcon)
}