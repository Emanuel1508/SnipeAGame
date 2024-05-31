package com.example.domain.models

import com.example.domain.utils.formatDate

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
    val imageUrls: ArrayList<String>
) : Comparable<JournalParameters> {
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
        arrayListOf()
    )

    override fun compareTo(other: JournalParameters) =
        compareValuesBy(this, other) { formatDate(it.date) }
}
