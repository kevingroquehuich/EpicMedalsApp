package com.roque.epicmedalsapp.core.di

import android.content.Context
import com.roque.data.datastore.MedalDataStore
import com.roque.data.datastore.StreakDataStore
import com.roque.data.repository.MedalRepositoryImpl
import com.roque.data.repository.StreakRepositoryImpl
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
    fun provideStreakDataStore(@ApplicationContext context: Context) = StreakDataStore(context)


    @Provides
    @Singleton
    fun provideMedalRepositoryImpl(
        @ApplicationContext context: Context,
        dataStore: MedalDataStore
    ): MedalRepositoryImpl = MedalRepositoryImpl(context, dataStore)

    @Provides
    @Singleton
    fun provideStreakRepositoryImpl(
        dataStore: StreakDataStore
    ): StreakRepositoryImpl = StreakRepositoryImpl( dataStore)
}