package com.example.ui.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.IslamicCity
import com.example.data.model.PrayerSchedule
import com.example.data.prayer.PrayerTimesCalculator
import com.example.ui.components.IslamicEmblemMotif
import com.example.ui.theme.LocalIslamicColors
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * Screen providing verified Astronomical Prayer Times and Interactive Qibla Compass.
 */
@Composable
fun PrayerQiblaScreen(
    currentCity: IslamicCity,
    prayerSchedule: PrayerSchedule,
    onSelectCity: (IslamicCity) -> Unit,
    modifier: Modifier = Modifier
) {
    val islamicColors = LocalIslamicColors.current
    var selectedTab by remember { mutableIntStateOf(0) }
    var showCityDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Tab selector: Prayer Times vs Qibla Compass
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = islamicColors.cardBg,
            contentColor = islamicColors.goldAccent,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = islamicColors.goldAccent,
                    height = 3.dp
                )
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Mosque, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "የሰላት ወቅቶች",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp
                        )
                    }
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ቂብላ",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp
                        )
                    }
                }
            )
        }

        // Location header bar with city change button
        Surface(
            color = islamicColors.surfaceHighlight.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showCityDialog = true }
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "City",
                        tint = islamicColors.goldAccent,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "${currentCity.nameAm} (${currentCity.nameEn})",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = currentCity.country,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = islamicColors.goldAccent.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, islamicColors.goldAccent.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = "ከተማ ቀይር",
                        style = MaterialTheme.typography.labelSmall,
                        color = islamicColors.goldAccent,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Tab Content
        if (selectedTab == 0) {
            PrayerTimesTabContent(prayerSchedule = prayerSchedule)
        } else {
            QiblaCompassTabContent(city = currentCity)
        }
    }

    // City Selection Dialog
    if (showCityDialog) {
        CitySelectionDialog(
            currentCityId = currentCity.id,
            onCitySelected = {
                onSelectCity(it)
                showCityDialog = false
            },
            onDismiss = { showCityDialog = false }
        )
    }
}

@Composable
private fun PrayerTimesTabContent(prayerSchedule: PrayerSchedule) {
    val islamicColors = LocalIslamicColors.current

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Next Prayer Hero Card
        item {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = islamicColors.cardBg,
                border = BorderStroke(1.5.dp, islamicColors.goldAccent.copy(alpha = 0.7f)),
                shadowElevation = 3.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "ቀጣይ ሰላት",
                            style = MaterialTheme.typography.labelMedium,
                            color = islamicColors.goldAccent,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = prayerSchedule.nextPrayerName,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = prayerSchedule.nextPrayerTime,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            color = islamicColors.goldAccent
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        val hrs = prayerSchedule.minutesUntilNext / 60
                        val mins = prayerSchedule.minutesUntilNext % 60
                        val countdownText = if (hrs > 0) "ከ $hrs ሰዓት ከ $mins ደቂቃ በኋላ" else "ከ $mins ደቂቃ በኋላ"
                        Text(
                            text = countdownText,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = islamicColors.arabicCalligraphyColor
                        )
                    }
                }
            }
        }

        // List of all prayers
        val prayers = listOf(
            Triple("ፈጅር (Fajr)", prayerSchedule.fajr, Icons.Default.WbTwilight),
            Triple("ፀሐይ መውጣት (Sunrise)", prayerSchedule.sunrise, Icons.Default.WbSunny),
            Triple("ዙህር (Dhuhr)", prayerSchedule.dhuhr, Icons.Default.WbSunny),
            Triple("ዐስር (Asr)", prayerSchedule.asr, Icons.Default.Schedule),
            Triple("መግሪብ (Maghrib)", prayerSchedule.maghrib, Icons.Default.WbTwilight),
            Triple("ዒሻእ (Isha)", prayerSchedule.isha, Icons.Default.NightsStay)
        )

        items(prayers) { (name, time, icon) ->
            val isNext = name == prayerSchedule.nextPrayerName
            PrayerTimeRow(
                name = name,
                time = time,
                icon = icon,
                isNext = isNext
            )
        }

        // Calculation Method Note
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "ስሌት ዘዴ: ${prayerSchedule.calculationMethod}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "በሥነ-ፈለክ ስሌት የተመሠረተ",
                    style = MaterialTheme.typography.labelSmall,
                    color = islamicColors.goldAccent.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
