package com.quranku.quranku_app.ui.screens

import android.app.Activity
import com.quranku.quranku_app.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.quranku.quranku_app.ui.util.Frame_source
import com.quranku.quranku_app.ui.util.Headphones

@Composable
fun AboutScreen() {

    val context = LocalContext.current

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

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
            ){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Spacer(modifier = Modifier.height(60.dp))

                    // Logo Image
                    Image(
                        painter = painterResource(id = R.drawable.logo), // Ganti dengan resource logo Anda
                        contentDescription = "Logo",
                        modifier = Modifier.size(100.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Title
                    Image(
                        painter = painterResource(id = R.drawable.nama_logo), // Ganti dengan resource logo Anda
                        contentDescription = "Logo"
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    // About Section
                    AboutItem(
                        icon = Icons.Outlined.Info, // Ikon "i" untuk About
                        title = "About",
                        content = "Qur'anku adalah aplikasi Pembelajaran membaca huruf hijaiyah dengan baik yang bersifat open source yang bisa digunakan secara gratis."
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Support Section
                    AboutItem(
                        icon = Headphones, // Ikon untuk Support
                        title = "Support",
                        content = "Email : 202151002@std.umk.ac.id",
                        content1 = "Contact : 08522976024"
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Source Section
                    AboutItem(
                        icon = Frame_source, // Ikon untuk Source
                        title = "Source",
                        content = "github.com/Reinjin/Quranku_app"
                    )
                }
            }
        }
    }
}

@Composable
fun AboutItem(icon: ImageVector, title: String, content: String, content1 : String = "") {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Row untuk Icon dan Title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Icon
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = colorResource(id = R.color.blue_dark_light),
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 8.dp)
            )

            // Title
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.blue_dark_light),
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Row untuk Content
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = content,
                fontSize = 18.sp,
                color = Color.Gray,
                textAlign = TextAlign.Justify,
                lineHeight = 24.sp
            )
        }

        if (content1 != "") {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = content1,
                    fontSize = 18.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutScreenPreview() {
    AboutScreen()
}
