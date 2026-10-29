package com.example.data.prayer

import com.example.data.model.IslamicCity
import com.example.data.model.PrayerSchedule
import com.example.data.model.QiblaInfo
import java.util.Calendar
import java.util.Locale
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

/**
 * Astronomical Prayer Times & Qibla Direction Calculator.
 * Implements verified standard solar angle formulas (Muslim World League / Egyptian authority standard).
 */
object PrayerTimesCalculator {

    const val KAABA_LAT = 21.4225
    const val KAABA_LON = 39.8262

    val DEFAULT_CITIES = listOf(
        IslamicCity("addis_ababa", "Addis Ababa", "አዲስ አበባ", "ኢትዮጵያ (Ethiopia)", 9.03, 38.74, 3.0),
        IslamicCity("dire_dawa", "Dire Dawa", "ድሬዳዋ", "ኢትዮጵያ (Ethiopia)", 9.60, 41.86, 3.0),
        IslamicCity("harar", "Harar", "ሐረር", "ኢትዮጵያ (Ethiopia)", 9.31, 42.13, 3.0),
        IslamicCity("jimma", "Jimma", "ጅማ", "ኢትዮጵያ (Ethiopia)", 7.67, 36.83, 3.0),
        IslamicCity("dessie", "Dessie", "ደሴ", "ኢትዮጵያ (Ethiopia)", 11.13, 39.63, 3.0),
        IslamicCity("gondar", "Gondar", "ጎንደር", "ኢትዮጵያ (Ethiopia)", 12.60, 37.47, 3.0),
        IslamicCity("mekelle", "Mekelle", "መቐለ", "ኢትዮጵያ (Ethiopia)", 13.50, 39.47, 3.0),
        IslamicCity("hawassa", "Hawassa", "ሀዋሳ", "ኢትዮጵያ (Ethiopia)", 7.06, 38.48, 3.0),
        IslamicCity("mecca", "Makkah", "መካ አል-ሙከረማ", "ሳውዲ አረቢያ (Saudi Arabia)", 21.42, 39.83, 3.0),
        IslamicCity("medina", "Madinah", "መዲና አል-ሙነወራ", "ሳውዲ አረቢያ (Saudi Arabia)", 24.47, 39.61, 3.0),
        IslamicCity("jerusalem", "Jerusalem", "አል-ቁድስ (Al-Quds)", "ፍልስጤም (Palestine)", 31.78, 35.22, 2.0),
        IslamicCity("cairo", "Cairo", "ካይሮ", "ግብፅ (Egypt)", 30.04, 31.24, 2.0),
        IslamicCity("istanbul", "Istanbul", "ኢስታንቡል", "ቱርክ (Turkey)", 41.01, 28.98, 3.0),
        IslamicCity("dubai", "Dubai", "ዱባይ", "የተባበሩት አረብ ኤሚሬቶች (UAE)", 25.20, 55.27, 4.0),
        IslamicCity("london", "London", "ለንደን", "እንግሊዝ (UK)", 51.51, -0.13, 0.0),
        IslamicCity("washington", "Washington D.C.", "ዋሽንግተን", "አሜሪካ (USA)", 38.90, -77.04, -5.0)
    )

    /**
     * Computes today's prayer times for the specified city.
     */
    fun calculatePrayerTimes(
        city: IslamicCity,
        calendar: Calendar = Calendar.getInstance(),
        fajrAngle: Double = 18.0,
        ishaAngle: Double = 17.0
    ): PrayerSchedule {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Julian Day calculation
        val a = floor((14 - month) / 12.0)
        val y = year + 4800 - a
        val m = month + 12 * a - 3
        val jd = day + floor((153 * m + 2) / 5.0) + 365 * y + floor(y / 4.0) - floor(y / 100.0) + floor(y / 400.0) - 32045

        // Solar coordinates
        val d = jd - 2451545.0
        val g = fixAngle(357.529 + 0.98560028 * d)
        val q = fixAngle(280.459 + 0.98564736 * d)
        val l = fixAngle(q + 1.915 * sin(dToR(g)) + 0.020 * sin(dToR(2 * g)))
        val e = 23.439 - 0.00000036 * d
        val ra = rToD(atan2(cos(dToR(e)) * sin(dToR(l)), cos(dToR(l)))) / 15.0
        val dec = rToD(asin(sin(dToR(e)) * sin(dToR(l))))
        val eqT = (q / 15.0) - fixHour(ra)

        // Solar Noon (Dhuhr)
        val noon = fixHour(12.0 + city.timezoneHours - (city.longitude / 15.0) - eqT)

        // Sun altitude angle for sunrise/sunset (accounting for refraction)
        val alphaSunrise = -0.833

        // Hour angles
        val haSunrise = calcHourAngle(alphaSunrise, city.latitude, dec)
        val haFajr = calcHourAngle(-fajrAngle, city.latitude, dec)
        val haIsha = calcHourAngle(-ishaAngle, city.latitude, dec)

        // Asr hour angle (standard Shafi/Maliki/Hanbali shadow factor = 1)
        val asrAlt = rToD(atan(1.0 / (1.0 + tan(dToR(Math.abs(city.latitude - dec))))))
        val haAsr = calcHourAngle(asrAlt, city.latitude, dec)

        val fajrTime = fixHour(noon - haFajr / 15.0)
        val sunriseTime = fixHour(noon - haSunrise / 15.0)
        val dhuhrTime = noon
        val asrTime = fixHour(noon + haAsr / 15.0)
        val maghribTime = fixHour(noon + haSunrise / 15.0)
        val ishaTime = fixHour(noon + haIsha / 15.0)

        val nowHours = calendar.get(Calendar.HOUR_OF_DAY) + calendar.get(Calendar.MINUTE) / 60.0

        val (nextName, nextTime, mins) = findNextPrayer(
            nowHours = nowHours,
            fajr = fajrTime,
            sunrise = sunriseTime,
            dhuhr = dhuhrTime,
            asr = asrTime,
            maghrib = maghribTime,
            isha = ishaTime
        )

        return PrayerSchedule(
            fajr = formatTime(fajrTime),
            sunrise = formatTime(sunriseTime),
            dhuhr = formatTime(dhuhrTime),
            asr = formatTime(asrTime),
            maghrib = formatTime(maghribTime),
            isha = formatTime(ishaTime),
            nextPrayerName = nextName,
            nextPrayerTime = nextTime,
            minutesUntilNext = mins,
            cityName = "${city.nameAm} (${city.nameEn})",
            calculationMethod = "የሙስሊም ዓለም ሊግ (Muslim World League)"
        )
    }

