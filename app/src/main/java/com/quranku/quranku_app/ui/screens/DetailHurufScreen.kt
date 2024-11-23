package com.quranku.quranku_app.ui.screens

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.quranku.quranku_app.R
import com.quranku.quranku_app.ui.util.Microphone
import com.quranku.quranku_app.ui.util.Volume_up
import com.quranku.quranku_app.ui.viewmodel.DetailHurufViewModel

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
    val context = LocalContext.current

    // Audio Permission State
    val audioPermissionState = rememberPermissionState(
        Manifest.permission.RECORD_AUDIO
    )
    var showRationaleDialog by remember { mutableStateOf(false) }

    // Handle recording result
    LaunchedEffect(recordingResult) {
        recordingResult?.fold(
            onSuccess = { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            },
            onFailure = { error ->
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        )
    }

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
                TextButton(onClick = {
                    context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    })
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Huruf Display
            selectedHuruf?.let { huruf ->
                Card(
                    modifier = Modifier.size(100.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = huruf.huruf,
                            fontSize = 80.sp,
                            color = colorResource(id = R.color.blue_dark)
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
                        onClick = { viewModel.setKondisi(kondisi) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Record Button with Permission Check
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        color = if (isRecording) Color.Red else Color(0xFF2B637B),
                        shape = CircleShape
                    )
                    .clickable {
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
                    imageVector = Microphone,
                    contentDescription = if (isRecording) "Stop Recording" else "Start Recording",
                    tint = Color.White,
                    modifier = Modifier.size(50.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Instruction Text
            Text(
                text = "Tekan mikrofon dan ucapkan huruf diatas",
                fontSize = 14.sp,
                color = Color.Gray
            )

            // Recording Result Display
            recordingResult?.let { result ->
                result.fold(
                    onSuccess = { message ->
                        Text(
                            text = message,
                            color = Color.Green,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    },
                    onFailure = { error ->
                        Text(
                            text = error.message ?: "Error occurred",
                            color = Color.Red,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun KondisiButton(
    kondisi: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isSelected) Color(0xFF2B637B) else Color.White,
                contentColor = if (isSelected) Color.White else Color(0xFF2B637B)
            ),
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .width(90.dp)
                .height(36.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp
            ),
            border = BorderStroke(
                1.dp,
                Color(0xFF2B637B)
            ),
            contentPadding = PaddingValues(
                horizontal = 4.dp,
                vertical = 0.dp
            )
        ) {
            Text(
                text = kondisi.replaceFirstChar { it.uppercase() },
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Icon(
            imageVector = Volume_up,
            contentDescription = "Sound",
            tint = Color(0xFF2B637B),
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview
@Composable
private fun DetailHurufScreenPreview() {
    DetailHurufScreen(
        hurufId = 1,
        onBackClick = {}
    )
}