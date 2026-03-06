package com.hsgaragepecas.garagehub.data.repository

import com.hsgaragepecas.garagehub.data.local.order.OrderDao
import com.hsgaragepecas.garagehub.data.local.order.OrderEntity
import com.hsgaragepecas.garagehub.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * The implementation of the [OrderRepository] interface.
 *
 * @param orderDao The DAO for the [OrderEntity] class.
 */
class OrderRepositoryImpl @Inject constructor(
    private val orderDao: OrderDao
) : OrderRepository {
    override suspend fun insertOrder(order: OrderEntity) {
        orderDao.insertOrder(order)
    }

    override fun getAllOrders(): Flow<List<OrderEntity>> {
        return orderDao.getAllOrders()
    }

    override suspend fun getOrderByOrderNumber(orderNumber: Int): OrderEntity? {
        return orderDao.getOrderByOrderNumber(orderNumber)
    }

    override suspend fun updateOrder(order: OrderEntity) {
        orderDao.updateOrder(order)
    }

    override suspend fun deleteOrder(order: OrderEntity) {
        orderDao.deleteOrder(order)
    }
}
