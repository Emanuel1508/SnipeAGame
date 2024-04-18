package com.example.domain.models

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

    override fun compareTo(other: GameParameters): Int {
        return compareValuesBy(this, other, { it.formatDate() }, { it.time })
    }
}

fun GameParameters.containsFilteringString(inputString: String): Boolean {
    return (this.gameId.contains(inputString, ignoreCase = true) ||
            this.date.contains(inputString) ||
            this.time.contains(inputString) ||
            this.location.contains(inputString, ignoreCase = true))
}

fun GameParameters.formatDate(): Date? {
    val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return dateFormatter.parse(date)
}


