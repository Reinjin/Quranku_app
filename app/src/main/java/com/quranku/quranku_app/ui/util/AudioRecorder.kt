@file:Suppress("DEPRECATION")

package com.quranku.quranku_app.ui.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class AudioRecorder @Inject constructor(
    private val context: Context
) {
    private var audioRecord: AudioRecord? = null
    private var isRecording = false
    private val sampleRate = 16000
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT

    // Fungsi untuk merekam dan return ByteArray
    suspend fun record(): ByteArray {
        return withContext(Dispatchers.IO) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
                throw SecurityException("Recording permission not granted")
            }

            val bufferSize = AudioRecord.getMinBufferSize(
                sampleRate,
                channelConfig,
                audioFormat
            )

            val audioData = ByteArrayOutputStream()

            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                channelConfig,
                audioFormat,
                bufferSize
            )

            val buffer = ByteArray(bufferSize)
            audioRecord?.startRecording()
            isRecording = true

            try {
                while (isRecording) {
                    val readCount = audioRecord?.read(buffer, 0, bufferSize) ?: 0
                    if (readCount > 0) {
                        audioData.write(buffer, 0, readCount)
                    }
                }
                // Convert to WAV format
                return@withContext createWavData(audioData.toByteArray())
            } finally {
                audioRecord?.stop()
                audioRecord?.release()
                audioRecord = null
            }
        }
    }

    fun stopRecording() {
        isRecording = false
    }

    private fun createWavData(rawData: ByteArray): ByteArray {
        // Buat WAV header
        val header = ByteArray(44)
        val totalDataLen = rawData.size + 36
        val bitrate = sampleRate * 16 * 1 / 8

        header[0] = 'R'.toByte()  // RIFF/WAVE header
        header[1] = 'I'.toByte()
        header[2] = 'F'.toByte()
        header[3] = 'F'.toByte()
        header[4] = (totalDataLen and 0xff).toByte()
        header[5] = (totalDataLen shr 8 and 0xff).toByte()
        header[6] = (totalDataLen shr 16 and 0xff).toByte()
        header[7] = (totalDataLen shr 24 and 0xff).toByte()
        header[8] = 'W'.toByte()
        header[9] = 'A'.toByte()
        header[10] = 'V'.toByte()
        header[11] = 'E'.toByte()
        header[12] = 'f'.toByte()  // 'fmt ' chunk
        header[13] = 'm'.toByte()
        header[14] = 't'.toByte()
        header[15] = ' '.toByte()
        header[16] = 16  // 4 bytes: size of 'fmt ' chunk
        header[17] = 0
        header[18] = 0
        header[19] = 0
        header[20] = 1  // format = 1 (PCM)
        header[21] = 0
        header[22] = 1  // channels = 1 (mono)
        header[23] = 0
        header[24] = (sampleRate and 0xff).toByte()
        header[25] = (sampleRate shr 8 and 0xff).toByte()
        header[26] = (sampleRate shr 16 and 0xff).toByte()
        header[27] = (sampleRate shr 24 and 0xff).toByte()
        header[28] = (bitrate and 0xff).toByte()
        header[29] = (bitrate shr 8 and 0xff).toByte()
        header[30] = (bitrate shr 16 and 0xff).toByte()
        header[31] = (bitrate shr 24 and 0xff).toByte()
        header[32] = 2  // block align
        header[33] = 0
        header[34] = 16  // bits per sample
        header[35] = 0
        header[36] = 'd'.toByte()
        header[37] = 'a'.toByte()
        header[38] = 't'.toByte()
        header[39] = 'a'.toByte()
        header[40] = (rawData.size and 0xff).toByte()
        header[41] = (rawData.size shr 8 and 0xff).toByte()
        header[42] = (rawData.size shr 16 and 0xff).toByte()
        header[43] = (rawData.size shr 24 and 0xff).toByte()

        return header + rawData
    }
}