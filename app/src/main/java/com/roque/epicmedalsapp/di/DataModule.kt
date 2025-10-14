package com.roque.epicmedalsapp.di

import android.app.Application
import android.content.Context
import com.roque.data.datastore.MedalDataStore
import com.roque.data.repository.MedalRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideMedalDataStore(@ApplicationContext context: Context) = MedalDataStore(context)

    @Provides
    @Singleton
    fun provideMedalRepositoryImpl(
        @ApplicationContext context: Context,
        dataStore: MedalDataStore
    ): MedalRepositoryImpl = MedalRepositoryImpl(context, dataStore)
}