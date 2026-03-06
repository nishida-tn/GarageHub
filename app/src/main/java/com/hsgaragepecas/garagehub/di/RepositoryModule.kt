package com.hsgaragepecas.garagehub.di

import com.hsgaragepecas.garagehub.data.repository.OrderRepositoryImpl
import com.hsgaragepecas.garagehub.domain.repository.OrderRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * A Dagger Hilt module that provides the repositories.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * Binds the [OrderRepositoryImpl] to the [OrderRepository] interface.
     *
     * @param orderRepositoryImpl The [OrderRepositoryImpl] instance.
     * @return The [OrderRepository] instance.
     */
    @Binds
    @Singleton
    abstract fun bindOrderRepository(orderRepositoryImpl: OrderRepositoryImpl): OrderRepository
}
