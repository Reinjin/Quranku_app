package com.quranku.quranku_app

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.quranku.quranku_app.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController, authViewModel: AuthViewModel) {
    // Menggunakan LaunchedEffect untuk menjalankan logika hanya sekali saat SplashScreen dibuka
    LaunchedEffect(Unit) {
        delay(1000) // Menunggu selama 1 detik (bisa disesuaikan sesuai kebutuhan)

        // Cek status login dan arahkan ke layar yang sesuai
        if (authViewModel.isLoggedIn()) {
            navController.navigate("main") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            navController.navigate("welcome") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    // Layout Splash Screen
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo Image
            Image(
                painter = painterResource(id = R.drawable.logo), // Replace with your actual logo resource
                contentDescription = "Logo",
                modifier = Modifier.size(170.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nama Logo Image
            Image(
                painter = painterResource(id = R.drawable.nama_logo), // Replace with your actual name logo resource
                contentDescription = "Nama Logo",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 40.dp)
            )
        }
    }
}
