package com.hsgaragepecas.garagehub.ui.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hsgaragepecas.garagehub.navigation.Login
import com.hsgaragepecas.garagehub.navigation.Main
import com.hsgaragepecas.garagehub.navigation.Splash

@Composable
fun SplashScreen(navController: NavController) {
    val viewModel: SplashViewModel = hiltViewModel()
    val isUserLoggedIn by viewModel.isUserLoggedIn.collectAsState()

    LaunchedEffect(isUserLoggedIn) {
        isUserLoggedIn?.let {
            if (it) {
                navController.navigate(Main) {
                    popUpTo(Splash) { inclusive = true }
                }
            } else {
                navController.navigate(Login) {
                    popUpTo(Splash) { inclusive = true }
                }
            }
        }
    }
}
