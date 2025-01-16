package com.quranku.quranku_app.data.repositorys

import com.quranku.quranku_app.data.local.dao.LikedVerseDao
import com.quranku.quranku_app.data.local.entity.LikedVerseEntity
import com.quranku.quranku_app.data.models.LikedVerse
import com.quranku.quranku_app.data.models.Verse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LikedVerseRepository @Inject constructor(
    private val likedVerseDao: LikedVerseDao
) {
    suspend fun toggleLikeVerse(
        surahId: Int,
        surahName: String,
        verse: Verse
    ) {
        val existingLike = likedVerseDao.isVerseLiked(surahId, verse.id).first()

        if (existingLike == null) {
            // Tambahkan ke liked verses
            val likedVerse = LikedVerseEntity(
                surahId = surahId,
                surahName = surahName,
                verseNumber = verse.id,
                verseText = verse.text,
                verseTranslation = verse.translation
            )
            likedVerseDao.insertLikedVerse(likedVerse)
        } else {
            // Hapus dari liked verses
            likedVerseDao.deleteLikedVerse(surahId, verse.id)
        }
    }

    fun isVerseLiked(surahId: Int, verseNumber: Int): Flow<Boolean> {
        return likedVerseDao.isVerseLiked(surahId, verseNumber)
            .map { it != null }
    }

    fun getAllLikedVerses(): Flow<List<LikedVerse>> {
        return likedVerseDao.getAllLikedVerses()
            .map { entities ->
                entities.map { it.toDomainModel() }
            }
    }

    private fun LikedVerseEntity.toDomainModel(): LikedVerse {
        return LikedVerse(
            surahId = surahId,
            surahName = surahName,
            verseNumber = verseNumber,
            verseText = verseText,
            verseTranslation = verseTranslation
        )
    }

    suspend fun deleteLikeVerse(
        surahId: Int,
        verseId: Int
    ) {
        likedVerseDao.deleteLikedVerse(surahId, verseId)
    }
}