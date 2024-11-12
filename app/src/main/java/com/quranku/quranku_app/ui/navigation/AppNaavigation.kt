package com.quranku.quranku_app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.quranku.quranku_app.SplashScreen
import com.quranku.quranku_app.ui.screens.HomeScreen
import com.quranku.quranku_app.ui.screens.LoginScreen
import com.quranku.quranku_app.ui.screens.RegisterScreen
import com.quranku.quranku_app.ui.screens.WelcomeScreen
import com.quranku.quranku_app.ui.viewmodel.AuthViewModel

@Composable
fun AppNavigation(authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash" // Set splash as the start destination
    ) {
        composable("splash") { SplashScreen(navController, authViewModel) }
        composable("welcome") { WelcomeScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("home") { HomeScreen(navController) }
    }
}