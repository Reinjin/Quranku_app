package com.quranku.quranku_app.ui.screens

import android.app.Activity
import com.quranku.quranku_app.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.quranku.quranku_app.ui.navigation.BottomNavBar2


@Composable
fun MainScreen() {
    val navController = rememberNavController()

    // Mengatur warna status bar
    val context = LocalContext.current
    SideEffect {
        val activity = context as? Activity
        activity?.let {
            val window = it.window
            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.statusBarColor = Color.White.toArgb() // Atur warna status bar jadi putih
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar2(
                navController = navController,
                modifier = Modifier
                    .padding(bottom = 36.dp)
            )
        }
    ) { innerPadding ->
        // Layer utama untuk warna padding
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = R.color.black))
        ) {
            // Layer untuk konten utama
            Box(
                modifier = Modifier
                    .consumeWindowInsets(innerPadding)
                    .fillMaxSize()
                    .padding(top = 29.dp, bottom = 80.dp)
                    .background(color = Color.White)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") { HomeScreen(navController) }
                    composable("history") { HistoryScreen(navController) }
                    composable("quran") { QuranScreen(navController) }
                    composable("profile") { ProfileScreen(navController) }
                }
            }
        }
    }
}