package com.quranku.quranku_app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.quranku.quranku_app.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column {
        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        TextField(value = password, onValueChange = { password = it }, label = { Text("Password") })
        Button(onClick = {
            authViewModel.login("sample_token") // Replace with actual token from API
            navController.navigate("home") { popUpTo("welcome") { inclusive = true } }
        }) {
            Text("Login")
        }
    }
}
