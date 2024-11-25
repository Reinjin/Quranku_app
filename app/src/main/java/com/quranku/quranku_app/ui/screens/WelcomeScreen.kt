package com.quranku.quranku_app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.quranku.quranku_app.R

@Composable
fun WelcomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo Image
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(170.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.nama_logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(150.dp)
                .padding(bottom = 40.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Subtitle
        Text(
            text = "Let's start our Learning journey towards better Muslim",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 32.dp).padding(bottom = 26.dp)
        )

        Spacer(modifier = Modifier.height(38.dp))

        // "Getting Started" Button
        Button(
            onClick = { navController.navigate("register") },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(48.dp),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.blue_dark),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Getting Started",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // "Already have an account?" Text
        @Suppress("DEPRECATION")
        ClickableText(
            text = AnnotatedString("Already have an account?"),
            onClick = { navController.navigate("login") },
            style = TextStyle(
                color = Color(0xFF1B1B1E),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen(navController = rememberNavController())
}