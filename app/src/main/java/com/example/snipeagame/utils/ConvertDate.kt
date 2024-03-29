package com.example.snipeagame.utils

import com.example.domain.models.GameParameters
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun convertDate(game: GameParameters): Date {
    val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    val formattedGameDate = dateFormatter.parse(game.date)
    val formattedGameTime = timeFormatter.parse(game.time)
    val calendar = Calendar.getInstance()

    formattedGameDate?.let { date ->
        formattedGameTime?.let { time ->
            calendar.time = date
            calendar.set(Calendar.HOUR_OF_DAY, time.hours)
            calendar.set(Calendar.MINUTE, time.minutes)
        }
    }
    return calendar.time
}
