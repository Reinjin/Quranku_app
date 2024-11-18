package com.quranku.quranku_app.data.models

data class HistoryResponse(
    val history_belajar: List<HistoryItem>,
    val current_page: Int,
    val per_page: Int,
    val total_items: Int,
    val total_pages: Int
)

data class HistoryItem(
    val huruf: String,
    val tanggal: String,
    val waktu: String,
    val kondisi: String,
    val hasil: String
)