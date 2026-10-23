package com.boyidapu.homestock.ui.home

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalTime

class GreetingUtilsTest {

    @Test
    fun getGreetingPrefix_morningHours_returnsGoodMorning() {
        assertEquals("Good morning", GreetingUtils.getGreetingPrefix(LocalTime.of(5, 0)))
        assertEquals("Good morning", GreetingUtils.getGreetingPrefix(LocalTime.of(8, 30)))
        assertEquals("Good morning", GreetingUtils.getGreetingPrefix(LocalTime.of(11, 59)))
    }

    @Test
    fun getGreetingPrefix_afternoonHours_returnsGoodAfternoon() {
        assertEquals("Good afternoon", GreetingUtils.getGreetingPrefix(LocalTime.of(12, 0)))
        assertEquals("Good afternoon", GreetingUtils.getGreetingPrefix(LocalTime.of(14, 15)))
        assertEquals("Good afternoon", GreetingUtils.getGreetingPrefix(LocalTime.of(16, 59)))
    }

    @Test
    fun getGreetingPrefix_eveningAndNightHours_returnsGoodEvening() {
        assertEquals("Good evening", GreetingUtils.getGreetingPrefix(LocalTime.of(17, 0)))
        assertEquals("Good evening", GreetingUtils.getGreetingPrefix(LocalTime.of(21, 30)))
        assertEquals("Good evening", GreetingUtils.getGreetingPrefix(LocalTime.of(4, 59)))
    }

    @Test
    fun formatGreeting_withValidName_includesName() {
        val time = LocalTime.of(9, 0)
        assertEquals("Good morning, Priya", GreetingUtils.formatGreeting("Priya", time))
        assertEquals("Good morning, Priya", GreetingUtils.formatGreeting("  Priya  ", time))
    }

    @Test
    fun formatGreeting_withEmptyName_returnsOnlyPrefix() {
        val time = LocalTime.of(18, 0)
        assertEquals("Good evening", GreetingUtils.formatGreeting("", time))
        assertEquals("Good evening", GreetingUtils.formatGreeting("   ", time))
    }
}
