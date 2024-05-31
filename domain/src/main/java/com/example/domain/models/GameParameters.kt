package com.example.domain.models

import com.example.domain.utils.formatDate

data class GameParameters(
    val gameId: String,
    val date: String,
    val time: String,
    val location: String,
    val numberOfPlayers: String,
    var currentPlayers: Int
) : Comparable<GameParameters> {
    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        0
    )

    override fun compareTo(other: GameParameters) =
        compareValuesBy(this, other) { formatDate(it.date) }
}

fun GameParameters.containsFilteringString(inputString: String): Boolean {
    return (this.gameId.contains(inputString, ignoreCase = true) ||
            this.date.contains(inputString) ||
            this.time.contains(inputString) ||
            this.location.contains(inputString, ignoreCase = true))
}



