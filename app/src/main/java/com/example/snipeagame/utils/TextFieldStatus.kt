package com.example.snipeagame.utils

import com.example.domain.utils.ValidationMessage
import kotlin.Unit.toString

data class TextFieldStatus(
    var text: String = toString(),
    var error: ValidationMessage = ValidationMessage.NOT_CHECKED,
    var shouldShowErrorIcon: Boolean = false
)

fun TextFieldStatus.isValid() = this.error == ValidationMessage.ERROR_NOT_FOUND
