package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Podcasts
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.StudioTab
import com.example.ui.VoiceStudioViewModel
import com.example.ui.components.AdSenseReservedSlotBanner
import com.example.ui.components.MskBrandFooter
import com.example.ui.theme.MskCyanBright
import com.example.ui.theme.MskElectricBlue
import com.example.ui.theme.MskEmeraldSuccess
import com.example.ui.theme.MskNeonViolet

data class StudioToolCardModel(
    val id: String,
    val title: String,
    val subtitle: String,
    val badge: String,
    val icon: ImageVector,
    val actionLabel: String
)

@Composable
fun AiToolsScreen(
    viewModel: VoiceStudioViewModel,
    aiToolBusy: Boolean,
    modifier: Modifier = Modifier
) {
    var customTopic by remember {
        mutableStateOf("How Artificial Intelligence is empowering Urdu and global storytellers")
    }

    val tools = listOf(
        StudioToolCardModel(
            id = "urdu_tts",
            title = "Urdu Text to Speech (اردو & Roman Urdu)",
            subtitle = "Dedicated Pakistani & literary Urdu voices (Zoya, Hamza, Ayesha, Bilal) supporting both Urdu script and Roman Urdu.",
            badge = "Flagship Urdu Engine",
            icon = Icons.Default.Translate,
            actionLabel = "Open Urdu Studio"
        ),
        StudioToolCardModel(
            id = "english_tts",
            title = "English Text to Speech",
            subtitle = "American, British, and International studio voices for creators, YouTube, audiobooks, and corporate narration.",
            badge = "Global English",
            icon = Icons.Default.RecordVoiceOver,
            actionLabel = "Open English Studio"
        ),
        StudioToolCardModel(
            id = "hindi_tts",
            title = "Hindi Text to Speech (हिन्दी)",
            subtitle = "Natural Devanagari Hindi male & female voices (Ananya & Kabir) for stories, education, and media.",
            badge = "Hindi Neural",
            icon = Icons.Default.Language,
            actionLabel = "Open Hindi Studio"
        ),
        StudioToolCardModel(
            id = "pashto_tts",
            title = "Pashto Text to Speech (پښتو)",
            subtitle = "Authentic Pashto male & female voices (Zalan & Spogmai) for Peshawar/Kabul standard scripts.",
            badge = "Pashto Neural",
            icon = Icons.Default.Language,
            actionLabel = "Open Pashto Studio"
        ),
        StudioToolCardModel(
            id = "story",
            title = "AI Story Narration Generator",
            subtitle = "Generates an expressive storytelling script on your chosen topic and configures Storytelling voice prosody.",
            badge = "Gemini 3.5 + TTS",
            icon = Icons.AutoMirrored.Filled.MenuBook,
            actionLabel = "Generate Story Script"
        ),
        StudioToolCardModel(
            id = "podcast",
            title = "Podcast Voice Generator",
            subtitle = "Writes a warm, engaging podcast host opening monologue and selects Podcast Host studio microphone style.",
            badge = "Creator Studio",
            icon = Icons.Default.Podcasts,
            actionLabel = "Create Podcast Script"
        ),
        StudioToolCardModel(
            id = "news",
            title = "News Voice Generator",
            subtitle = "Creates an articulate prime-time broadcast news bulletin in your active language with News Presenter cadence.",
            badge = "Broadcast Ready",
            icon = Icons.Default.Newspaper,
            actionLabel = "Create News Bulletin"
        ),
        StudioToolCardModel(
            id = "educational",
            title = "Educational Narration",
            subtitle = "Drafts a structured e-learning tutorial script and switches to patient Educational voice pacing.",
            badge = "E-Learning",
            icon = Icons.Default.School,
            actionLabel = "Create Lesson Script"
        ),
        StudioToolCardModel(
            id = "long_text",
            title = "Long Text Narration (Chunked Combiner)",
            subtitle = "Splits long articles or chapters (up to 4,000 chars) into ordered sentence chunks and combines them into one seamless WAV file.",
            badge = "Multi-Chunk Engine",
            icon = Icons.Default.AudioFile,
            actionLabel = "Enable Long Text Mode"
        ),
        StudioToolCardModel(
            id = "cleanup",
            title = "Text Cleanup & Punctuation",
            subtitle = "Automatically normalizes whitespace, breathing commas, and Urdu/Hindi/English sentence marks for natural speech flow.",
            badge = "Smart Prosody",
            icon = Icons.Default.AutoFixHigh,
            actionLabel = "Clean Active Studio Text"
        ),
        StudioToolCardModel(
            id = "roman_to_urdu",
            title = "Roman Urdu → Urdu Script (اردو) Converter",
            subtitle = "Transliterates Roman Urdu sentences into native Urdu script so TTS engines pronounce every word accurately.",
            badge = "Urdu Script AI",
            icon = Icons.Default.AutoAwesome,
            actionLabel = "Convert Active Text"
        ),
        StudioToolCardModel(
            id = "voice_library",
            title = "Voice Preview Library",
            subtitle = "Browse 20+ male and female voices across 17 languages with real cached sample previews.",
            badge = "17 Languages",
            icon = Icons.Default.LibraryMusic,
            actionLabel = "Browse Voice Library"
        ),
        StudioToolCardModel(
            id = "speed_prosody",
            title = "Speech Speed, Pitch & Format Studio",
            subtitle = "Fine-tune speaking rate (0.5x–2.0x), pitch contour, volume gain, and WAV/MP3 output formats.",
            badge = "DSP Controls",
            icon = Icons.Default.Speed,
            actionLabel = "Open Prosody Controls"
        ),
        StudioToolCardModel(
            id = "history_downloads",
            title = "Downloadable Audio History",
            subtitle = "Replay, re-download, or share all your previously generated voiceovers stored on your device.",
            badge = "Offline Ready",
            icon = Icons.Default.History,
            actionLabel = "Open Audio History"
        )
    )

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = modifier.fillMaxSize()
    ) {
        item {
            Column {
                Text(
                    text = "AI Voice & Script Tools",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Specialized multilingual narration generators, Urdu/Roman Urdu tools, long-text chunking, and audio utilities.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Topic Prompt Box for AI Script Generators (Story, Podcast, News, Educational)
        item {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        brush = Brush.horizontalGradient(listOf(MskCyanBright, MskNeonViolet)),
                        shape = RoundedCornerShape(20.dp)
                    )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Topic for AI Story, Podcast, News & Educational Generators",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MskCyanBright
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = customTopic,
                        onValueChange = { customTopic = it },
                        placeholder = { Text("Enter a topic in English, Urdu, Roman Urdu, or Hindi…") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("ai_tool_topic_input")
                    )
                }
            }
        }

        items(tools, key = { it.id }) { tool ->
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 3.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.45f),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .testTag("tool_card_${tool.id}")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
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
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(MskElectricBlue.copy(alpha = 0.18f))
                            ) {
                                Icon(
                                    imageVector = tool.icon,
                                    contentDescription = null,
                                    tint = MskCyanBright
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = tool.title,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MskNeonViolet.copy(alpha = 0.2f),
                                    modifier = Modifier.padding(top = 4.dp)
                                ) {
                                    Text(
                                        text = tool.badge,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Color(0xFFD8B4FE),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = tool.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            when (tool.id) {
                                "voice_library" -> viewModel.selectTab(StudioTab.VOICES)
                                "speed_prosody" -> viewModel.selectTab(StudioTab.STUDIO)
                                "history_downloads" -> viewModel.selectTab(StudioTab.DASHBOARD)
                                else -> viewModel.runAiStudioTool(tool.id, customTopic)
                            }
                        },
                        enabled = !aiToolBusy,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MskElectricBlue,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("launch_tool_btn_${tool.id}")
                    ) {
                        Text(
                            text = if (aiToolBusy) "Processing with AI…" else tool.actionLabel,
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }

        // Ethical Voice Cloning & Enterprise API Transparency Notice
        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MskEmeraldSuccess.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = MskEmeraldSuccess,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Voice Safety & Custom Voice Cloning Policy",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "To protect creators and speakers, MSK AI Voice Studio strictly prohibits unauthorized voice cloning or deepfakes. Custom voice cloning requires a separate verified enterprise API agreement and recorded cryptographic speaker consent.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        item {
            AdSenseReservedSlotBanner(slotName = "TOOLS_HUB_FOOTER")
        }

        item {
            MskBrandFooter(
                onOpenResourceSection = { viewModel.selectResourceSection(it) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
