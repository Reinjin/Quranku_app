package com.quranku.quranku_app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.quranku.quranku_app.SplashScreen
import com.quranku.quranku_app.ui.screens.AboutScreen
import com.quranku.quranku_app.ui.screens.DetailHurufScreen
import com.quranku.quranku_app.ui.screens.DetailSurahScreen
import com.quranku.quranku_app.ui.screens.LikedVersesScreen
import com.quranku.quranku_app.ui.screens.LoginScreen
import com.quranku.quranku_app.ui.screens.MainScreen
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

        composable("main") { MainScreen(navControllerUtama = navController) }

        composable("about") { AboutScreen() }

        composable(
            route = "detail_surah/{surahId}",
            arguments = listOf(navArgument("surahId") { type = NavType.IntType })
        ) { backStackEntry ->
            val surahId = backStackEntry.arguments?.getInt("surahId") ?: return@composable
            DetailSurahScreen(
                surahId = surahId,
                onBackClick = { navController.navigateUp() }
            )
        }

        composable(
            route = "detail_huruf/{hurufId}",
            arguments = listOf(navArgument("hurufId") { type = NavType.IntType })
        ) { backStackEntry ->
            val hurufId = backStackEntry.arguments?.getInt("hurufId") ?: return@composable
            DetailHurufScreen(
                hurufId = hurufId,
                onBackClick = { navController.navigateUp() }
            )
        }

        composable("liked_verses"){
            LikedVersesScreen(
                onBackClick = { navController.navigateUp() }
            )
        }

    }
}