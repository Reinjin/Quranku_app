package com.quranku.quranku_app.ui.util

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quranku.quranku_app.data.models.PrayerTimesResponse
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import com.quranku.quranku_app.R


@RequiresApi(Build.VERSION_CODES.O)
fun getNextPrayerTime(
    currentTime: String,
    fajr: String,
    dhuhr: String,
    asr: String,
    maghrib: String,
    isha: String
): String {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val timeWithSecondsFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    // Parse current time handling both formats (with and without seconds)
    val current = try {
        LocalTime.parse(currentTime, timeWithSecondsFormatter)
    } catch (e: DateTimeParseException) {
        try {
            LocalTime.parse(currentTime, timeFormatter)
        } catch (e: DateTimeParseException) {
            return "" // Return empty string if parsing fails
        }
    }

    // Create pairs of prayer names and their times
    val prayerTimes = listOf(
        "Subuh" to LocalTime.parse(fajr, timeFormatter),
        "Dzuhur" to LocalTime.parse(dhuhr, timeFormatter),
        "Ashar" to LocalTime.parse(asr, timeFormatter),
        "Maghrib" to LocalTime.parse(maghrib, timeFormatter),
        "Isya" to LocalTime.parse(isha, timeFormatter)
    )

    return prayerTimes
        .firstOrNull { it.second.isAfter(current) }?.first ?: "Subuh"
}

@Composable
fun PrayerTimeColumn(
    name: String,
    time: String,
    isNextPrayer: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .then(
                if (isNextPrayer) {
                    Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x31169496)) // opacity and color setting
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                } else {
                    Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                }
            )
    ) {
        Text(
            text = name,
            fontSize = 12.sp,
            color = if (isNextPrayer) Color.Black else Color.Black.copy(alpha = 0.6f)
        )
        Text(
            text = time,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = if (isNextPrayer) colorResource(id = R.color.blue_dark_light) else colorResource(id = R.color.blue_dark_light).copy(alpha = 0.6f)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PrayerTimesRow(
    currentTime: String,
    prayerTimes: PrayerTimesResponse?,
) {
    val nextPrayer = if (prayerTimes != null) {
        getNextPrayerTime(
            currentTime = currentTime,
            fajr = prayerTimes.fajr,
            dhuhr = prayerTimes.dhuhr,
            asr = prayerTimes.asr,
            maghrib = prayerTimes.maghrib,
            isha = prayerTimes.isha
        )
    } else null

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        PrayerTimeColumn(
            name = "Subuh",
            time = prayerTimes?.fajr ?: "--:--",
            isNextPrayer = nextPrayer == "Subuh"
        )
        PrayerTimeColumn(
            name = "Dzuhur",
            time = prayerTimes?.dhuhr ?: "--:--",
            isNextPrayer = nextPrayer == "Dzuhur"
        )
        PrayerTimeColumn(
            name = "Ashar",
            time = prayerTimes?.asr ?: "--:--",
            isNextPrayer = nextPrayer == "Ashar"
        )
        PrayerTimeColumn(
            name = "Maghrib",
            time = prayerTimes?.maghrib ?: "--:--",
            isNextPrayer = nextPrayer == "Maghrib"
        )
        PrayerTimeColumn(
            name = "Isya",
            time = prayerTimes?.isha ?: "--:--",
            isNextPrayer = nextPrayer == "Isya"
        )
    }
}