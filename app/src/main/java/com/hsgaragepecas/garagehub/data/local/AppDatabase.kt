package com.hsgaragepecas.garagehub.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.hsgaragepecas.garagehub.data.local.order.OrderDao
import com.hsgaragepecas.garagehub.data.local.order.OrderEntity
import com.hsgaragepecas.garagehub.data.local.order.OrderStatus

/**
 * The Room database for the application.
 */
@Database(entities = [OrderEntity::class], version = 1, exportSchema = false)
@TypeConverters(AppDatabase.Converters::class)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Returns the DAO for the [OrderEntity] class.
     */
    abstract fun orderDao(): OrderDao

    /**
     * A class that contains the type converters for the database.
     */
    class Converters {
        /**
         * Converts an [OrderStatus] to a [String].
         *
         * @param status The [OrderStatus] to be converted.
         * @return The converted [String].
         */
        @TypeConverter
        fun fromOrderStatus(status: OrderStatus): String {
            return status.name
        }

        /**
         * Converts a [String] to an [OrderStatus].
         *
         * @param statusString The [String] to be converted.
         * @return The converted [OrderStatus].
         */
        @TypeConverter
        fun toOrderStatus(statusString: String): OrderStatus {
            return OrderStatus.valueOf(statusString)
        }
    }
}
