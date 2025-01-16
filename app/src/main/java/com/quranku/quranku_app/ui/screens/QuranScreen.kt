package com.quranku.quranku_app.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.quranku.quranku_app.R
import com.quranku.quranku_app.data.models.Surah
import com.quranku.quranku_app.data.models.Verse
import com.quranku.quranku_app.ui.viewmodel.QuranViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranScreen(
    navController: NavController = rememberNavController(),
    quranViewModel: QuranViewModel = hiltViewModel()
) {
    val surahList by quranViewModel.surahList.collectAsState()
    val isLoading by quranViewModel.loadingState.collectAsState()
    val errorState by quranViewModel.errorState.collectAsState()

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp, end = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.wrapContentSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Surah",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(id = R.color.blue_dark),
                            )
                        }

                        Box(
                            modifier = Modifier.size(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(
                                onClick = {
                                    navController.navigate("liked_verses")
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Favorite,
                                    contentDescription = "Like Verse List",
                                    tint = colorResource(id = R.color.blue_dark)
                                )
                            }
                        }
                    }
                },
                // Tambahkan warna latar belakang untuk TopAppBar
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White, // Warna background TopAppBar putih
                    titleContentColor = colorResource(id = R.color.blue_dark) // Warna teks
                ),
                modifier = Modifier.height(45.dp).shadow(4.dp)  // Sesuaikan tinggi total AppBar
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
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
                else -> {
                    SurahList(
                        surahList = surahList,
                        onSurahClick = { surahId, surahTitle, verses ->
                            navController.navigate("detail_surah/$surahId")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SurahList(
    surahList: List<Surah>,
    onSurahClick: (Int, String, List<Verse>) -> Unit
) {
    // Log untuk debugging
    //Log.d("SurahList", "Jumlah surah: ${surahList.size}")

    if (surahList.isEmpty()) {
        Text(
            "Tidak ada data tersedia",
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = surahList,
                key = { surah -> surah.id } // Tambahkan key untuk optimasi
            ) { surah ->
                SurahItem(
                    number = surah.id.toString(),
                    title = surah.transliteration,
                    translation = surah.translation,
                    ayahCount = "${surah.total_verses} Ayat",
                    arabicTitle = surah.name,
                    onClick = {
                        onSurahClick(surah.id, surah.transliteration, surah.verses)
                    }
                )
            }
            item{
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(bottom = 10.dp)
                )
            }
        }
    }
}

@Composable
fun SurahItem(
    number: String,
    title: String,
    translation : String,
    ayahCount: String,
    arabicTitle: String,
    onClick: () -> Unit
) {
    val limitedTitle = ("$title - $translation").let { fullText ->
        if (fullText.length > 23) {
            fullText.substring(0, 23) + ".."
        } else {
            fullText
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(0.88f)
            .height(80.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.white)
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Surah Number and Info
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = number,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.blue_dark_light)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = limitedTitle,
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.blue_dark_light)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = ayahCount,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.black)
                    )
                }
            }
            // Arabic Title
            Text(
                text = arabicTitle,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.blue_dark_light),
                modifier = Modifier.align(
                    Alignment.CenterVertically
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuranScreenPreview(){
    QuranScreen()
}