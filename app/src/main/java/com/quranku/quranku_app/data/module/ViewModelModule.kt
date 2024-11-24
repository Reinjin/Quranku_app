package com.quranku.quranku_app.data.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext


@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @Provides
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }
}