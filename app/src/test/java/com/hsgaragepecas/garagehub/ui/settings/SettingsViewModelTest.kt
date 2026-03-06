package com.hsgaragepecas.garagehub.ui.settings

import com.hsgaragepecas.garagehub.domain.usecases.CheckPasswordUseCase
import com.hsgaragepecas.garagehub.domain.usecases.SaveHourlyRatesUseCase
import com.hsgaragepecas.garagehub.domain.usecases.SaveSettingsUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SettingsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var saveSettingsUseCase: SaveSettingsUseCase
    private lateinit var checkPasswordUseCase: CheckPasswordUseCase
    private lateinit var saveHourlyRatesUseCase: SaveHourlyRatesUseCase
    private lateinit var viewModel: SettingsViewModelImpl

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        saveSettingsUseCase = mockk()
        checkPasswordUseCase = mockk()
        saveHourlyRatesUseCase = mockk()
        viewModel = SettingsViewModelImpl(saveSettingsUseCase, checkPasswordUseCase, saveHourlyRatesUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `saveWorkshopData with valid data should update state to saved`() = runTest {
        // Arrange
        val state = viewModel.uiState.value
        every {
            saveSettingsUseCase(
                fantasyName = state.fantasyName,
                email = state.email,
                cnpj = state.cnpj,
                landline = state.landline,
                whatsapp = state.whatsapp,
                cep = state.cep,
                number = state.number,
                neighborhood = state.neighborhood,
                city = state.city,
                uf = state.uf,
                complement = state.complement,
                logoPath = state.logoPath
            )
        } returns true

        // Act
        viewModel.saveWorkshopData()
        testDispatcher.scheduler.advanceUntilIdle()


        // Assert
        assertEquals(true, viewModel.uiState.value.isWorkshopDataSaved)
        assertEquals(null, viewModel.uiState.value.workshopDataError)
    }

    @Test
    fun `saveWorkshopData with invalid data should update state with error`() = runTest {
        // Arrange
        val state = viewModel.uiState.value
        every {
            saveSettingsUseCase(
                fantasyName = state.fantasyName,
                email = state.email,
                cnpj = state.cnpj,
                landline = state.landline,
                whatsapp = state.whatsapp,
                cep = state.cep,
                number = state.number,
                neighborhood = state.neighborhood,
                city = state.city,
                uf = state.uf,
                complement = state.complement,
                logoPath = state.logoPath
            )
        } returns false

        // Act
        viewModel.saveWorkshopData()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(false, viewModel.uiState.value.isWorkshopDataSaved)
        assertEquals("Invalid data", viewModel.uiState.value.workshopDataError)
    }

    @Test
    fun `changePassword with valid passwords should update state to changed`() = runTest {
        // Arrange
        val state = viewModel.uiState.value
        every {
            checkPasswordUseCase(
                currentPassword = state.currentPassword,
                newPassword = state.newPassword,
                confirmPassword = state.confirmPassword,
                storedPassword = "" // Mocked
            )
        } returns CheckPasswordUseCase.PasswordResult.Success

        // Act
        viewModel.changePassword()
        testDispatcher.scheduler.advanceUntilIdle()


        // Assert
        assertEquals(true, viewModel.uiState.value.isPasswordChanged)
        assertEquals(null, viewModel.uiState.value.passwordError)
    }

    @Test
    fun `changePassword with invalid password should update state with error`() = runTest {
        // Arrange
        val state = viewModel.uiState.value
        every {
            checkPasswordUseCase(
                currentPassword = state.currentPassword,
                newPassword = state.newPassword,
                confirmPassword = state.confirmPassword,
                storedPassword = "" // Mocked
            )
        } returns CheckPasswordUseCase.PasswordResult.InvalidCurrentPassword

        // Act
        viewModel.changePassword()
        testDispatcher.scheduler.advanceUntilIdle()


        // Assert
        assertEquals(false, viewModel.uiState.value.isPasswordChanged)
        assertEquals("Invalid current password", viewModel.uiState.value.passwordError)
    }

    @Test
    fun `saveHourlyRates with valid rates should update state to saved`() = runTest {
        // Arrange
        val state = viewModel.uiState.value
        every {
            saveHourlyRatesUseCase(
                mechanicsRate = state.mechanicsRate,
                paintingRate = state.paintingRate
            )
        } returns SaveHourlyRatesUseCase.Result.Success

        // Act
        viewModel.saveHourlyRates()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(true, viewModel.uiState.value.isRatesSaved)
        assertEquals(null, viewModel.uiState.value.ratesError)
    }

    @Test
    fun `saveHourlyRates with invalid rates should update state with error`() = runTest {
        // Arrange
        val state = viewModel.uiState.value
        every {
            saveHourlyRatesUseCase(
                mechanicsRate = state.mechanicsRate,
                paintingRate = state.paintingRate
            )
        } returns SaveHourlyRatesUseCase.Result.InvalidMechanicsRate

        // Act
        viewModel.saveHourlyRates()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(false, viewModel.uiState.value.isRatesSaved)
        assertEquals("Invalid mechanics rate", viewModel.uiState.value.ratesError)
    }
}
