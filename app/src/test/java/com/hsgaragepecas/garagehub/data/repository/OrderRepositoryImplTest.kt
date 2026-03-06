package com.hsgaragepecas.garagehub.data.repository

import com.hsgaragepecas.garagehub.data.local.order.OrderDao
import com.hsgaragepecas.garagehub.data.local.order.OrderEntity
import com.hsgaragepecas.garagehub.data.local.order.OrderStatus
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class OrderRepositoryImplTest {

    private lateinit var orderDao: OrderDao
    private lateinit var orderRepository: OrderRepositoryImpl

    @Before
    fun setUp() {
        orderDao = mockk()
        orderRepository = OrderRepositoryImpl(orderDao)
    }

    @Test
    fun `insertOrder calls dao's insertOrder`() = runTest {
        // Arrange
        val order = OrderEntity(1, 1, "Client", OrderStatus.ABERTO, 1, 100.0)
        coEvery { orderDao.insertOrder(order) } returns Unit

        // Act
        orderRepository.insertOrder(order)

        // Assert
        coVerify(exactly = 1) { orderDao.insertOrder(order) }
    }

    @Test
    fun `getAllOrders returns flow of orders from dao`() = runTest {
        // Arrange
        val orders = listOf(
            OrderEntity(1, 1, "Client A", OrderStatus.ABERTO, 1, 100.0),
            OrderEntity(2, 2, "Client B", OrderStatus.CONCLUIDO, 2, 200.0)
        )
        every { orderDao.getAllOrders() } returns flowOf(orders)

        // Act
        val result = orderRepository.getAllOrders().first()

        // Assert
        assertEquals(orders, result)
        coVerify(exactly = 1) { orderDao.getAllOrders() }
    }

    @Test
    fun `getOrderByOrderNumber returns order from dao`() = runTest {
        // Arrange
        val orderNumber = 123
        val order = OrderEntity(orderNumber, 1, "Client", OrderStatus.ABERTO, 1, 100.0)
        coEvery { orderDao.getOrderByOrderNumber(orderNumber) } returns order

        // Act
        val result = orderRepository.getOrderByOrderNumber(orderNumber)

        // Assert
        assertEquals(order, result)
        coVerify(exactly = 1) { orderDao.getOrderByOrderNumber(orderNumber) }
    }

    @Test
    fun `updateOrder calls dao's updateOrder`() = runTest {
        // Arrange
        val order = OrderEntity(1, 1, "Client", OrderStatus.ABERTO, 1, 100.0)
        coEvery { orderDao.updateOrder(order) } returns Unit

        // Act
        orderRepository.updateOrder(order)

        // Assert
        coVerify(exactly = 1) { orderDao.updateOrder(order) }
    }

    @Test
    fun `deleteOrder calls dao's deleteOrder`() = runTest {
        // Arrange
        val order = OrderEntity(1, 1, "Client", OrderStatus.ABERTO, 1, 100.0)
        coEvery { orderDao.deleteOrder(order) } returns Unit

        // Act
        orderRepository.deleteOrder(order)

        // Assert
        coVerify(exactly = 1) { orderDao.deleteOrder(order) }
    }
}
