package com.hsgaragepecas.garagehub.domain.usecases

import com.hsgaragepecas.garagehub.domain.usecases.SaveHourlyRatesUseCase.Result
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SaveHourlyRatesUseCaseTest {

    private lateinit var useCase: SaveHourlyRatesUseCase

    @Before
    fun setUp() {
        useCase = SaveHourlyRatesUseCase()
    }

    @Test
    fun `invoke with valid values should return success`() {
        val result = useCase(mechanicsRate = "80,00", paintingRate = "100,50")
        assertEquals(Result.Success, result)
    }

    @Test
    fun `invoke with valid dot-separated values should return success`() {
        val result = useCase(mechanicsRate = "80.00", paintingRate = "100.50")
        assertEquals(Result.Success, result)
    }

    @Test
    fun `invoke with empty mechanics rate should return empty mechanics rate error`() {
        val result = useCase(mechanicsRate = "", paintingRate = "100.50")
        assertEquals(Result.EmptyMechanicsRate, result)
    }

    @Test
    fun `invoke with empty painting rate should return empty painting rate error`() {
        val result = useCase(mechanicsRate = "80.00", paintingRate = "")
        assertEquals(Result.EmptyPaintingRate, result)
    }

    @Test
    fun `invoke with invalid mechanics rate should return invalid mechanics rate error`() {
        val result = useCase(mechanicsRate = "abc", paintingRate = "100.50")
        assertEquals(Result.InvalidMechanicsRate, result)
    }

    @Test
    fun `invoke with invalid painting rate should return invalid painting rate error`() {
        val result = useCase(mechanicsRate = "80.00", paintingRate = "abc")
        assertEquals(Result.InvalidPaintingRate, result)
    }

    @Test
    fun `invoke with zero mechanics rate should return invalid mechanics rate error`() {
        val result = useCase(mechanicsRate = "0", paintingRate = "100.50")
        assertEquals(Result.InvalidMechanicsRate, result)
    }

    @Test
    fun `invoke with negative painting rate should return invalid painting rate error`() {
        val result = useCase(mechanicsRate = "80.00", paintingRate = "-100")
        assertEquals(Result.InvalidPaintingRate, result)
    }
}
