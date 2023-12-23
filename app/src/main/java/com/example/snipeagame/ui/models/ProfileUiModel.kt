package com.example.snipeagame.ui.models

data class ProfileUiModel(
    val name: String?,
    val email: String?,
    val phone: String?,
    val faction: String?,
    val roles: ArrayList<String>?,
    val takedowns: Int,
    val games: Int
)

