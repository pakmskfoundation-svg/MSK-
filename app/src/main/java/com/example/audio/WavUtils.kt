package com.example.audio

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.abs
import kotlin.math.sqrt

object WavUtils {

    /**
     * Checks if raw bytes already start with a RIFF/WAV header or ID3/MP3 header.
     * If not (for example, Gemini TTS inlineData `audio/L16;rate=24000` 16-bit mono PCM),
     * wraps the raw PCM bytes in a valid 44-byte RIFF WAVE header and optionally scales volume/speed.
     */
    fun ensurePlayableAudioFile(
        rawBytes: ByteArray,
        mimeType: String,
        targetFile: File,
        speedMultiplier: Float = 1.0f,
        volumeMultiplier: Float = 1.0f
    ): Long {
        if (rawBytes.size >= 4) {
            val header = String(rawBytes, 0, 4, Charsets.US_ASCII)
            if (header == "RIFF") {
                targetFile.writeBytes(rawBytes)
                return estimateWavDurationMs(rawBytes.size.toLong(), 24000)
            }
            if (header.startsWith("ID3") || (rawBytes[0] == 0xFF.toByte() && (rawBytes[1].toInt() and 0xE0) == 0xE0)) {
                targetFile.writeBytes(rawBytes)
                return ((rawBytes.size / 16000f) * 1000L).toLong().coerceAtLeast(1000L)
            }
        }

        // Parse sample rate from mimeType e.g. "audio/L16;codec=pcm;rate=24000"
        val sampleRate = parseSampleRate(mimeType, defaultRate = 24000)
        val processedPcm = processPcm16Mono(
            pcmBytes = rawBytes,
            speed = speedMultiplier.coerceIn(0.5f, 2.0f),
            volume = volumeMultiplier.coerceIn(0.1f, 1.5f)
        )
        writeWavFile(processedPcm, sampleRate, channels = 1, bitsPerSample = 16, outFile = targetFile)
        val totalSamples = processedPcm.size / 2
        return ((totalSamples.toDouble() / sampleRate.toDouble()) * 1000.0).toLong().coerceAtLeast(500L)
    }

    private fun parseSampleRate(mimeType: String, defaultRate: Int): Int {
        val regex = Regex("rate=(\\d+)", RegexOption.IGNORE_CASE)
        val match = regex.find(mimeType)
        return match?.groupValues?.getOrNull(1)?.toIntOrNull() ?: defaultRate
    }

