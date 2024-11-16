package com.quranku.quranku_app.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import com.quranku.quranku_app.R
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


@Composable
fun QuranScreen(
    navController: NavController = rememberNavController()
) {
    Scaffold { paddingValues ->
        // Gunakan Box atau Column untuk menempatkan konten di dalam Scaffold
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Menerapkan paddingValues dari Scaffold
                .padding(top = 30.dp) // Padding tambahan di atas
        ) {
            Text(
                text = "Quran Screen",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.blue_dark),
                modifier = Modifier.align(Alignment.Center) // Mengatur teks di tengah layar
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuranScreenPreview(){
    QuranScreen()
}