package com.hsgaragepecas.garagehub.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class AppNavigation

@Serializable
data object Splash : AppNavigation()

@Serializable
data object Login : AppNavigation()

@Serializable
data object Main : AppNavigation()

@Serializable
data object CreateAccount : AppNavigation()

@Serializable
data object ForgotPassword : AppNavigation()

@Serializable
data object Settings : AppNavigation()

@Serializable
data object ListOrder : AppNavigation()

@Serializable
data object ListEstimate : AppNavigation()

@Serializable
data object CreateEstimate : AppNavigation()

@Serializable
data class EditEstimate(val estimateId: Int) : AppNavigation()
