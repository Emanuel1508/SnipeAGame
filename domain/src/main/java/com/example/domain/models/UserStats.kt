package com.example.domain.models

data class UserStats(
    val takedowns: Int,
    val games: Int
) {
    constructor() : this(
        0,
        0
    )
}
