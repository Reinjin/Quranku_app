package com.quranku.quranku_app.ui.screens

import com.quranku.quranku_app.R
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp


@Composable
fun HistoryScreen(){
    Text(
        text = "History Screen",
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        color = colorResource(id = R.color.blue_dark)
    )
}


@Preview
@Composable
fun HistoryScreenPreview(){
    HistoryScreen()
}