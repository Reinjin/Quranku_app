package com.quranku.quranku_app.data.models

data class LikedVerse(
    val surahId: Int,
    val surahName: String,
    val verseNumber: Int,
    val verseText: String,
    val verseTranslation: String
)
