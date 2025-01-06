package com.quranku.quranku_app.data.models

data class PrayerTimesResponse(
    val fajr: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val isha: String
)