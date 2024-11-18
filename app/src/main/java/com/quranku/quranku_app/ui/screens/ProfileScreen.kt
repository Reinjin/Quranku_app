package com.quranku.quranku_app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.quranku.quranku_app.R
import com.quranku.quranku_app.ui.util.UserCircle
import com.quranku.quranku_app.ui.viewmodel.ProfileViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun ProfileScreen(
    navController: NavController = rememberNavController(),
    profileViewModel: ProfileViewModel = hiltViewModel()
) {

    val userName by profileViewModel.userName.collectAsState()
    val email by profileViewModel.email.collectAsState()
    val errorMessageUser by profileViewModel.errorMessage.collectAsState()

    val loadingState by profileViewModel.loadingState.collectAsState()
    val logoutState by profileViewModel.logoutState.collectAsState()
    val errorMessageLogout by profileViewModel.errorMessageLogout.collectAsState()

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope() // Membuat CoroutineScope

    // Menjalankan pengambilan nama pengguna
    LaunchedEffect(Unit) {
        if (userName == null) {
            profileViewModel.fetchUserProfile()
        }
        delay(1500)
        if (errorMessageUser != null) {
            Toast.makeText(context, "Can't Display Username: $errorMessageUser", Toast.LENGTH_SHORT).show()
            profileViewModel.resetErrorMessage()
        }
    }

    LaunchedEffect(logoutState) {
        logoutState?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            if (message == "Successfully logged out") {
                navController.navigate("welcome") {
                    popUpTo("main") {inclusive = true}
                }
            }
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Title
            Text(
                text = "Profile",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.blue_dark)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Profile Image Placeholder
            Box(
                modifier = Modifier
                    .size(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = UserCircle,
                    contentDescription = "Profile Picture",
                    tint = colorResource(id = R.color.blue_grey),
                    modifier = Modifier.size(120.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // User Name
            Text(
                text = userName ?: "-----",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.black)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // User Email
            Text(
                text = email ?: "-------",
                fontSize = 16.sp,
                color = colorResource(id = R.color.black),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(50.dp))

            // Buttons
            ProfileButton(text = "About") {
                navController.navigate("about")
            }

            Spacer(modifier = Modifier.height(24.dp))

            ProfileButton(text = "Logout") {
                profileViewModel.logout()
                coroutineScope.launch{
                    delay(1500)
                    if (errorMessageLogout != null) {
                        Toast.makeText(context, "Can't Logout: $errorMessageLogout", Toast.LENGTH_SHORT).show()
                        profileViewModel.resetErrorMessageLogout()
                    }
                }
            }
        }
        if (loadingState) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x80000000)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = colorResource(id = R.color.blue_dark_light)
                )
            }
        }
    }
}

@Composable
fun ProfileButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .height(48.dp)
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            color = colorResource(id = R.color.blue_dark),
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}
