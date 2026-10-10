package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.audio.AudioPlaybackState
import com.example.data.SupportedLanguage
import com.example.data.TtsProviderType
import com.example.data.UserAccountEntity
import com.example.data.VoiceCatalog
import com.example.data.VoiceGender
import com.example.data.VoiceProfile
import com.example.data.VoiceStyle
import com.example.ui.GenerationProgressState
import com.example.ui.StudioTab
import com.example.ui.VoiceStudioViewModel
import com.example.ui.components.AdSenseReservedSlotBanner
import com.example.ui.components.AudioGenerationAnimationCard
import com.example.ui.components.MskBrandFooter
import com.example.ui.components.StudioWaveformAudioPlayerCard
import com.example.ui.theme.MskCyanBright
import com.example.ui.theme.MskElectricBlue
import com.example.ui.theme.MskEmeraldSuccess
import com.example.ui.theme.MskNeonViolet
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StudioHomeScreen(
    viewModel: VoiceStudioViewModel,
    inputText: String,
    selectedLanguage: SupportedLanguage,
    autoDetect: Boolean,
    selectedGenderFilter: VoiceGender?,
    selectedVoice: VoiceProfile,
    selectedStyle: VoiceStyle,
    selectedProvider: TtsProviderType,
    speed: Float,
    pitch: Float,
    volume: Float,
    longTextMode: Boolean,
    downloadFormat: String,
    generationProgress: GenerationProgressState,
    playbackState: AudioPlaybackState,
    userAccount: UserAccountEntity,
    previewingVoiceId: String?,
    aiToolBusy: Boolean,
    downloadSuccessBanner: String?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var showAllLanguages by remember { mutableStateOf(false) }
    var showAdvancedControls by remember { mutableStateOf(true) }

    val maxChars = if (longTextMode) 4000 else 1500
    val remainingDailyChars = (userAccount.dailyAllowanceChars - userAccount.usedCharsToday).coerceAtLeast(0)
    val matchingVoices = VoiceCatalog.voices.filter { v ->
        v.languageCode == selectedLanguage.code &&
            (selectedGenderFilter == null || v.gender == selectedGenderFilter)
    }.ifEmpty {
        VoiceCatalog.voices.filter { it.languageCode == selectedLanguage.code }
    }.ifEmpty {
        VoiceCatalog.voices
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // 1. Futuristic 3D Hero Card with Large Heading
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(26.dp))
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(
                        listOf(MskCyanBright, MskElectricBlue, MskNeonViolet)
                    ),
                    shape = RoundedCornerShape(26.dp)
                )
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_hero_soundwave_1791630560809),
                contentDescription = "MSK AI Voice Studio 3D Soundwave Laboratory Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0x99060B16),
                                Color(0xD9060B16),
                                Color(0xF5060B16)
                            )
                        )
                    )
                    .padding(18.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MskElectricBlue.copy(alpha = 0.28f),
                            modifier = Modifier.border(1.dp, MskCyanBright.copy(alpha = 0.6f), CircleShape)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.GraphicEq,
                                    contentDescription = null,
                                    tint = MskCyanBright,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "MSK AI VOICE STUDIO • MULTILINGUAL",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )
                            }
                        }

                        Surface(
                            shape = CircleShape,
                            color = Color(0xAA0F172A),
                            onClick = { viewModel.selectTab(StudioTab.DASHBOARD) }
                        ) {
                            Text(
                                text = "$remainingDailyChars free chars left",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = MskCyanBright,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Column {
                        Text(
                            text = "Turn Text into Natural-Sounding Speech.",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Urdu, Roman Urdu, English, Hindi, Arabic, Pashto & 18+ AI Voices • Instant WAV/MP3 Downloads",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFCBD5E1)
                        )
                    }
                }
            }
        }

        // Verified Download Success Banner (only shown after actual download completes)
        AnimatedVisibility(visible = !downloadSuccessBanner.isNullOrBlank()) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MskEmeraldSuccess.copy(alpha = 0.16f),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MskEmeraldSuccess, RoundedCornerShape(16.dp))
                    .testTag("download_success_banner")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Download Completed",
                        tint = MskEmeraldSuccess
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = downloadSuccessBanner ?: "",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Compatible with Android, iPhone, tablet, and desktop audio players.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    TextButton(onClick = { viewModel.dismissDownloadSuccessBanner() }) {
                        Text("OK")
                    }
                }
            }
        }

        // 2. Main Multilingual Text Editor Card
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(24.dp)
                )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Top Bar: Language Badge & Auto-Detect Toggle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${selectedLanguage.flagEmoji} ${selectedLanguage.name}",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MskElectricBlue.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = if (autoDetect) "Auto-Detect ON" else "Manual Lang",
                                style = MaterialTheme.typography.labelSmall,
                                color = MskCyanBright,
                                modifier = Modifier
                                    .clickable { viewModel.toggleAutoDetectLanguage(!autoDetect) }
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    // Character Counter
                    Text(
                        text = "${inputText.length} / $maxChars chars",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (inputText.length > maxChars * 0.9) Color(0xFFF59E0B) else MskCyanBright,
                        modifier = Modifier.testTag("character_counter_text")
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Quick Script Sample & Urdu / Roman Urdu Switcher Bar
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                ) {
                    Surface(
                        onClick = { viewModel.selectLanguage(selectedLanguage, loadSampleText = true) },
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = "Load ${selectedLanguage.name} Sample",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }

                    Surface(
                        onClick = { viewModel.loadRomanUrduSample() },
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.testTag("roman_urdu_sample_chip")
                    ) {
                        Text(
                            text = "🇵🇰 Roman Urdu Sample",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = MskCyanBright,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }

                    Surface(
                        onClick = { viewModel.runAiStudioTool("roman_to_urdu") },
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.testTag("convert_roman_to_urdu_chip")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Translate,
                                contentDescription = null,
                                tint = MskNeonViolet,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (aiToolBusy) "Converting…" else "Roman → اردو Script",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Surface(
                        onClick = { viewModel.runAiStudioTool("cleanup") },
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.testTag("ai_text_cleanup_chip")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoFixHigh,
                                contentDescription = null,
                                tint = MskCyanBright,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Fix Punctuation",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Multilingual Text Field (Supports RTL Urdu/Arabic/Pashto/Persian & LTR scripts)
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { viewModel.updateInputText(it) },
                    placeholder = {
                        Text(
                            text = "Type or paste your text in Urdu (اردو), Roman Urdu, English, Hindi, Arabic, Pashto, or any supported language…",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.65f)
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        textDirection = TextDirection.Content
                    ),
                    minLines = 4,
                    maxLines = 9,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MskCyanBright,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.22f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("tts_text_editor")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Copy Text, Clear Text, & Long Text Narration Toggle Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboard.setPrimaryClip(ClipData.newPlainText("MSK AI Voice Text", inputText))
                                Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("copy_text_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy Text",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Copy", style = MaterialTheme.typography.labelMedium)
                        }

                        OutlinedButton(
                            onClick = { viewModel.clearText() },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("clear_text_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear Text",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Clear", style = MaterialTheme.typography.labelMedium)
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Long Text (4k)",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Switch(
                            checked = longTextMode,
                            onCheckedChange = { viewModel.setLongTextMode(it) },
                            modifier = Modifier.testTag("long_text_mode_switch")
                        )
                    }
                }
            }
        }

        // 3. Language, Gender, Voice & Style Selector Card
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(24.dp)
                )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Language Selector Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = null,
                            tint = MskCyanBright,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "1. Select Language (${VoiceCatalog.languages.size} Supported)",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    TextButton(onClick = { showAllLanguages = !showAllLanguages }) {
                        Text(if (showAllLanguages) "Compact View" else "Show All 17")
                    }
                }

                val displayedLanguages = if (showAllLanguages) {
                    VoiceCatalog.languages
                } else {
                    VoiceCatalog.languages.take(7)
                }

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    displayedLanguages.forEach { lang ->
                        val isSelected = (lang.code == selectedLanguage.code)
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.selectLanguage(lang, loadSampleText = false) },
                            label = {
                                Text(
                                    text = "${lang.flagEmoji} ${lang.name}",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MskElectricBlue,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("lang_chip_${lang.code}")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Male / Female Voice Filter
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.RecordVoiceOver,
                            contentDescription = null,
                            tint = MskNeonViolet,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "2. Voice Gender & Artist",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(
                            null to "All",
                            VoiceGender.FEMALE to "Female",
                            VoiceGender.MALE to "Male"
                        ).forEach { (gender, label) ->
                            val active = (selectedGenderFilter == gender)
                            FilterChip(
                                selected = active,
                                onClick = { viewModel.selectGenderFilter(gender) },
                                label = { Text(label, style = MaterialTheme.typography.labelSmall) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MskNeonViolet,
                                    selectedLabelColor = Color.White
                                ),
                                modifier = Modifier.testTag("gender_filter_${label.lowercase(Locale.ROOT)}")
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Horizontal Voice Cards for Selected Language
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                ) {
                    matchingVoices.forEach { voice ->
                        val isChosen = (voice.id == selectedVoice.id)
                        Surface(
                            onClick = { viewModel.selectVoice(voice) },
                            shape = RoundedCornerShape(16.dp),
                            color = if (isChosen) {
                                MskElectricBlue.copy(alpha = 0.2f)
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
                            },
                            modifier = Modifier
                                .width(225.dp)
                                .border(
                                    width = if (isChosen) 2.dp else 1.dp,
                                    color = if (isChosen) MskCyanBright else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .testTag("studio_voice_card_${voice.id}")
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = voice.name,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (voice.gender == VoiceGender.FEMALE) {
                                            MskNeonViolet.copy(alpha = 0.25f)
                                        } else {
                                            MskElectricBlue.copy(alpha = 0.25f)
                                        }
                                    ) {
                                        Text(
                                            text = voice.gender.displayName,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (voice.gender == VoiceGender.FEMALE) Color(0xFFD8B4FE) else MskCyanBright,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${voice.accent} • ${voice.primaryStyleName}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = voice.provider.badge,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = MskCyanBright
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 3. Speaking Style Selector
                Text(
                    text = "3. Speaking Style (${selectedStyle.name})",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                ) {
                    VoiceCatalog.styles.forEach { style ->
                        val active = (style.id == selectedStyle.id)
                        FilterChip(
                            selected = active,
                            onClick = { viewModel.selectStyle(style) },
                            label = {
                                Text(
                                    text = style.name,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MskCyanBright.copy(alpha = 0.22f),
                                selectedLabelColor = MskCyanBright
                            ),
                            modifier = Modifier.testTag("style_chip_${style.id}")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 4. AI Speech Engine Provider Toggle (Cloud Neural vs Free On-Device Fallback)
                Text(
                    text = "4. Speech Synthesis Engine",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TtsProviderType.entries.forEach { provider ->
                        val active = (selectedProvider == provider)
                        Surface(
                            onClick = { viewModel.selectProvider(provider) },
                            shape = RoundedCornerShape(14.dp),
                            color = if (active) MskElectricBlue.copy(alpha = 0.18f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            modifier = Modifier
                                .weight(1f)
                                .border(
                                    width = if (active) 1.5.dp else 1.dp,
                                    color = if (active) MskCyanBright else MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .testTag("provider_card_${provider.id}")
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = provider.title,
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                    color = if (active) MskCyanBright else MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = provider.badge,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 5. Speed, Pitch & Volume Sliders
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = null,
                            tint = MskCyanBright,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "5. Speed, Pitch & Volume Controls",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    TextButton(onClick = {
                        viewModel.updateSpeed(1.0f)
                        viewModel.updatePitch(1.0f)
                        viewModel.updateVolume(1.0f)
                    }) {
                        Text("Reset (1.0x)")
                    }
                }

                AnimatedVisibility(visible = showAdvancedControls) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        // Speed Slider
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = String.format(Locale.US, "Speed: %.2fx", speed),
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.width(96.dp)
                            )
                            Slider(
                                value = speed,
                                onValueChange = { viewModel.updateSpeed(it) },
                                valueRange = 0.5f..2.0f,
                                colors = SliderDefaults.colors(
                                    thumbColor = MskCyanBright,
                                    activeTrackColor = MskElectricBlue
                                ),
                                modifier = Modifier.weight(1f).testTag("speed_slider")
                            )
                        }

                        // Pitch Slider
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.GraphicEq,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = String.format(Locale.US, "Pitch: %.2fx", pitch),
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.width(96.dp)
                            )
                            Slider(
                                value = pitch,
                                onValueChange = { viewModel.updatePitch(it) },
                                valueRange = 0.5f..1.8f,
                                colors = SliderDefaults.colors(
                                    thumbColor = MskNeonViolet,
                                    activeTrackColor = MskNeonViolet
                                ),
                                modifier = Modifier.weight(1f).testTag("pitch_slider")
                            )
                        }

                        // Volume Slider
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Headphones,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = String.format(Locale.US, "Volume: %d%%", (volume * 100).toInt()),
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.width(96.dp)
                            )
                            Slider(
                                value = volume,
                                onValueChange = { viewModel.updateVolume(it) },
                                valueRange = 0.1f..1.0f,
                                colors = SliderDefaults.colors(
                                    thumbColor = MskEmeraldSuccess,
                                    activeTrackColor = MskEmeraldSuccess
                                ),
                                modifier = Modifier.weight(1f).testTag("volume_slider")
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 6. Voice Preview & Generate Audio Primary Action Buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = { viewModel.previewVoice(selectedVoice) },
                        enabled = !generationProgress.isGenerating && previewingVoiceId == null,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .height(54.dp)
                            .testTag("preview_voice_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Headphones,
                            contentDescription = "Preview Selected Voice",
                            tint = MskCyanBright,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (previewingVoiceId == selectedVoice.id) "Previewing…" else "Voice Preview",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Button(
                        onClick = { viewModel.generateAudio() },
                        enabled = !generationProgress.isGenerating,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MskElectricBlue,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(54.dp)
                            .testTag("generate_audio_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Generate Audio",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (generationProgress.isGenerating) "Generating…" else "Generate Audio",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }

        // 4. Animated Circular Sound-Wave Progress Card (Visible during active generation)
        AnimatedVisibility(visible = generationProgress.isGenerating) {
            AudioGenerationAnimationCard(
                progressState = generationProgress,
                voiceName = selectedVoice.name,
                languageName = selectedLanguage.name
            )
        }

        // 5. Studio Audio Player with Waveform Visualization & Download Button
        AnimatedVisibility(visible = playbackState.isLoaded) {
            StudioWaveformAudioPlayerCard(
                playbackState = playbackState,
                downloadFormat = downloadFormat,
                onFormatChange = { viewModel.setDownloadFormat(it) },
                onTogglePlayPause = { viewModel.togglePlayPause() },
                onReplay = { viewModel.replayAudio() },
                onSeekFraction = { viewModel.seekAudio(it) },
                onVolumeChange = { viewModel.updateVolume(it) },
                onDownloadClick = { viewModel.downloadCurrentAudio() },
                onShareClick = { viewModel.shareCurrentAudio(title = playbackState.currentTitle) }
            )
        }

        // 6. Free Usage & Fair Allowance Transparency Card
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MskCyanBright,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${userAccount.authMethod} • Daily Cloud Allowance",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    Text(
                        text = "${userAccount.usedCharsToday} / ${userAccount.dailyAllowanceChars} chars",
                        style = MaterialTheme.typography.labelMedium,
                        color = MskCyanBright
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                val usageFrac = (userAccount.usedCharsToday.toFloat() / userAccount.dailyAllowanceChars.toFloat().coerceAtLeast(1f))
                    .coerceIn(0f, 1f)
                LinearProgressIndicator(
                    progress = { usageFrac },
                    color = MskCyanBright,
                    trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "No payment card or login required to generate and download audio. On-Device Speech Engine fallback is always free without character deductions.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 7. AdSense-Ready Reserved Slot (Safely separated from action buttons)
        AdSenseReservedSlotBanner(slotName = "HOMEPAGE_BOTTOM_LEADERBOARD")

        // 8. Brand Footer ("Created with passion by MSK & Team")
        MskBrandFooter(
            onOpenResourceSection = { sectionId -> viewModel.selectResourceSection(sectionId) }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}
