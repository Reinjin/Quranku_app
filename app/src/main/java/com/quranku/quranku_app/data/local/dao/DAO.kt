package com.quranku.quranku_app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.quranku.quranku_app.data.local.entity.LikedVerseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LikedVerseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLikedVerse(verse: LikedVerseEntity)

    @Query("DELETE FROM liked_verses WHERE surahId = :surahId AND verseNumber = :verseNumber")
    suspend fun deleteLikedVerse(surahId: Int, verseNumber: Int)

    @Query("SELECT * FROM liked_verses WHERE surahId = :surahId AND verseNumber = :verseNumber")
    fun isVerseLiked(surahId: Int, verseNumber: Int): Flow<LikedVerseEntity?>

    @Query("SELECT * FROM liked_verses ORDER BY timestamp DESC")
    fun getAllLikedVerses(): Flow<List<LikedVerseEntity>>
}