package com.quranku.quranku_app.ui.viewmodel

import android.content.Context
import android.util.JsonReader
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quranku.quranku_app.R
import com.quranku.quranku_app.data.models.Surah
import com.quranku.quranku_app.data.models.Verse
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuranViewModel @Inject constructor(
    @ApplicationContext private val context: Context // Menggunakan Application Context
) : ViewModel() {

    private val _surahList = MutableStateFlow<List<Surah>>(emptyList())
    val surahList: StateFlow<List<Surah>> = _surahList.asStateFlow()

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState.asStateFlow()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    init {
        loadSurahData()
    }

    private fun loadSurahData() {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingState.value = true
            _errorState.value = null
            try {
                val surahs = parseQuranJson()
                _surahList.value = surahs
            } catch (e: Exception) {
                Log.e("QuranViewModel", "Gagal memuat data", e)
                _surahList.value = emptyList()
                _errorState.value = e.message ?: "Gagal memuat data Quran"
            } finally {
                _loadingState.value = false
            }
        }
    }

    private fun parseQuranJson(): List<Surah> {
        val surahs = mutableListOf<Surah>()

        context.resources.openRawResource(R.raw.al_quran).use { inputStream ->
            JsonReader(inputStream.bufferedReader()).use { reader ->
                reader.beginArray()
                while (reader.hasNext()) {
                    val surah = parseSurah(reader)
                    surahs.add(surah)
                }
                reader.endArray()
            }
        }

        return surahs
    }

    private fun parseSurah(reader: JsonReader): Surah {
        var id: Int? = null
        var name: String? = null
        var transliteration: String? = null
        var translation: String? = null
        var type: String? = null
        var totalVerses: Int? = null
        val verses = mutableListOf<Verse>()

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "id" -> id = reader.nextInt()
                "name" -> name = reader.nextString()
                "transliteration" -> transliteration = reader.nextString()
                "translation" -> translation = reader.nextString()
                "type" -> type = reader.nextString()
                "total_verses" -> totalVerses = reader.nextInt()
                "verses" -> {
                    reader.beginArray()
                    while (reader.hasNext()) {
                        verses.add(parseVerse(reader))
                    }
                    reader.endArray()
                }
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return Surah(
            id = id ?: 0,
            name = name ?: "",
            transliteration = transliteration ?: "",
            translation = translation ?: "",
            type = type ?: "",
            total_verses = totalVerses ?: 0,
            verses = verses
        )
    }

    private fun parseVerse(reader: JsonReader): Verse {
        var id: Int? = null
        var text: String? = null
        var translation: String? = null

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "id" -> id = reader.nextInt()
                "text" -> text = reader.nextString()
                "translation" -> translation = reader.nextString()
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return Verse(
            id = id ?: 0,
            text = text ?: "",
            translation = translation ?: ""
        )
    }
}
