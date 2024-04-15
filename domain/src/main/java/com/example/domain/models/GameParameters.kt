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

fun GameParameters.containsFilteringString(inputString: String): Boolean {
    return (this.gameId.contains(inputString, ignoreCase = true) ||
            this.date.contains(inputString) ||
            this.time.contains(inputString) ||
            this.location.contains(inputString, ignoreCase = true))
}
