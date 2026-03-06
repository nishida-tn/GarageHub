package com.hsgaragepecas.garagehub.domain.repository

import com.hsgaragepecas.garagehub.data.local.order.OrderEntity
import kotlinx.coroutines.flow.Flow

/**
 * An interface for the order repository.
 */
interface OrderRepository {
    /**
     * Inserts an order into the database.
     *
     * @param order The order to be inserted.
     */
    suspend fun insertOrder(order: OrderEntity)
    /**
     * Gets all orders from the database.
     *
     * @return A flow of all orders.
     */
    fun getAllOrders(): Flow<List<OrderEntity>>
    /**
     * Gets an order by its order number.
     *
     * @param orderNumber The order number of the order to be retrieved.
     * @return The order with the given order number.
     */
    suspend fun getOrderByOrderNumber(orderNumber: Int): OrderEntity?
    /**
     * Updates an order in the database.
     *
     * @param order The order to be updated.
     */
    suspend fun updateOrder(order: OrderEntity)
    /**
     * Deletes an order from the database.
     *
     * @param order The order to be deleted.
     */
    suspend fun deleteOrder(order: OrderEntity)
}
