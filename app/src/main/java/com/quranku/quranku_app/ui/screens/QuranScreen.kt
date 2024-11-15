package com.quranku.quranku_app.ui.screens

import androidx.compose.foundation.layout.padding
import com.quranku.quranku_app.R
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun QuranScreen(){
    Text(
        text = "Quran Screen",
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        color = colorResource(id = R.color.blue_dark),
        modifier = Modifier.padding(top = 30.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun QuranScreenPreview(){
    QuranScreen()
}