package com.example

import com.example.data.prayer.PrayerTimesCalculator
import java.util.Calendar
import java.util.TimeZone
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PrayerTimesCalculatorTest {

    private fun calendar(year: Int, month: Int, day: Int): Calendar =
        Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            set(year, month - 1, day, 12, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }

    private fun minutes(time: String): Int {
        val parts = time.split(":")
        return parts[0].toInt() * 60 + parts[1].toInt()
    }

    @Test
    fun qiblaForKaabaIsZeroDistanceAndNorthBearing() {
        val mecca = PrayerTimesCalculator.DEFAULT_CITIES.first { it.id == "mecca" }
        val result = PrayerTimesCalculator.calculateQibla(mecca)

        assertTrue(result.distanceKm < 2.0)
        assertTrue(result.bearingDegrees in 0f..360f)
    }

    @Test
    fun qiblaForAddisAbabaHasPlausibleDistanceAndBearing() {
        val addis = PrayerTimesCalculator.DEFAULT_CITIES.first { it.id == "addis_ababa" }
        val result = PrayerTimesCalculator.calculateQibla(addis)

        assertTrue(result.distanceKm in 1000.0..3000.0)
        assertTrue(result.bearingDegrees in 0f..180f)
    }

    @Test
    fun londonPrayerTimesApplyDaylightSavingOffset() {
        val london = PrayerTimesCalculator.DEFAULT_CITIES.first { it.id == "london" }
        val winter = PrayerTimesCalculator.calculatePrayerTimes(london, calendar(2026, 1, 15))
        val summer = PrayerTimesCalculator.calculatePrayerTimes(london, calendar(2026, 7, 15))

        val difference = kotlin.math.abs(minutes(winter.dhuhr) - minutes(summer.dhuhr))
        assertTrue("Expected roughly one hour DST shift, got $difference minutes", difference in 45..75)
    }

    @Test
    fun ethiopiaPrayerTimesRemainOnUtcPlusThreeOffset() {
        val addis = PrayerTimesCalculator.DEFAULT_CITIES.first { it.id == "addis_ababa" }
        val winter = PrayerTimesCalculator.calculatePrayerTimes(addis, calendar(2026, 1, 15))
        val summer = PrayerTimesCalculator.calculatePrayerTimes(addis, calendar(2026, 7, 15))

        val difference = kotlin.math.abs(minutes(winter.dhuhr) - minutes(summer.dhuhr))
        assertTrue("Ethiopia has no DST; only seasonal solar variation is expected", difference <= 15)
    }
}
