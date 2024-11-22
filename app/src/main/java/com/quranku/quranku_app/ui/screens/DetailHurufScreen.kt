package com.quranku.quranku_app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quranku.quranku_app.ui.util.Microphone
import com.quranku.quranku_app.ui.util.Volume_up

@Composable
fun DetailHurufScreen() {
    // State untuk melacak kondisi yang dipilih
    var selectedCondition by remember { mutableStateOf("Kasroh") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back Button and Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color(0xFF2B637B),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Hijaiyah",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF2B637B)
            )
        }

        Spacer(modifier = Modifier.height(80.dp))

        // Hijaiyah Letter Card
        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.size(100.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "ب",
                    fontSize = 80.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF2B637B)
                )
            }
        }

        Spacer(modifier = Modifier.height(80.dp))

        // Condition Buttons (Fathah, Kasroh, Dhommah)
        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(100.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ConditionButton(
                    text = "Fathah",
                    isSelected = selectedCondition == "Fathah",
                    onClick = { selectedCondition = "Fathah" }
                )
                ConditionButton(
                    text = "Kasroh",
                    isSelected = selectedCondition == "Kasroh",
                    onClick = { selectedCondition = "Kasroh" }
                )
                ConditionButton(
                    text = "Dhommah",
                    isSelected = selectedCondition == "Dhommah",
                    onClick = { selectedCondition = "Dhommah" }
                )
            }
        }

        Spacer(modifier = Modifier.height(70.dp))

        // Microphone Button and Text
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(100.dp)
                .background(
                    color = Color(0xFF2B637B),
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Microphone,
                contentDescription = "Microphone",
                tint = Color.White,
                modifier = Modifier.size(50.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Tekan mikrofon dan ucapkan huruf diatas",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Composable
fun ConditionButton(
    text: String,
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
                text = text,
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

@Preview(showBackground = true)
@Composable
fun DetailHurufScreenPreview() {
    DetailHurufScreen()
}