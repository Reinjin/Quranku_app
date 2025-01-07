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
import com.google.accompanist.permissions.isGranted
import com.quranku.quranku_app.R
import com.quranku.quranku_app.data.models.HurufHijaiyah
import com.quranku.quranku_app.ui.viewmodel.HomeViewModel
import com.google.accompanist.permissions.rememberPermissionState
import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.shouldShowRationale
import com.quranku.quranku_app.ui.util.Rotate_left
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.quranku.quranku_app.ui.util.PrayerTimesRow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    navControllerUtama : NavController,
    homeViewModel : HomeViewModel = hiltViewModel()
) {
    val hurufHijaiyahList by homeViewModel.hurufHijaiyahLists.collectAsState()

    // Mendapatkan date dan time saat ini
    val currentTime by homeViewModel.currentTime.collectAsState()
    val currentDate by homeViewModel.currentDate.collectAsState()

    val userName by homeViewModel.userName.collectAsState()
    val errorMessageUser by homeViewModel.errorMessageUser.collectAsState()

    val prayerTimes by homeViewModel.prayerTimes.collectAsState()
    val errorMessageTimes by homeViewModel.errorMessageTimes.collectAsState()

    val cityLocation by homeViewModel.cityLocation.collectAsState()
    val erroCityLocation by homeViewModel.errorMessageCity.collectAsState()

    // Inisialisasi izin lokasi
    val context = LocalContext.current
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    var showRationaleDialog by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope() // Membuat CoroutineScope


    // Meluncurkan permintaan izin jika belum disetujui dan memperbarui statusnya
    LaunchedEffect(locationPermissionState.status.isGranted) {
        if (!locationPermissionState.status.isGranted && !showRationaleDialog) {
            locationPermissionState.launchPermissionRequest()
            if(!locationPermissionState.status.isGranted && locationPermissionState.status.shouldShowRationale){
                showRationaleDialog = true
            }
        } else if (locationPermissionState.status.isGranted) {
            showRationaleDialog = false
            // Setelah izin diberikan, panggil loadPrayerTimes
            if (prayerTimes == null) {
                homeViewModel.loadPrayerTimes()
            }
            if (cityLocation == null) {
                homeViewModel.loadCityLocation()
            }

            delay(2000)
            if (errorMessageTimes != null) {
                Toast.makeText(context, "Can't Display PrayTimes: $errorMessageTimes", Toast.LENGTH_SHORT).show()
                homeViewModel.resetErrorMessageTimes()
            }
            if (erroCityLocation != null) {
                Toast.makeText(context, "Can't Display CityLocation: $erroCityLocation", Toast.LENGTH_SHORT).show()
                homeViewModel.resetErrorMessageTimes()
            }
        }
    }

    // Menjalankan pengambilan nama pengguna
    LaunchedEffect(Unit) {
        if (userName == null) {
            homeViewModel.fetchUserName()
        }
        delay(2000)
        if (errorMessageUser != null) {
            Toast.makeText(context, "Can't Display Username: $errorMessageUser", Toast.LENGTH_SHORT).show()
            homeViewModel.resetErrorMessageUser()
        }
    }


    // Menampilkan dialog saat izin ditolak permanen
    if (showRationaleDialog) {
        AlertDialog(
            containerColor = Color.White,
            textContentColor = Color.Black,
            onDismissRequest = { showRationaleDialog = false },
            title = { Text(stringResource(R.string.title_alert_dialog)) },
            text = {Text(
                text = buildAnnotatedString {
                    append(stringResource(R.string.alert_dialog))
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(stringResource(R.string.alert_dialog1))
                    }
                    append(stringResource(R.string.alert_dialog2))
                }
            )},
            confirmButton = {
                TextButton(onClick = {
                    // Buka pengaturan aplikasi
                    context.startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.packageName, null)
                        )
                    )
                    showRationaleDialog = false
                }) {
                    Text("Buka Pengaturan", color = colorResource(id = R.color.blue_dark))
                }
            },
            dismissButton = {
                TextButton(onClick = { showRationaleDialog = false }) {
                    Text("Batal", color = colorResource(id = R.color.blue_dark))
                }
            }
        )
    }


    Scaffold { paddingValues ->
        // Membungkus Column dengan Box agar padding luar memiliki warna putih
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White) // Warna background putih di sekelilingnya
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp ) // Padding luar untuk Box agar ada ruang putih di sekeliling
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
                        .padding(bottom = 20.dp, top = 28.dp, start = 8.dp)
                )
                Text(
                    text = "   Assalamu'alaikum",
                    fontSize = 18.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "   ${userName ?: "-----"}",
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
                                text = "${cityLocation?.city ?: "---"}, $currentDate",
                                fontSize = 14.sp,
                                color = Color.Black
                            )

                            // Text di sebelah kanan
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val image = Rotate_left
                                IconButton(
                                    onClick = {
                                    if (!locationPermissionState.status.isGranted && !showRationaleDialog) {
                                        locationPermissionState.launchPermissionRequest()
                                        if(!locationPermissionState.status.isGranted && !locationPermissionState.status.shouldShowRationale){
                                            showRationaleDialog = true
                                        }
                                    } else if (locationPermissionState.status.isGranted) {
                                        showRationaleDialog = false
                                        homeViewModel.resetNameTimesAndCity()
                                        // Setelah izin diberikan, panggil loadPrayerTimes
                                        homeViewModel.loadPrayerTimes()
                                        homeViewModel.fetchUserName()
                                        homeViewModel.loadCityLocation()
                                        coroutineScope.launch{
                                            delay(2000)
                                            if (errorMessageTimes != null) {
                                                Toast.makeText(context, "Can't Display PrayTimes: $errorMessageTimes", Toast.LENGTH_SHORT).show()
                                                homeViewModel.resetErrorMessageTimes()
                                            }
                                            if (errorMessageUser != null) {
                                                Toast.makeText(context, "Can't Display Username: $errorMessageUser", Toast.LENGTH_SHORT).show()
                                                homeViewModel.resetErrorMessageUser()
                                            }
                                            if (erroCityLocation != null) {
                                                Toast.makeText(context, "Can't Display CityLocation: $erroCityLocation", Toast.LENGTH_SHORT).show()
                                                homeViewModel.resetErrorMessageCity()
                                            }
                                        }
                                    }
                                    },
                                    modifier = Modifier.size(20.dp),
                                ) {
                                    Icon(
                                        imageVector = image,
                                        contentDescription = "Load Prayer Times",
                                        modifier = Modifier.size(20.dp), // Sesuaikan ukuran ikon
                                        tint = colorResource(id = R.color.blue_dark)
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp)) // Spacer untuk jarak antara ikon dan teks

                                Text(
                                    text = currentTime,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colorResource(id = R.color.blue_dark)
                                )
                            }

                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        PrayerTimesRow(currentTime, prayerTimes)
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
                HijaiyahGrid(
                    hurufHijaiyahList = hurufHijaiyahList,
                    navController = navControllerUtama  // Pass navController ke Grid
                )
            }
        }
    }
}

@Composable
fun HijaiyahGrid(
    hurufHijaiyahList: List<HurufHijaiyah>,
    navController: NavController
) {
    Column {
        hurufHijaiyahList.chunked(4).forEach { rowItems ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                rowItems.forEach { huruf ->
                    HijaiyahButton(
                        huruf = huruf,
                        onClick = {
                            navController.navigate("detail_huruf/${huruf.id}")
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun HijaiyahButton(
    huruf: HurufHijaiyah,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = huruf.huruf,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.blue_dark),
                textAlign = TextAlign.Center
            )
        }
    }
}


// Preview function
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        navControllerUtama = rememberNavController()
    )
}







