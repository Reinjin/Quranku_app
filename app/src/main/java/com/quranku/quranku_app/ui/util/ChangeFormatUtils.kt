package com.quranku.quranku_app.ui.util

import java.text.SimpleDateFormat
import java.util.Locale

fun formatTanggal(tanggal: String): String {
    // Format asli tanggal (input)
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
    // Format yang diinginkan (output)
    val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))

    return try {
        // Parse tanggal dari format input ke Date
        val date = inputFormat.parse(tanggal)
        // Format tanggal ke format output
        outputFormat.format(date!!)
    } catch (e: Exception) {
        "Format tanggal salah"
    }
}

fun capitalizeFirstLetter(word: String): String {
    return word.lowercase().replaceFirstChar {
        if (it.isLowerCase()) it.titlecase() else it.toString()
    }
}