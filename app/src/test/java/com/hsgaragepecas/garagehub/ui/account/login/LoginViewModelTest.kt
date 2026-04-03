package com.hsgaragepecas.garagehub.ui.account.login

import com.hsgaragepecas.garagehub.domain.usecases.LoginUseCase
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var loginUseCase: LoginUseCase
    private lateinit var viewModel: LoginViewModelImpl

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        loginUseCase = mockk()
        viewModel = LoginViewModelImpl(loginUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() = runTest {
        val initialState = viewModel.uiState.value
        assert(initialState == LoginContract.LoginUiState())
    }
}
