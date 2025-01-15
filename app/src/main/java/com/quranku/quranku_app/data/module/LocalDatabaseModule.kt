package com.quranku.quranku_app.data.module

import android.content.Context
import androidx.room.Room
import com.quranku.quranku_app.data.local.QuranDatabase
import com.quranku.quranku_app.data.local.dao.LikedVerseDao
import com.quranku.quranku_app.data.repositorys.LikedVerseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideQuranDatabase(@ApplicationContext context: Context): QuranDatabase {
        return Room.databaseBuilder(
            context,
            QuranDatabase::class.java,
            "quran_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideLikedVerseDao(database: QuranDatabase): LikedVerseDao {
        return database.likedVerseDao()
    }

    @Provides
    @Singleton
    fun provideLikedVerseRepository(likedVerseDao: LikedVerseDao): LikedVerseRepository {
        return LikedVerseRepository(likedVerseDao)
    }
}
