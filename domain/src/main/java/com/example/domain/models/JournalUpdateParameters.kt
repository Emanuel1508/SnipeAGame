package com.example.domain.models

data class JournalUpdateParameters(
    val userId: String,
    val journalId: String,
    var rating: String,
    val journalText: String
) {
    constructor() : this(
        "",
        "",
        "",
        ""
    )
}
