package com.quranku.quranku_app.data.models

data class PrayerTimesRequest(
    val latitude: Double,
    val longitude: Double,
    val date: String
)

data class PrayerTimesResponse(
    val Fajr: String,
    val Dhuhr: String,
    val Asr: String,
    val Maghrib: String,
    val Isha: String,
    val City: String
)