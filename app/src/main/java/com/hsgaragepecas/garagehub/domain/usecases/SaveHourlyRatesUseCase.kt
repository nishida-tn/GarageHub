package com.hsgaragepecas.garagehub.domain.usecases

import javax.inject.Inject

/**
 * A use case that saves the hourly rates.
 */
class SaveHourlyRatesUseCase @Inject constructor() {

    /**
     * The result of saving the hourly rates.
     */
    sealed class Result {
        /**
         * The hourly rates were saved successfully.
         */
        object Success : Result()
        /**
         * The mechanics rate is empty.
         */
        object EmptyMechanicsRate : Result()
        /**
         * The mechanics rate is invalid.
         */
        object InvalidMechanicsRate : Result()
        /**
         * The painting rate is empty.
         */
        object EmptyPaintingRate : Result()
        /**
         * The painting rate is invalid.
         */
        object InvalidPaintingRate : Result()
    }

    /**
     * Invokes the use case.
     *
     * @param mechanicsRate The mechanics rate.
     * @param paintingRate The painting rate.
     * @return The result of saving the hourly rates.
     */
    operator fun invoke(mechanicsRate: String, paintingRate: String): Result {
        if (mechanicsRate.isBlank()) {
            return Result.EmptyMechanicsRate
        }
        if (paintingRate.isBlank()) {
            return Result.EmptyPaintingRate
        }

        val mechanicsValue = mechanicsRate.parseToDouble()
        if (mechanicsValue == null || mechanicsValue <= 0) {
            return Result.InvalidMechanicsRate
        }

        val paintingValue = paintingRate.parseToDouble()
        if (paintingValue == null || paintingValue <= 0) {
            return Result.InvalidPaintingRate
        }

        return Result.Success
    }

    private fun String.parseToDouble(): Double? {
        return this.replace(",", ".").toDoubleOrNull()
    }
}
