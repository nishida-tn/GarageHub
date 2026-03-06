package com.hsgaragepecas.garagehub.data.local.order

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * The Data Access Object for the [OrderEntity] class.
 */
@Dao
interface OrderDao {

    /**
     * Inserts an order into the database.
     *
     * @param order The order to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    /**
     * Gets all orders from the database.
     *
     * @return A flow of all orders.
     */
    @Query("SELECT * FROM orders ORDER BY orderDate DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    /**
     * Gets an order by its order number.
     *
     * @param orderNumber The order number of the order to be retrieved.
     * @return The order with the given order number.
     */
    @Query("SELECT * FROM orders WHERE orderNumber = :orderNumber")
    suspend fun getOrderByOrderNumber(orderNumber: Int): OrderEntity?

    /**
     * Updates an order in the database.
     *
     * @param order The order to be updated.
     */
    @Update
    suspend fun updateOrder(order: OrderEntity)

    /**
     * Deletes an order from the database.
     *
     * @param order The order to be deleted.
     */
    @Delete
    suspend fun deleteOrder(order: OrderEntity)
}
