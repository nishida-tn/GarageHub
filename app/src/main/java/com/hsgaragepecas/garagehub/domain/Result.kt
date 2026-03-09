package com.hsgaragepecas.garagehub.domain

/**
 * A sealed class that represents the result of an operation.
 *
 * @param T The type of the data.
 */
sealed class Result<out T> {
    /**
     * Represents a successful result.
     *
     * @param data The data.
     */
    data class Success<out T>(val data: T) : Result<T>()

    /**
     * Represents an error result.
     *
     * @param exception The exception.
     */
    data class Error(val exception: Exception) : Result<Nothing>()
}
