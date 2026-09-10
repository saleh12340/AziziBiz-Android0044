package com.mruraza.ims.Utils.Objects

import android.content.Context
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

object DateHelper {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun todayDate(): String {
        val today = LocalDate.now().format(formatter)
        return today.toString()
    }
    fun dateToString(date: LocalDate): String {
        return date.format(formatter).toString()
    }
    fun stringToDate(dateString: String): LocalDate? {
        return dateString.let { LocalDate.parse(it, formatter) }
    }
    fun randomDateWithin12Years(): String {
        val today = LocalDate.now()
        val pastDate = today.minusYears(12)

        // total days between pastDate and today
        val daysBetween = today.toEpochDay() - pastDate.toEpochDay()

        // pick a random offset
        val randomDays = Random.nextLong(daysBetween + 1)

        val randomDate = pastDate.plusDays(randomDays)
        return randomDate.format(formatter)
    }
    fun randomDateWithin30Days(): String {
        val today = LocalDate.now()
        val pastDate = today.minusDays(30)

        // total days between pastDate and today
        val daysBetween = today.toEpochDay() - pastDate.toEpochDay()

        // pick a random offset
        val randomDays = Random.nextLong(daysBetween + 1)

        val randomDate = pastDate.plusDays(randomDays)
        return randomDate.format(formatter)
    }
    fun randomDateWithin1Year(): String {
        val today = LocalDate.now()
        val pastDate = today.minusYears(1)

        // total days between pastDate and today
        val daysBetween = today.toEpochDay() - pastDate.toEpochDay()

        // pick a random offset
        val randomDays = Random.nextLong(daysBetween + 1)

        val randomDate = pastDate.plusDays(randomDays)
        return randomDate.format(formatter)
    }
}
