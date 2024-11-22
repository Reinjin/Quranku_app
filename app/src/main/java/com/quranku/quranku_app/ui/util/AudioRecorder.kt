@file:Suppress("DEPRECATION")

package com.quranku.quranku_app.ui.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Environment
import androidx.core.app.ActivityCompat
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AudioRecorder(private val context: Context) {
    private var audioRecord: AudioRecord? = null
    private var isRecording = false
    private val sampleRate = 16000
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT

    // Metode untuk mendapatkan file yang disimpan
    var recordedFile: File? = null
        private set

    fun startRecording(): File? {
        // Check permissions
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }

        // Calculate buffer size
        val bufferSize = AudioRecord.getMinBufferSize(
            sampleRate,
            channelConfig,
            audioFormat
        )

        // Create AudioRecord instance
        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            channelConfig,
            audioFormat,
            bufferSize
        )

        // Prepare output file
        recordedFile = File(
            context.getExternalFilesDir(Environment.DIRECTORY_MUSIC),
            "recorded_audio_${System.currentTimeMillis()}.wav"
        )

        // Start recording in a background thread
        Thread {
            try {
                val buffer = ByteArray(bufferSize)
                audioRecord?.startRecording()
                isRecording = true

                // Use ByteArrayOutputStream to collect audio data
                val outputStream = ByteArrayOutputStream()

                while (isRecording) {
                    val readCount = audioRecord?.read(buffer, 0, bufferSize) ?: 0
                    if (readCount > 0) {
                        outputStream.write(buffer, 0, readCount)
                    }
                }

                // Write WAV file with proper headers
                writeWavFile(outputStream.toByteArray(), recordedFile!!)
            } catch (e: IOException) {
                e.printStackTrace()
                recordedFile = null
            }
        }.start()

        return recordedFile
    }

    fun stopRecording() {
        isRecording = false
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
    }

    private fun writeWavFile(audioData: ByteArray, outputFile: File) {
        try {
            FileOutputStream(outputFile).use { fileOutputStream ->
                // Write WAV header
                val headerSize = 44
                val header = ByteArray(headerSize)

                // RIFF header
                header[0] = 'R'.toByte()
                header[1] = 'I'.toByte()
                header[2] = 'F'.toByte()
                header[3] = 'F'.toByte()

                // File size (data size + header size)
                val fileSize = audioData.size + headerSize - 8
                header[4] = (fileSize and 0xFF).toByte()
                header[5] = ((fileSize shr 8) and 0xFF).toByte()
                header[6] = ((fileSize shr 16) and 0xFF).toByte()
                header[7] = ((fileSize shr 24) and 0xFF).toByte()

                // WAV identifier
                header[8] = 'W'.toByte()
                header[9] = 'A'.toByte()
                header[10] = 'V'.toByte()
                header[11] = 'E'.toByte()

                // Format chunk
                header[12] = 'f'.toByte()
                header[13] = 'm'.toByte()
                header[14] = 't'.toByte()
                header[15] = ' '.toByte()

                // Chunk size
                header[16] = 16
                header[17] = 0
                header[18] = 0
                header[19] = 0

                // Audio format (1 = PCM)
                header[20] = 1
                header[21] = 0

                // Number of channels
                header[22] = 1
                header[23] = 0

                // Sample rate
                header[24] = (sampleRate and 0xFF).toByte()
                header[25] = ((sampleRate shr 8) and 0xFF).toByte()
                header[26] = ((sampleRate shr 16) and 0xFF).toByte()
                header[27] = ((sampleRate shr 24) and 0xFF).toByte()

                // Byte rate
                val byteRate = sampleRate * 2 // 16-bit mono
                header[28] = (byteRate and 0xFF).toByte()
                header[29] = ((byteRate shr 8) and 0xFF).toByte()
                header[30] = ((byteRate shr 16) and 0xFF).toByte()
                header[31] = ((byteRate shr 24) and 0xFF).toByte()

                // Block align
                header[32] = 2
                header[33] = 0

                // Bits per sample
                header[34] = 16
                header[35] = 0

                // Data chunk
                header[36] = 'd'.toByte()
                header[37] = 'a'.toByte()
                header[38] = 't'.toByte()
                header[39] = 'a'.toByte()

                // Data chunk size
                header[40] = (audioData.size and 0xFF).toByte()
                header[41] = ((audioData.size shr 8) and 0xFF).toByte()
                header[42] = ((audioData.size shr 16) and 0xFF).toByte()
                header[43] = ((audioData.size shr 24) and 0xFF).toByte()

                // Write header and audio data
                fileOutputStream.write(header)
                fileOutputStream.write(audioData)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}