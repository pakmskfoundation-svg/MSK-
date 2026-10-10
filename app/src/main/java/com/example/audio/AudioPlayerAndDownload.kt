package com.example.audio

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

data class AudioPlaybackState(
    val isLoaded: Boolean = false,
    val isPlaying: Boolean = false,
    val currentFilePath: String? = null,
    val currentTitle: String = "",
    val currentVoiceName: String = "",
    val currentPositionMs: Long = 0L,
    val durationMs: Long = 0L,
    val volume: Float = 1.0f,
    val waveformBars: List<Float> = List(48) { 0.25f }
)

class AudioPlayerController(private val scope: CoroutineScope) {

    private var mediaPlayer: MediaPlayer? = null
    private var progressJob: Job? = null

    private val _state = MutableStateFlow(AudioPlaybackState())
    val state: StateFlow<AudioPlaybackState> = _state.asStateFlow()

    fun loadAndPlay(
        file: File,
        title: String,
        voiceName: String,
        autoPlay: Boolean = true,
        initialVolume: Float = 1.0f
    ) {
        if (!file.exists()) return
        releasePlayer()

        val bars = WavUtils.extractWaveformAmplitudes(file, barCount = 48)
        try {
            val mp = MediaPlayer().apply {
                setDataSource(file.absolutePath)
                prepare()
                val vol = initialVolume.coerceIn(0f, 1f)
                setVolume(vol, vol)
                setOnCompletionListener {
                    stopProgressPolling()
                    _state.value = _state.value.copy(
                        isPlaying = false,
                        currentPositionMs = _state.value.durationMs
                    )
                }
            }
            mediaPlayer = mp
            val totalDur = mp.duration.toLong().coerceAtLeast(500L)
            _state.value = AudioPlaybackState(
                isLoaded = true,
                isPlaying = autoPlay,
                currentFilePath = file.absolutePath,
                currentTitle = title,
                currentVoiceName = voiceName,
                currentPositionMs = 0L,
                durationMs = totalDur,
                volume = initialVolume.coerceIn(0f, 1f),
                waveformBars = bars
            )
            if (autoPlay) {
                mp.start()
                startProgressPolling()
            }
        } catch (_: Exception) {
            _state.value = AudioPlaybackState(
                isLoaded = true,
                isPlaying = false,
                currentFilePath = file.absolutePath,
                currentTitle = title,
                currentVoiceName = voiceName,
                currentPositionMs = 0L,
                durationMs = WavUtils.estimateWavDurationMs(file.length()),
                volume = initialVolume.coerceIn(0f, 1f),
                waveformBars = bars
            )
        }
    }

    fun togglePlayPause() {
        val mp = mediaPlayer ?: return
        try {
            if (mp.isPlaying) {
                mp.pause()
                stopProgressPolling()
                _state.value = _state.value.copy(isPlaying = false)
            } else {
                if (mp.currentPosition >= mp.duration - 150) {
                    mp.seekTo(0)
                }
                mp.start()
                startProgressPolling()
                _state.value = _state.value.copy(isPlaying = true)
            }
        } catch (_: Exception) {}
    }

    fun replay() {
        val mp = mediaPlayer ?: return
        try {
            mp.seekTo(0)
            mp.start()
            startProgressPolling()
            _state.value = _state.value.copy(isPlaying = true, currentPositionMs = 0L)
        } catch (_: Exception) {}
    }

    fun seekToFraction(fraction: Float) {
        val mp = mediaPlayer ?: return
        try {
            val targetMs = (mp.duration * fraction.coerceIn(0f, 1f)).toInt()
            mp.seekTo(targetMs)
            _state.value = _state.value.copy(currentPositionMs = targetMs.toLong())
        } catch (_: Exception) {}
    }

    fun setVolume(volume: Float) {
        val clamped = volume.coerceIn(0f, 1f)
        _state.value = _state.value.copy(volume = clamped)
        try {
            mediaPlayer?.setVolume(clamped, clamped)
        } catch (_: Exception) {}
    }

    private fun startProgressPolling() {
        stopProgressPolling()
        progressJob = scope.launch(Dispatchers.Main) {
            while (isActive) {
                val mp = mediaPlayer
                if (mp != null && _state.value.isPlaying) {
                    try {
                        val pos = mp.currentPosition.toLong().coerceAtLeast(0L)
                        _state.value = _state.value.copy(currentPositionMs = pos)
                    } catch (_: Exception) {}
                }
                delay(80L)
            }
        }
    }

