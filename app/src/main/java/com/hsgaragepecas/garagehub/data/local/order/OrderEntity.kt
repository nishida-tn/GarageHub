package com.hsgaragepecas.garagehub.data.local.order

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents an order in the database.
 *
 * @param orderNumber The order number.
 * @param orderDate The date of the order.
 * @param carBrand The brand of the car.
 * @param status The status of the order.
 * @param numberOfItems The number of items in the order.
 * @param price The price of the order.
 */
@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val orderNumber: Int = 0,
    val orderDate: Long,
    val carBrand: String,
    val status: OrderStatus,
    val numberOfItems: Int,
    val price: Double
)
