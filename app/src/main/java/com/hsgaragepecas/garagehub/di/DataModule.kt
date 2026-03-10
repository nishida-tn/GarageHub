package com.hsgaragepecas.garagehub.di

import com.hsgaragepecas.garagehub.data.local.user.UserPreferencesDataSource
import com.hsgaragepecas.garagehub.data.local.user.UserPreferencesDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindUserPreferencesDataSource(
        userPreferencesDataSource: UserPreferencesDataSourceImpl
    ): UserPreferencesDataSource
}
