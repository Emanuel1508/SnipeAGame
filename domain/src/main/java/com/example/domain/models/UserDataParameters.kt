package com.example.domain.models

data class UserDataParameters(
    val userId: String,
    val name: String?,
    val email: String?,
    val phone: String?,
    val faction: String?,
    val roles: ArrayList<String>?,
    val takedowns: Int,
    val games: Int
) {
    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        ArrayList<String>(),
        0,
        0,
    )
}
