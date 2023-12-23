package com.example.domain.models

data class UserGroupUpdateParameters(
    val userId: String,
    val faction: String,
    val roles: ArrayList<String>
)
