package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.AudioPlaybackState
import com.example.ui.GenerationProgressState
import com.example.ui.theme.MskCyanBright
import com.example.ui.theme.MskElectricBlue
import com.example.ui.theme.MskEmeraldSuccess
import com.example.ui.theme.MskNeonViolet
import kotlin.math.PI
import kotlin.math.sin

/**
 * Animated Sound-Wave Logo incorporating the letters "MSK" with glowing electric blue,
 * cyan, and violet acoustic bars.
 */
@Composable
fun MskAnimatedSoundwaveLogo(
    isAnimating: Boolean = true,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "msk_logo_wave")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2f * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_phase"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF0F172A),
                            Color(0xFF1E1B4B),
                            Color(0xFF083344)
                        )
                    )
                )
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(
                        listOf(MskCyanBright, MskElectricBlue, MskNeonViolet)
                    ),
                    shape = RoundedCornerShape(13.dp)
                )
        ) {
            Canvas(modifier = Modifier.fillMaxSize().padding(6.dp)) {
                val barCount = 7
                val spacing = size.width / (barCount + 1)
                for (i in 0 until barCount) {
                    val dynamicFactor = if (isAnimating) {
                        0.45f + 0.45f * sin(phase + i * 0.85f)
                    } else {
                        0.5f
                    }
                    val barHeight = (size.height * dynamicFactor).coerceIn(size.height * 0.22f, size.height * 0.92f)
                    val x = spacing * (i + 1)
                    val topY = (size.height - barHeight) / 2f
                    drawLine(
                        brush = Brush.verticalGradient(
                            listOf(MskCyanBright, MskElectricBlue, MskNeonViolet)
                        ),
                        start = Offset(x, topY),
                        end = Offset(x, topY + barHeight),
                        strokeWidth = 4.5f,
                        cap = StrokeCap.Round
                    )
                }
            }
            Text(
                text = "MSK",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 10.sp,
                    letterSpacing = 0.8.sp
                ),
                color = Color.White,
                modifier = Modifier
                    .background(Color(0xAA060B16), RoundedCornerShape(4.dp))
                    .padding(horizontal = 3.dp, vertical = 1.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "MSK",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    ),
                    color = MskCyanBright
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "AI Voice Studio",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = "Your Words. Every Voice. Powered by AI.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Lightweight, polished Circular Sound Wave + Equalizer animation displayed while
 * audio is genuinely being synthesized. Optimized for all Android devices.
 */
@Composable
fun AudioGenerationAnimationCard(
    progressState: GenerationProgressState,
    voiceName: String,
    languageName: String,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "gen_anim")
    val pulse by transition.animateFloat(
        initialValue = 0.82f,
        targetValue = 1.16f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_ring"
    )
    val wavePhase by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "eq_wave"
    )

    Surface(
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.92f),
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.5.dp,
                brush = Brush.horizontalGradient(
                    listOf(MskCyanBright, MskElectricBlue, MskNeonViolet)
                ),
                shape = RoundedCornerShape(22.dp)
            )
            .testTag("generation_progress_card")
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(20.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(96.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val baseRadius = size.minDimension / 3.2f

                    // Outer glowing circular sound wave rings
                    drawCircle(
                        color = MskCyanBright.copy(alpha = 0.18f),
                        radius = baseRadius * pulse * 1.25f,
                        center = center,
                        style = Stroke(width = 3.dp.toPx())
                    )
                    drawCircle(
                        color = MskNeonViolet.copy(alpha = 0.28f),
                        radius = baseRadius * (2f - pulse * 0.85f),
                        center = center,
                        style = Stroke(width = 2.dp.toPx())
                    )
                    drawCircle(
                        brush = Brush.sweepGradient(
                            listOf(MskCyanBright, MskElectricBlue, MskNeonViolet, MskCyanBright)
                        ),
                        radius = baseRadius,
                        center = center,
                        style = Stroke(width = 4.dp.toPx())
                    )

                    // Moving equalizer bars inside the circle
                    val barCount = 9
                    val totalWidth = baseRadius * 1.3f
                    val step = totalWidth / barCount
                    val startX = center.x - totalWidth / 2f + step / 2f
                    for (i in 0 until barCount) {
                        val amp = 0.3f + 0.65f * kotlin.math.abs(sin(wavePhase + i * 0.6f))
                        val h = baseRadius * 1.1f * amp
                        val x = startX + i * step
                        drawLine(
                            color = Color.White,
                            start = Offset(x, center.y - h / 2f),
                            end = Offset(x, center.y + h / 2f),
                            strokeWidth = 3.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = progressState.stageMessage.ifBlank { "Generating speech…" },
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "$voiceName • $languageName",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (progressState.totalChunks > 1) {
                val fraction = (progressState.completedChunks.toFloat() / progressState.totalChunks.toFloat())
                    .coerceIn(0.08f, 1f)
                LinearProgressIndicator(
                    progress = { fraction },
                    color = MskCyanBright,
                    trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(7.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Chunk ${progressState.completedChunks + 1} of ${progressState.totalChunks}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MskCyanBright
                )
            } else {
                LinearProgressIndicator(
                    color = MskCyanBright,
                    trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(7.dp)
                        .clip(CircleShape)
                )
            }
        }
    }
}

/**
 * Interactive Studio Audio Player with real acoustic waveform visualization,
 * tap-to-seek, Play/Pause/Resume, Replay, Volume slider, and one-tap Download & Share buttons.
 */
@Composable
fun StudioWaveformAudioPlayerCard(
    playbackState: AudioPlaybackState,
    downloadFormat: String,
    onFormatChange: (String) -> Unit,
    onTogglePlayPause: () -> Unit,
    onReplay: () -> Unit,
    onSeekFraction: (Float) -> Unit,
    onVolumeChange: (Float) -> Unit,
    onDownloadClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progressFraction = if (playbackState.durationMs > 0L) {
        (playbackState.currentPositionMs.toFloat() / playbackState.durationMs.toFloat()).coerceIn(0f, 1f)
    } else 0f

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.5.dp,
                brush = Brush.horizontalGradient(
                    listOf(MskElectricBlue, MskCyanBright, MskNeonViolet)
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .testTag("audio_player_card")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(MskElectricBlue.copy(alpha = 0.18f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = "Audio Output Ready",
                            tint = MskCyanBright
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = playbackState.currentTitle.ifBlank { "Studio Audio Output" },
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = playbackState.currentVoiceName,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Format Badge Selector (WAV / MP3)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("wav", "mp3").forEach { fmt ->
                        val selected = downloadFormat.equals(fmt, ignoreCase = true)
                        Surface(
                            onClick = { onFormatChange(fmt) },
                            shape = RoundedCornerShape(8.dp),
                            color = if (selected) MskElectricBlue else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.testTag("format_chip_$fmt")
                        ) {
                            Text(
                                text = fmt.uppercase(),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Interactive Acoustic Waveform Visualizer (Tap to Seek)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f))
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            if (size.width > 0) {
                                val frac = (offset.x / size.width.toFloat()).coerceIn(0f, 1f)
                                onSeekFraction(frac)
                            }
                        }
                    }
                    .padding(horizontal = 10.dp, vertical = 8.dp)
                    .testTag("waveform_seek_canvas")
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val bars = playbackState.waveformBars
                    val count = bars.size.coerceAtLeast(1)
                    val slotWidth = size.width / count
                    val barWidth = (slotWidth * 0.62f).coerceAtLeast(3f)

                    for (i in 0 until count) {
                        val barFraction = i.toFloat() / count.toFloat()
                        val isPlayed = barFraction <= progressFraction
                        val amp = bars[i].coerceIn(0.15f, 1.0f)
                        val barHeight = (size.height * amp).coerceAtLeast(6f)
                        val left = i * slotWidth + (slotWidth - barWidth) / 2f
                        val top = (size.height - barHeight) / 2f

                        drawRoundRect(
                            color = if (isPlayed) MskCyanBright else MskElectricBlue.copy(alpha = 0.35f),
                            topLeft = Offset(left, top),
                            size = Size(barWidth, barHeight),
                            cornerRadius = CornerRadius(barWidth / 2f, barWidth / 2f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Timestamp Row
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = formatDurationMs(playbackState.currentPositionMs),
                    style = MaterialTheme.typography.labelSmall,
                    color = MskCyanBright
                )
                Text(
                    text = "Tap waveform to seek",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatDurationMs(playbackState.durationMs),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Playback Transport & Volume Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilledIconButton(
                        onClick = onTogglePlayPause,
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MskElectricBlue,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("player_play_pause_button")
                    ) {
                        Icon(
                            imageVector = if (playbackState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (playbackState.isPlaying) "Pause Audio" else "Play Audio"
                        )
                    }

                    IconButton(
                        onClick = onReplay,
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("player_replay_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Replay,
                            contentDescription = "Replay Audio",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Player Volume Slider
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Playback Volume",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Slider(
                        value = playbackState.volume,
                        onValueChange = onVolumeChange,
                        valueRange = 0.1f..1.0f,
                        colors = SliderDefaults.colors(
                            thumbColor = MskCyanBright,
                            activeTrackColor = MskElectricBlue
                        ),
                        modifier = Modifier.weight(1f).testTag("player_volume_slider")
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Prominent Download & Share Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onDownloadClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MskEmeraldSuccess,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("download_audio_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Download Audio File",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Download Audio (.${downloadFormat.uppercase()})",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }

                OutlinedButton(
                    onClick = onShareClick,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("share_audio_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share Audio File",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Share")
                }
            }
        }
    }
}

/**
 * Non-intrusive, policy-compliant Google AdSense Reserve Slot Card.
 * Placed strictly away from Generate, Play, and Download buttons to prevent accidental clicks.
 */
@Composable
fun AdSenseReservedSlotBanner(
    slotName: String,
    adUnitPlaceholder: String = "ca-pub-XXXXXXXXXXXXXXXX / 9482019283",
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.38f),
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(vertical = 4.dp)
            .testTag("adsense_slot_$slotName")
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "ADVERTISEMENT • GOOGLE ADSENSE READY SLOT ($slotName)",
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.8.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Reserved responsive ad container ($adUnitPlaceholder). Loads live AdSense units only after publisher verification.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Footer branding displayed across tabs: "Created with passion by MSK & Team"
 */
@Composable
fun MskBrandFooter(
    onOpenResourceSection: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.45f),
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = MskCyanBright,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Created with passion by MSK & Team",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "MSK AI Voice Studio — Your Words. Every Voice. Powered by AI.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf(
                    "about" to "About Us",
                    "privacy" to "Privacy Policy",
                    "terms" to "Terms",
                    "contact" to "Contact"
                ).forEach { (id, label) ->
                    Surface(
                        onClick = { onOpenResourceSection(id) },
                        color = Color.Transparent
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MskCyanBright,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}

fun formatDurationMs(ms: Long): String {
    val totalSeconds = (ms / 1000L).coerceAtLeast(0L)
    val mins = totalSeconds / 60L
    val secs = totalSeconds % 60L
    return String.format(java.util.Locale.US, "%d:%02d", mins, secs)
}