private fun PrayerTimeRow(
    name: String,
    time: String,
    icon: ImageVector,
    isNext: Boolean
) {
    val islamicColors = LocalIslamicColors.current

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (isNext) islamicColors.surfaceHighlight else islamicColors.cardBg,
        border = BorderStroke(
            width = if (isNext) 1.5.dp else 1.dp,
            color = if (isNext) islamicColors.goldAccent else islamicColors.cardBorder.copy(alpha = 0.5f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isNext) islamicColors.goldAccent else islamicColors.goldAccent.copy(alpha = 0.15f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = name,
                        tint = if (isNext) Color.Black else islamicColors.goldAccent,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = if (isNext) FontWeight.Bold else FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (isNext) {
                        Text(
                            text = "ቀጣይ ሰላት",
                            style = MaterialTheme.typography.labelSmall,
                            color = islamicColors.goldAccent,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Text(
                text = time,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (isNext) islamicColors.goldAccent else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun QiblaCompassTabContent(city: IslamicCity) {
    val context = LocalContext.current
    val islamicColors = LocalIslamicColors.current

    val qiblaInfo = remember(city) {
        PrayerTimesCalculator.calculateQibla(city)
    }

    var azimuthDegrees by remember { mutableFloatStateOf(0f) }

    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        val rotationSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
            ?: sensorManager?.getDefaultSensor(Sensor.TYPE_ORIENTATION)

        val listener = object : SensorEventListener {
            private val rotationMatrix = FloatArray(9)
            private val orientationAngles = FloatArray(3)

            override fun onSensorChanged(event: SensorEvent?) {
                if (event == null) return
                if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                    SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                    SensorManager.getOrientation(rotationMatrix, orientationAngles)
                    val azimuthRad = orientationAngles[0]
                    var deg = Math.toDegrees(azimuthRad.toDouble()).toFloat()
                    deg = (deg + 360f) % 360f
                    azimuthDegrees = deg
                } else if (event.sensor.type == Sensor.TYPE_ORIENTATION) {
                    azimuthDegrees = event.values[0]
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        if (rotationSensor != null) {
            sensorManager?.registerListener(listener, rotationSensor, SensorManager.SENSOR_DELAY_UI)
        }

        onDispose {
            sensorManager?.unregisterListener(listener)
        }
    }

    val animatedRotation by animateFloatAsState(
        targetValue = -azimuthDegrees,
        label = "compassRotation"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Qibla Angle Card Header
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = islamicColors.cardBg,
            border = BorderStroke(1.dp, islamicColors.goldAccent.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "የቂብላ አቅጣጫ",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${qiblaInfo.bearingDegrees.roundToInt()}° N",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = islamicColors.goldAccent
                    )
                }

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(30.dp)
                        .background(islamicColors.cardBorder.copy(alpha = 0.5f))
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "እስከ መካ ያለው ርቀት",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${qiblaInfo.distanceKm.roundToInt()} ኪ.ሜ",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Large Rotating Compass Dial
        Box(
            modifier = Modifier
                .size(280.dp),
            contentAlignment = Alignment.Center
        ) {
            // Rotating Compass Rose
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .rotate(animatedRotation)
            ) {
                val center = this.center
                val radius = size.minDimension / 2f - 12f

                // Outer border circle
                drawCircle(
                    color = islamicColors.cardBorder,
                    radius = radius,
                    style = Stroke(width = 2.dp.toPx())
                )

                // Sub-ring
                drawCircle(
                    color = islamicColors.goldAccent.copy(alpha = 0.2f),
                    radius = radius * 0.82f,
                    style = Stroke(width = 1.dp.toPx())
                )

                // Draw 36 tick marks around circle
                for (i in 0 until 36) {
                    val angle = (i * 10) * PI / 180f - PI / 2
                    val isMajor = i % 9 == 0
                    val len = if (isMajor) 18f else 10f
                    val r1 = radius - len
                    val x1 = center.x + r1 * cos(angle).toFloat()
                    val y1 = center.y + r1 * sin(angle).toFloat()
                    val x2 = center.x + radius * cos(angle).toFloat()
                    val y2 = center.y + radius * sin(angle).toFloat()

                    drawLine(
                        color = if (isMajor) islamicColors.goldAccent else islamicColors.cardBorder.copy(alpha = 0.5f),
                        start = androidx.compose.ui.geometry.Offset(x1, y1),
                        end = androidx.compose.ui.geometry.Offset(x2, y2),
                        strokeWidth = if (isMajor) 2.5f else 1.2f
                    )
                }
            }

            // Fixed Kaaba Indicator Needle pointing towards Qibla Bearing relative to dial
            val relativeQiblaAngle = qiblaInfo.bearingDegrees + animatedRotation
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .rotate(relativeQiblaAngle),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    // Golden Kaaba emblem pointing to Mecca
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = islamicColors.goldAccent,
                        shadowElevation = 4.dp,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "🕋",
                                fontSize = 18.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "ቂብላ",
                        fontWeight = FontWeight.Bold,
                        color = islamicColors.goldAccent,
                        fontSize = 12.sp
                    )
                }
            }

            // Center core
            Surface(
                shape = CircleShape,
                color = islamicColors.cardBg,
                border = BorderStroke(2.dp, islamicColors.goldAccent),
                modifier = Modifier.size(64.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Navigation,
                        contentDescription = "Bearing",
                        tint = islamicColors.goldAccent,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Calibration Advice
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = islamicColors.cardBg,
            border = BorderStroke(1.dp, islamicColors.cardBorder.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CompassCalibration,
                    contentDescription = "Calibration",
                    tint = islamicColors.goldAccent,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "ትክክለኛውን አቅጣጫ ለማግኘት ስልክዎን በጠፍጣፋ ቦታ ላይ ያድርጉ ወይም በ '8' ቅርጽ በማንቀሳቀስ ካሊብሬት ያድርጉ።",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun CitySelectionDialog(
    currentCityId: String,
    onCitySelected: (IslamicCity) -> Unit,
    onDismiss: () -> Unit
) {
    val islamicColors = LocalIslamicColors.current
    val cities = PrayerTimesCalculator.DEFAULT_CITIES

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "ከተማ ይምረጡ (Select City)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            LazyColumn(modifier = Modifier.height(300.dp)) {
                items(cities) { city ->
                    val isSelected = city.id == currentCityId
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) islamicColors.surfaceHighlight else Color.Transparent,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onCitySelected(city) }
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "${city.nameAm} (${city.nameEn})",
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) islamicColors.goldAccent else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = city.country,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("ተመለስ (Close)")
            }
        }
    )
}