    /**
     * Applies volume gain and linear resampling for speed adjustment on 16-bit little-endian mono PCM.
     */
    fun processPcm16Mono(pcmBytes: ByteArray, speed: Float, volume: Float): ByteArray {
        val sampleCount = pcmBytes.size / 2
        if (sampleCount <= 0) return pcmBytes
        if (abs(speed - 1.0f) < 0.03f && abs(volume - 1.0f) < 0.03f) {
            return pcmBytes
        }

        val inBuffer = ByteBuffer.wrap(pcmBytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer()
        val samples = ShortArray(sampleCount)
        inBuffer.get(samples)

        val outSampleCount = (sampleCount / speed).toInt().coerceAtLeast(1)
        val outBytes = ByteArray(outSampleCount * 2)
        val outBuffer = ByteBuffer.wrap(outBytes).order(ByteOrder.LITTLE_ENDIAN)

        for (i in 0 until outSampleCount) {
            val srcPos = i * speed
            val idx0 = srcPos.toInt().coerceIn(0, sampleCount - 1)
            val idx1 = (idx0 + 1).coerceIn(0, sampleCount - 1)
            val frac = srcPos - idx0
            val interpolated = samples[idx0] * (1f - frac) + samples[idx1] * frac
            val scaled = (interpolated * volume).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
            outBuffer.putShort(scaled.toShort())
        }
        return outBytes
    }

    fun writeWavFile(
        pcmData: ByteArray,
        sampleRate: Int,
        channels: Int = 1,
        bitsPerSample: Int = 16,
        outFile: File
    ) {
        val byteRate = sampleRate * channels * bitsPerSample / 8
        val blockAlign = (channels * bitsPerSample / 8).toShort()
        val dataSize = pcmData.size
        val chunkSize = 36 + dataSize

        val header = ByteBuffer.allocate(44).order(ByteOrder.LITTLE_ENDIAN)
        header.put("RIFF".toByteArray(Charsets.US_ASCII))
        header.putInt(chunkSize)
        header.put("WAVE".toByteArray(Charsets.US_ASCII))
        header.put("fmt ".toByteArray(Charsets.US_ASCII))
        header.putInt(16) // PCM subchunk1 size
        header.putShort(1.toShort()) // AudioFormat 1 = PCM
        header.putShort(channels.toShort())
        header.putInt(sampleRate)
        header.putInt(byteRate)
        header.putShort(blockAlign)
        header.putShort(bitsPerSample.toShort())
        header.put("data".toByteArray(Charsets.US_ASCII))
        header.putInt(dataSize)

        FileOutputStream(outFile).use { fos ->
            fos.write(header.array())
            fos.write(pcmData)
        }
    }

    /**
     * Combines multiple WAV files generated from chunked long text narration into a single WAV file.
     */
    fun combineWavFiles(wavFiles: List<File>, outputFile: File): Long {
        if (wavFiles.isEmpty()) return 0L
        if (wavFiles.size == 1) {
            wavFiles.first().copyTo(outputFile, overwrite = true)
            return estimateWavDurationMs(outputFile.length(), 24000)
        }

        var detectedSampleRate = 24000
        var detectedChannels = 1
        var detectedBits = 16
        val combinedPcm = ByteArrayOutputStream()

        wavFiles.forEachIndexed { index, file ->
            if (file.exists() && file.length() > 44) {
                val bytes = file.readBytes()
                if (index == 0 && bytes.size >= 44 && String(bytes, 0, 4, Charsets.US_ASCII) == "RIFF") {
                    val buf = ByteBuffer.wrap(bytes, 0, 44).order(ByteOrder.LITTLE_ENDIAN)
                    detectedChannels = buf.getShort(22).toInt().coerceAtLeast(1)
                    detectedSampleRate = buf.getInt(24).coerceAtLeast(8000)
                    detectedBits = buf.getShort(34).toInt().coerceAtLeast(16)
                }
                val offset = if (bytes.size > 44 && String(bytes, 0, 4, Charsets.US_ASCII) == "RIFF") 44 else 0
                combinedPcm.write(bytes, offset, bytes.size - offset)
                // Add 180ms silence gap between chunks for natural paragraph breathing
                val silenceBytes = (detectedSampleRate * detectedChannels * (detectedBits / 8) * 0.18).toInt()
                val alignedSilence = if (silenceBytes % 2 == 0) silenceBytes else silenceBytes + 1
                combinedPcm.write(ByteArray(alignedSilence))
            }
        }

        val finalPcm = combinedPcm.toByteArray()
        writeWavFile(
            pcmData = finalPcm,
            sampleRate = detectedSampleRate,
            channels = detectedChannels,
            bitsPerSample = detectedBits,
            outFile = outputFile
        )
        val bytesPerSecond = (detectedSampleRate * detectedChannels * (detectedBits / 8)).coerceAtLeast(1)
        return ((finalPcm.size.toDouble() / bytesPerSecond.toDouble()) * 1000.0).toLong().coerceAtLeast(800L)
    }

    fun estimateWavDurationMs(fileSizeBytes: Long, sampleRate: Int = 24000): Long {
        val pcmBytes = (fileSizeBytes - 44L).coerceAtLeast(0L)
        val bytesPerSec = sampleRate * 2 // 16-bit mono
        return ((pcmBytes.toDouble() / bytesPerSec.toDouble()) * 1000.0).toLong().coerceAtLeast(600L)
    }

    /**
     * Extracts normalized RMS amplitude bars [0.12f..1.0f] directly from a WAV/PCM file
     * so the audio player displays the real acoustic waveform of the generated speech.
     */
    fun extractWaveformAmplitudes(file: File, barCount: Int = 48): List<Float> {
        if (!file.exists() || file.length() <= 100) {
            return List(barCount) { 0.25f }
        }
        return try {
            val bytes = file.readBytes()
            val startOffset = if (bytes.size > 44 && String(bytes, 0, 4, Charsets.US_ASCII) == "RIFF") 44 else 0
            val pcmLength = bytes.size - startOffset
            val totalSamples = pcmLength / 2
            if (totalSamples < barCount) return List(barCount) { 0.3f }

            val samplesPerBar = (totalSamples / barCount).coerceAtLeast(1)
            val shortBuf = ByteBuffer.wrap(bytes, startOffset, pcmLength)
                .order(ByteOrder.LITTLE_ENDIAN)
                .asShortBuffer()

            val rawRms = FloatArray(barCount)
            var maxRms = 1f

            for (bar in 0 until barCount) {
                var sumSq = 0.0
                val startSample = bar * samplesPerBar
                val step = (samplesPerBar / 64).coerceAtLeast(1)
                var count = 0
                var s = startSample
                val endSample = (startSample + samplesPerBar).coerceAtMost(totalSamples)
                while (s < endSample) {
                    val v = shortBuf.get(s).toDouble()
                    sumSq += v * v
                    count++
                    s += step
                }
                val rms = if (count > 0) sqrt(sumSq / count).toFloat() else 0f
                rawRms[bar] = rms
                if (rms > maxRms) maxRms = rms
            }

            rawRms.map { (it / maxRms).coerceIn(0.14f, 1.0f) }
        } catch (_: Exception) {
            List(barCount) { 0.3f }
        }
    }
}
