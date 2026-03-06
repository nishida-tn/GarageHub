package com.hsgaragepecas.garagehub.di

import android.content.Context
import androidx.room.Room
import com.hsgaragepecas.garagehub.data.local.AppDatabase
import com.hsgaragepecas.garagehub.data.local.order.OrderDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * A Dagger Hilt module that provides the database and its DAOs.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provides the [AppDatabase] instance.
     *
     * @param context The application context.
     * @return The [AppDatabase] instance.
     */
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "garagehub-database"
        ).build()
    }

    /**
     * Provides the [OrderDao] instance.
     *
     * @param database The [AppDatabase] instance.
     * @return The [OrderDao] instance.
     */
    @Singleton
    @Provides
    fun provideOrderDao(database: AppDatabase): OrderDao {
        return database.orderDao()
    }
}
