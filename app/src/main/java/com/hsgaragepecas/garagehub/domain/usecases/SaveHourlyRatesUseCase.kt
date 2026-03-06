package com.hsgaragepecas.garagehub.domain.usecases

import javax.inject.Inject

class SaveHourlyRatesUseCase @Inject constructor() {

    sealed class Result {
        object Success : Result()
        object EmptyMechanicsRate : Result()
        object InvalidMechanicsRate : Result()
        object EmptyPaintingRate : Result()
        object InvalidPaintingRate : Result()
    }

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