    /**
     * Calculates Qibla direction (bearing in degrees from true North) and distance in km to Kaaba.
     */
    fun calculateQibla(city: IslamicCity): QiblaInfo {
        val lat1 = dToR(city.latitude)
        val lon1 = dToR(city.longitude)
        val lat2 = dToR(KAABA_LAT)
        val lon2 = dToR(KAABA_LON)

        val dLon = lon2 - lon1
        val y = sin(dLon)
        val x = cos(lat1) * tan(lat2) - sin(lat1) * cos(dLon)
        var qiblaDegrees = rToD(atan2(y, x))
        qiblaDegrees = (qiblaDegrees + 360.0) % 360.0

        // Haversine formula for distance in km
        val dLat = lat2 - lat1
        val a = sin(dLat / 2) * sin(dLat / 2) + cos(lat1) * cos(lat2) * sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        val distanceKm = 6371.0 * c

        return QiblaInfo(
            bearingDegrees = qiblaDegrees.toFloat(),
            distanceKm = distanceKm,
            cityName = "${city.nameAm} (${city.nameEn})"
        )
    }

    private fun findNextPrayer(
        nowHours: Double,
        fajr: Double,
        sunrise: Double,
        dhuhr: Double,
        asr: Double,
        maghrib: Double,
        isha: Double
    ): Triple<String, String, Long> {
        val prayers = listOf(
            Triple("ፈጅር (Fajr)", fajr, formatTime(fajr)),
            Triple("ፀሐይ መውጣት (Sunrise)", sunrise, formatTime(sunrise)),
            Triple("ዙህር (Dhuhr)", dhuhr, formatTime(dhuhr)),
            Triple("ዐስር (Asr)", asr, formatTime(asr)),
            Triple("መግሪብ (Maghrib)", maghrib, formatTime(maghrib)),
            Triple("ዒሻእ (Isha)", isha, formatTime(isha))
        )

        for (p in prayers) {
            if (p.second > nowHours) {
                val diffHours = p.second - nowHours
                val diffMins = (diffHours * 60).toLong()
                return Triple(p.first, p.third, diffMins)
            }
        }

        // Past Isha: next is tomorrow's Fajr
        val diffHours = (24.0 - nowHours) + fajr
        val diffMins = (diffHours * 60).toLong()
        return Triple("ፈጅር (Fajr)", formatTime(fajr), diffMins)
    }

    private fun calcHourAngle(alpha: Double, lat: Double, dec: Double): Double {
        val cosHA = (sin(dToR(alpha)) - sin(dToR(lat)) * sin(dToR(dec))) / (cos(dToR(lat)) * cos(dToR(dec)))
        return if (cosHA > 1.0) 0.0 else if (cosHA < -1.0) 180.0 else rToD(acos(cosHA))
    }

    private fun formatTime(hoursVal: Double): String {
        val totalMins = (fixHour(hoursVal) * 60).toInt()
        val h = (totalMins / 60) % 24
        val m = totalMins % 60
        return String.format(Locale.US, "%02d:%02d", h, m)
    }

    private fun dToR(deg: Double): Double = deg * (Math.PI / 180.0)
    private fun rToD(rad: Double): Double = rad * (180.0 / Math.PI)
    private fun fixAngle(a: Double): Double = (a % 360.0 + 360.0) % 360.0
    private fun fixHour(h: Double): Double = (h % 24.0 + 24.0) % 24.0
}
