package com.example.domain.models

data class JournalParameters(
    val gameId: String,
    val userId: String,
    val location: String,
    val date: String,
    val time: String,
    val numberOfPlayers: String,
    val takedowns: String,
    var rating: String,
    val journalText: String,
) {
    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
    )
}
