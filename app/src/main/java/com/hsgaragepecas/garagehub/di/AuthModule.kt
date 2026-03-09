package com.hsgaragepecas.garagehub.di

import com.hsgaragepecas.garagehub.data.repository.AuthRepositoryImpl
import com.hsgaragepecas.garagehub.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * A Hilt module that provides the authentication-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    /**
     * Binds the [AuthRepositoryImpl] to the [AuthRepository] interface.
     *
     * @param authRepositoryImpl The [AuthRepositoryImpl] instance.
     * @return The [AuthRepository] instance.
     */
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}
