package com.quranku.quranku_app.ui.screens

import HistoryScreen
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.quranku.quranku_app.ui.navigation.BottomNavBar2
import com.quranku.quranku_app.ui.viewmodel.MainViewModel
import kotlinx.coroutines.delay


@Composable
fun MainScreen(
    viewModel : MainViewModel = hiltViewModel(),
    navControllerUtama: NavController
) {
    val navController = rememberNavController()

    val context = LocalContext.current
    val navigationMode by viewModel.navigationMode.collectAsState(initial = true)

    // Mengatur warna status bar
    SideEffect {
        val activity = context as? Activity
        activity?.let {
            val window = it.window
            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.statusBarColor = Color.White.toArgb() // Atur warna status bar jadi putih
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        }
    }

    // Update ViewModel state saat Composable dimuat
    LaunchedEffect(Unit) {
        delay(500)  // Delay agar menunggu construct ulanga android sistemnya
        viewModel.updateNavigationMode(context)
    }

    Scaffold(
        bottomBar = {
            BottomNavBar2(
                navController = navController,
                modifier = Modifier
                    .padding(bottom = if (navigationMode) 40.dp else 10.dp)
            )
        }
    ) { innerPadding ->
        // Layer utama untuk warna padding
        Box(
            modifier = Modifier
                .padding(bottom = 0.dp)
                .background(if (navigationMode) Color.Black else Color.White)
        ) {
            // Layer untuk konten utama
            Box(
                modifier = Modifier
                    .consumeWindowInsets(innerPadding)
                    .fillMaxSize()
                    .padding(
                        top = 24.dp,
                        bottom = if (navigationMode) 80.dp else 50.dp,
                    )
            ) {

                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") { HomeScreen(navControllerUtama) }
                    composable("history") { HistoryScreen() }
                    composable("quran") { QuranScreen(navControllerUtama) }
                    composable("profile") { ProfileScreen(navControllerUtama) }
                }
            }
        }
    }
}