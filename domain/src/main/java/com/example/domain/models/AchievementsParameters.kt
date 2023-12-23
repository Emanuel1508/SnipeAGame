package com.example.domain.models

import com.example.domain.utils.NumberConstants

data class AchievementsParameters(
    val title: String,
    val description: String,
    var imageResource: Int,
    val condition: Int,
    var isButtonVisible: Boolean
) {
    constructor() : this(
        "",
        "",
        NumberConstants.ZERO,
        NumberConstants.LARGE_CONDITION,
        false
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        other as AchievementsParameters
        return title == other.title
    }

    override fun hashCode(): Int {
        return title.hashCode()
    }
}

