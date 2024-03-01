package com.example.domain.models

data class UserGameDataParameters(
    val name: String,
    val phone: String,
    val faction: String,
    val roles: ArrayList<String>,
) {
    constructor() : this(
        "",
        "",
        "",
        ArrayList<String>()
    )
}
