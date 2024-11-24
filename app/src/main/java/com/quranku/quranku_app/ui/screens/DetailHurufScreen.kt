package com.quranku.quranku_app.ui.screens

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.quranku.quranku_app.R
import com.quranku.quranku_app.ui.util.Microphone
import com.quranku.quranku_app.ui.util.Stop_circle
import com.quranku.quranku_app.ui.util.Volume_up
import com.quranku.quranku_app.ui.viewmodel.DetailHurufViewModel

@Suppress("DEPRECATION")
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DetailHurufScreen(
    hurufId: Int,
    viewModel: DetailHurufViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val selectedHuruf by viewModel.selectedHuruf.collectAsState()
    val selectedKondisi by viewModel.selectedKondisi.collectAsState()
    val isRecording by viewModel.isRecording.collectAsState()
    val recordingResult by viewModel.recordingResult.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current

    // Audio Permission State
    val audioPermissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)
    var showRationaleDialog by remember { mutableStateOf(false) }

    val showResultSheet by viewModel.showResultSheet.collectAsState()

    LaunchedEffect(hurufId) {
        viewModel.setHuruf(hurufId)
    }

    // Permission Dialog
    if (showRationaleDialog) {
        AlertDialog(
            containerColor = Color.White,
            textContentColor = Color.Black,
            onDismissRequest = { showRationaleDialog = false },
            title = { Text("Izin Mikrofon Diperlukan") },
            text = {
                Text(
                    "Aplikasi memerlukan izin untuk mengakses mikrofon agar dapat merekam suara. " +
                            "Silakan berikan izin di pengaturan aplikasi."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        })
                        showRationaleDialog = false
                    }
                ) {
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hijaiyah") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Main Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(color = Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Huruf Display
                selectedHuruf?.let { huruf ->
                    Card(
                        modifier = Modifier
                            .size(120.dp)
                            .padding(8.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = Color.White)
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = huruf.huruf,
                                fontSize = 72.sp,
                                color = colorResource(id = R.color.blue_dark),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.wrapContentSize(align = Alignment.Center)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Kondisi Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("fathah", "kasroh", "dommah").forEach { kondisi ->
                        KondisiButton(
                            kondisi = kondisi,
                            isSelected = selectedKondisi == kondisi,
                            onClick = { viewModel.setKondisi(kondisi) },
                            viewModel = viewModel
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Record Button with Permission Check
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(
                            color = when {
                                isLoading -> Color.Gray
                                isRecording -> Color(0xFFBB2B2B)
                                else -> Color(0xFF2B637B)
                            },
                            shape = CircleShape
                        )
                        .clickable(enabled = !isLoading) {
                            when {
                                audioPermissionState.status.isGranted -> {
                                    if (isRecording) {
                                        viewModel.stopRecording()
                                    } else {
                                        viewModel.startRecording()
                                    }
                                }

                                audioPermissionState.status.shouldShowRationale -> {
                                    showRationaleDialog = true
                                }

                                else -> {
                                    audioPermissionState.launchPermissionRequest()
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isRecording) Stop_circle else Microphone,
                        contentDescription = if (isRecording) "Stop Recording" else "Start Recording",
                        tint = Color.White,
                        modifier = Modifier.size(50.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Instruction Text
                Text(
                    text = when {
                        isLoading -> "Memproses rekaman..."
                        isRecording -> "Tekan untuk menghentikan rekaman"
                        else -> "Tekan mikrofon dan ucapkan huruf diatas"
                    },
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = colorResource(id = R.color.blue_dark)
            )
        }
    }

    // Result Bottom Sheet
    if (showResultSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.hideResultSheet() },
            containerColor = Color.White,
            sheetState = rememberModalBottomSheetState()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                recordingResult?.fold(
                    onSuccess = { message ->
                        Text(
                            text = if(message == "benar") "Benar" else "Belum Sempurna",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Image(
                            painter = if (message == "benar") painterResource(id = R.drawable.correct_answer_icon) else painterResource(id = R.drawable.wrong_answer_icon),
                            contentDescription = if (message == "benar") "Correct Answer Icon" else "Wrong Answer Icon",
                            modifier = Modifier.size(80.dp)
                        )
                        Text(
                            text = if (message == "benar") stringResource(R.string.correct_message) else stringResource(R.string.wrong_message),
                            fontSize = 16.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    },
                    onFailure = { error ->
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = "Gagal Melakukan Prediksi",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red
                        )
                        Text(
                            text = error.message ?: "Maaf Terjadi kesalahan, Silahkan Ulang Kembali",
                            fontSize = 16.sp,
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                    }
                )

                Button(
                    onClick = { viewModel.hideResultSheet() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.blue_dark)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Text("Tutup", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun KondisiButton(
    kondisi: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    viewModel: DetailHurufViewModel
) {
    val selectedHuruf by viewModel.selectedHuruf.collectAsState()
    val playingFile by viewModel.isPlaying.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val currentFile = selectedHuruf?.kondisiKelas?.get(kondisi)
    val isPlayingThisFile = playingFile == currentFile

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onClick,
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isSelected) Color(0xFF2B637B) else Color.White,
                contentColor = if (isSelected) Color.White else Color(0xFF2B637B)
            ),
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .width(90.dp)
                .height(36.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
            border = BorderStroke(1.dp, Color(0xFF2B637B)),
            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
        ) {
            Text(
                text = kondisi.replaceFirstChar { it.uppercase() },
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .size(40.dp)
                .clickable(enabled = !isLoading) {
                    currentFile?.let { fileName ->
                        viewModel.playAudio(fileName)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isPlayingThisFile) Stop_circle else Volume_up,
                contentDescription = if (isPlayingThisFile) "Stop Sound" else "Play Sound",
                tint = if (isLoading) Color.Gray else Color(0xFF2B637B),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}