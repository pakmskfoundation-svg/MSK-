package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.audio.WavUtils
import com.example.data.VoiceCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `verify brand identity and strings`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        assertEquals("MSK AI Voice Studio", context.getString(R.string.app_name))
        assertEquals(
            "Created with passion by MSK & Team",
            context.getString(R.string.footer_attribution)
        )
    }

    @Test
    fun `verify Urdu script and Roman Urdu auto language detection`() {
        val urduScript = VoiceCatalog.detectLanguageFromText("السلام علیکم آپ کیسے ہیں؟")
        assertNotNull(urduScript)
        assertEquals("ur", urduScript?.code)

        val romanUrdu = VoiceCatalog.detectLanguageFromText("Aap kaise hain dost, bohat shukriya")
        assertNotNull(romanUrdu)
        assertEquals("ur", romanUrdu?.code)

        val pashtoScript = VoiceCatalog.detectLanguageFromText("ستاسو هره لیکنه په پښتو غږ")
        assertNotNull(pashtoScript)
        assertEquals("ps", pashtoScript?.code)
    }

    @Test
    fun `verify WAV generation and waveform amplitude extraction`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val tempWav = File(context.cacheDir, "test_msk_voice.wav")
        val rawPcm = ByteArray(4800) { idx -> ((idx * 17) % 120).toByte() }

        val durationMs = WavUtils.ensurePlayableAudioFile(
            rawBytes = rawPcm,
            mimeType = "audio/L16;rate=24000",
            targetFile = tempWav,
            speedMultiplier = 1.0f,
            volumeMultiplier = 1.0f
        )

        assertTrue(tempWav.exists())
        assertTrue(tempWav.length() > 44)
        assertTrue(durationMs > 0L)

        val bars = WavUtils.extractWaveformAmplitudes(tempWav, barCount = 24)
        assertEquals(24, bars.size)
        tempWav.delete()
    }
}
