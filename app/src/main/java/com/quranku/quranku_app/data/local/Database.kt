package com.quranku.quranku_app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.quranku.quranku_app.data.local.dao.LikedVerseDao
import com.quranku.quranku_app.data.local.entity.LikedVerseEntity

@Database(entities = [LikedVerseEntity::class], version = 1, exportSchema = false)
abstract class QuranDatabase : RoomDatabase() {
    abstract fun likedVerseDao(): LikedVerseDao
}
