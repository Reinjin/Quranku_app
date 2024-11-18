package com.quranku.quranku_app.data.models

data class Surah(
    val id: Int,
    val name: String,
    val transliteration: String,
    val translation: String,
    val type: String,
    val total_verses: Int,
    val verses: List<Verse>
)

data class Verse(
    val id: Int,
    val text: String,
    val translation: String
)