    private fun stopProgressPolling() {
        progressJob?.cancel()
        progressJob = null
    }

    fun releasePlayer() {
        stopProgressPolling()
        try {
            mediaPlayer?.stop()
        } catch (_: Exception) {}
        try {
            mediaPlayer?.release()
        } catch (_: Exception) {}
        mediaPlayer = null
    }
}

object AudioDownloadHelper {

    data class DownloadOutcome(
        val success: Boolean,
        val displayPath: String,
        val fileName: String,
        val message: String
    )

    /**
     * Saves the generated audio file to the user's device `Downloads/MSK_AI_Voice_Studio/` folder
     * using Android's MediaStore API (no dangerous storage permission required) with a meaningful
     * filename such as `msk-ai-voice-2026-10-10-1430.wav`.
     */
    suspend fun saveAudioToDownloads(
        context: Context,
        sourceFile: File,
        requestedFormatExt: String = "wav"
    ): DownloadOutcome = withContext(Dispatchers.IO) {
        if (!sourceFile.exists() || sourceFile.length() <= 44) {
            return@withContext DownloadOutcome(
                success = false,
                displayPath = "",
                fileName = "",
                message = "Audio file is empty or missing. Please generate the voice first."
            )
        }

        val datePart = java.text.SimpleDateFormat("yyyy-MM-dd-HHmmss", java.util.Locale.US)
            .format(java.util.Date())
        val safeExt = requestedFormatExt.lowercase().let { if (it == "mp3" || it == "wav") it else "wav" }
        val mimeType = if (safeExt == "mp3") "audio/mpeg" else "audio/wav"
        val fileName = "msk-ai-voice-$datePart.$safeExt"

        return@withContext try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = context.contentResolver
                val values = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                    put(MediaStore.Downloads.MIME_TYPE, mimeType)
                    put(
                        MediaStore.Downloads.RELATIVE_PATH,
                        Environment.DIRECTORY_DOWNLOADS + "/MSK_AI_Voice_Studio"
                    )
                    put(MediaStore.Downloads.IS_PENDING, 1)
                }
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                    ?: throw IllegalStateException("Could not create MediaStore Downloads entry.")

                resolver.openOutputStream(uri)?.use { outStream ->
                    FileInputStream(sourceFile).use { inStream ->
                        inStream.copyTo(outStream)
                    }
                } ?: throw IllegalStateException("Could not open output stream for Downloads.")

                values.clear()
                values.put(MediaStore.Downloads.IS_PENDING, 0)
                resolver.update(uri, values, null, null)

                DownloadOutcome(
                    success = true,
                    displayPath = "Downloads/MSK_AI_Voice_Studio/$fileName",
                    fileName = fileName,
                    message = "Downloaded \"$fileName\" to Downloads/MSK_AI_Voice_Studio"
                )
            } else {
                val downloadsDir = File(
                    context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                    "MSK_AI_Voice_Studio"
                ).apply { mkdirs() }
                val destFile = File(downloadsDir, fileName)
                FileInputStream(sourceFile).use { input ->
                    FileOutputStream(destFile).use { output ->
                        input.copyTo(output)
                    }
                }
                DownloadOutcome(
                    success = true,
                    displayPath = destFile.absolutePath,
                    fileName = fileName,
                    message = "Downloaded \"$fileName\" to device storage"
                )
            }
        } catch (e: Exception) {
            DownloadOutcome(
                success = false,
                displayPath = "",
                fileName = fileName,
                message = "Download error: ${e.localizedMessage ?: "Unable to write file"}"
            )
        }
    }

    fun shareAudioFile(context: Context, sourceFile: File, title: String) {
        if (!sourceFile.exists()) return
        try {
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                sourceFile
            )
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "audio/wav"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "MSK AI Voice Studio — $title")
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Generated with MSK AI Voice Studio (Your Words. Every Voice. Powered by AI.)"
                )
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(
                Intent.createChooser(shareIntent, "Share or Save Audio File")
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        } catch (_: Exception) {}
    }
}
