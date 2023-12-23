package com.example.snipeagame.utils

sealed class ButtonState {
    data object IsEnabled: ButtonState()
    data object NotEnabled: ButtonState()
}
