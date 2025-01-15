package com.quranku.quranku_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.quranku.quranku_app.R
import com.quranku.quranku_app.data.models.Verse
import com.quranku.quranku_app.ui.viewmodel.DetailSurahViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailSurahScreen(
    surahId: Int,
    detailSurahViewModel: DetailSurahViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val surahList by detailSurahViewModel.surahList.collectAsState()
    val isLoading by detailSurahViewModel.loadingState.collectAsState()
    val errorState by detailSurahViewModel.errorState.collectAsState()

    when {
        isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x80000000)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = colorResource(id = R.color.blue_dark_light))
            }
        }

        errorState != null -> {
            Text(
                text = errorState ?: "Terjadi kesalahan",
                color = Color.Red,
                modifier = Modifier.fillMaxSize(),
                textAlign = TextAlign.Center
            )
        }

        surahList.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Data surah belum tersedia.",
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }

        else -> {
            // Ambil data surah berdasarkan `surahId`
            val surah = surahList.find { it.id == surahId }
            if (surah != null) {
                val surahTitle = surah.transliteration
                val verses = surah.verses

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Box(
                                    modifier = Modifier.fillMaxHeight(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = surahTitle,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colorResource(id = R.color.blue_dark)
                                    )
                                }
                            },
                            navigationIcon = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight(),
                                    contentAlignment = Alignment.Center // Pastikan ikon di tengah
                                ) {
                                    IconButton(onClick = onBackClick) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "Back",
                                            tint = colorResource(id = R.color.blue_dark)
                                        )
                                    }
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.White, // Warna background TopAppBar putih
                                titleContentColor = colorResource(id = R.color.blue_dark) // Warna teks
                            ),
                            modifier = Modifier
                                .height(80.dp) // Tinggi AppBar
                                .shadow(4.dp) // Bayangan untuk menambah kedalaman
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black)
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .background(Color.White)
                        ) {
                            LazyColumn(
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(verses) { verse ->
                                    VerseItem(
                                        surahId = surahId,
                                        surahName = surah.transliteration,
                                        verse = verse,
                                        detailSurahViewModel = detailSurahViewModel
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // Jika surah dengan `surahId` tidak ditemukan
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Surah tidak ditemukan.",
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}



@Composable
fun VerseItem(
    surahId: Int,
    surahName: String,
    verse: Verse,
    detailSurahViewModel: DetailSurahViewModel
) {
    val isLiked by detailSurahViewModel.isVerseLiked(surahId, verse.id).collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(colorResource(id = R.color.blue_dark), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = verse.id.toString(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Like Button
                        IconButton(
                            onClick = {
                                detailSurahViewModel.toggleLikeVerse(surahId, surahName, verse)
                            }
                        ) {
                            Icon(
                                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Like Verse",
                                tint = if (isLiked) Color.Red else Color.Gray
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = verse.text,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.blue_dark),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = verse.translation,
                fontSize = 14.sp,
                color = colorResource(id = R.color.blue_dark),
                textAlign = TextAlign.Justify
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailSurahScreenPreview() {
    DetailSurahScreen(
        surahId = 1,
        onBackClick = {}
    )
}
