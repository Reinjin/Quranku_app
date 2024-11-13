package com.quranku.quranku_app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.quranku.quranku_app.R
import com.quranku.quranku_app.data.models.HurufHijaiyah
import com.quranku.quranku_app.ui.viewmodel.HomeViewModel



@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel : HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val cityName = "Kudus" // Placeholder, replace with actual city name retrieval
    // Mendapatkan date dan time saat ini
    val currentTime by homeViewModel.currentTime.collectAsState()
    val currentDate by homeViewModel.currentDate.collectAsState()

    val userName by homeViewModel.userName.collectAsState()
    val errorMessage by homeViewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        homeViewModel.fetchUserName()
    }

    Scaffold { paddingValues ->
        // Membungkus Column dengan Box agar padding luar memiliki warna putih
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White) // Warna background putih di sekelilingnya
                .padding(16.dp) // Padding luar untuk Box agar ada ruang putih di sekeliling
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.White) // Background utama di dalam Box
                    .verticalScroll(rememberScrollState())
            ) {
                // Greeting Section
                Image(
                    painter = painterResource(id = R.drawable.nama_logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .padding(bottom = 20.dp, top = 8.dp, start = 8.dp)
                )
                Text(
                    text = "   Assalamu'alaikum",
                    fontSize = 18.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "   $userName",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.blue_dark),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Prayer Times Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Text di sebelah kiri
                            Text(
                                text = "$cityName, $currentDate",
                                fontSize = 14.sp,
                                color = Color.Black
                            )

                            // Text di sebelah kanan
                            Text(
                                text = currentTime,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(id = R.color.blue_dark)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            PrayerTimeColumn("Subuh", "--:--")
                            PrayerTimeColumn("Dzuhur", "--:--")
                            PrayerTimeColumn("Ashar", "--:--")
                            PrayerTimeColumn("Maghrib", "--:--")
                            PrayerTimeColumn("Isya", "--:--")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Hijaiyah Section
                Text(
                    text = "Hijaiyah",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.blue_dark),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Belajar Pengucapan Huruf Hijaiyah",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Hijaiyah Grid
                HijaiyahGrid(hurufHijaiyahList = homeViewModel.hurufHijaiyahLists)
            }
        }
    }
}

@Composable
fun PrayerTimeColumn(name: String, time: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = name, fontSize = 12.sp, color = Color.Black)
        Text(text = time, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
    }
}

@Composable
fun HijaiyahGrid(hurufHijaiyahList: List<HurufHijaiyah>) {
    Column {
        hurufHijaiyahList.chunked(4).forEach { rowItems ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                rowItems.forEach { huruf ->
                    HijaiyahButton(huruf = huruf)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun HijaiyahButton(huruf: HurufHijaiyah) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Button(
            onClick = { /* Handle click here */ },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = colorResource(id = R.color.blue_dark)
            ),
            modifier = Modifier
                .size(60.dp)
                .background(Color.White)
        ) {
            Text(text = huruf.huruf, fontSize = 28.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center )
        }
    }
}

// Preview function
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        navController = rememberNavController()
    )
}







