package com.example.domain.models

data class GameParameters(
    val gameId: String,
    val date: String,
    val time: String,
    val location: String,
    val numberOfPlayers: String,
    var currentPlayers: Int
) {
    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        0
    )
}
