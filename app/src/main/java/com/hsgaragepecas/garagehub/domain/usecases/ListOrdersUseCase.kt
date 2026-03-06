package com.hsgaragepecas.garagehub.domain.usecases

import com.hsgaragepecas.garagehub.data.local.order.OrderEntity
import com.hsgaragepecas.garagehub.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * A use case that lists all orders.
 *
 * @param orderRepository The repository for orders.
 */
class ListOrdersUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    /**
     * Invokes the use case.
     *
     * @return A flow of all orders.
     */
    operator fun invoke(): Flow<List<OrderEntity>> {
        return orderRepository.getAllOrders()
    }
}
