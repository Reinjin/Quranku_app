package com.quranku.quranku_app.data.module

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.quranku.quranku_app.data.PreferencesManager
import com.quranku.quranku_app.data.api.ApiService
import com.quranku.quranku_app.data.repositorys.HijaiyahRepository
import com.quranku.quranku_app.data.repositorys.HistoryRepository
import com.quranku.quranku_app.data.repositorys.HomeRepository
import com.quranku.quranku_app.data.repositorys.LocationRepository
import com.quranku.quranku_app.data.repositorys.LoginRepository
import com.quranku.quranku_app.data.repositorys.ProfileRepository
import com.quranku.quranku_app.data.repositorys.RegisterRepository
import com.quranku.quranku_app.ui.util.AudioRecorder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    @Singleton
    fun provideHistoryRepository(
        apiService: ApiService,
        preferencesManager: PreferencesManager
    ): HistoryRepository {
        return HistoryRepository(apiService, preferencesManager)
    }

    @Provides
    @Singleton
    fun provideHomeRepository(
        apiService: ApiService,
        preferencesManager: PreferencesManager,
        locationRepository: LocationRepository
    ): HomeRepository {
        return HomeRepository(apiService, preferencesManager, locationRepository)
    }

    @Provides
    @Singleton
    fun provideLocationRepository(
        fusedLocationProviderClient: FusedLocationProviderClient,
        @ApplicationContext context: Context
    ): LocationRepository {
        return LocationRepository(fusedLocationProviderClient, context)
    }

    @Provides
    @Singleton
    fun provideLoginRepository(
        apiService: ApiService
    ): LoginRepository {
        return LoginRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(
        apiService: ApiService,
        preferencesManager: PreferencesManager
    ): ProfileRepository {
        return ProfileRepository(apiService, preferencesManager)
    }

    @Provides
    @Singleton
    fun provideRegisterRepository(
        apiService: ApiService
    ): RegisterRepository {
        return RegisterRepository(apiService)
    }
}