package com.quranku.quranku_app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "liked_verses")
data class LikedVerseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val surahId: Int,
    val surahName: String,
    val verseNumber: Int,
    val verseText: String,
    val verseTranslation: String,
    val isLiked: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)