package com.boyidapu.homestock.ui.home

import java.time.LocalTime

object GreetingUtils {

    fun getGreetingPrefix(time: LocalTime = LocalTime.now()): String {
        val hour = time.hour
        return when (hour) {
            in 5..11 -> "Good morning"
            in 12..16 -> "Good afternoon"
            else -> "Good evening"
        }
    }

    fun formatGreeting(userName: String, time: LocalTime = LocalTime.now()): String {
        val prefix = getGreetingPrefix(time)
        val name = userName.trim()
        return if (name.isNotEmpty()) "$prefix, $name" else prefix
    }
}
