package com.quranku.quranku_app.data.module

import com.quranku.quranku_app.data.PreferencesManager
import com.quranku.quranku_app.data.api.ApiService
import com.quranku.quranku_app.data.repositorys.HijaiyahRepository
import com.quranku.quranku_app.ui.util.AudioRecorder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideHijaiyahRepository(
        apiService: ApiService,
        audioRecorder: AudioRecorder,
        preferencesManager: PreferencesManager
    ): HijaiyahRepository {
        return HijaiyahRepository(apiService, audioRecorder, preferencesManager)
    }
}