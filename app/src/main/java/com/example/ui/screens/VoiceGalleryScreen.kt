package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import com.example.data.VoiceCatalog
import com.example.data.VoiceGender
import com.example.data.VoiceProfile
import com.example.ui.VoiceStudioViewModel
import com.example.ui.components.MskBrandFooter
import com.example.ui.theme.MskCyanBright
import com.example.ui.theme.MskElectricBlue
import com.example.ui.theme.MskEmeraldSuccess
import com.example.ui.theme.MskNeonViolet
import java.util.Locale

@Composable
fun VoiceGalleryScreen(
    viewModel: VoiceStudioViewModel,
    searchQuery: String,
    selectedLangCode: String,
    selectedGender: VoiceGender?,
    selectedStyleId: String,
    activeVoice: VoiceProfile,
    previewingVoiceId: String?,
    favoriteIds: Set<String>,
    modifier: Modifier = Modifier
) {
    val filteredVoices = VoiceCatalog.voices.filter { voice ->
        val matchesSearch = searchQuery.isBlank() ||
            voice.name.contains(searchQuery, ignoreCase = true) ||
            voice.languageName.contains(searchQuery, ignoreCase = true) ||
            voice.accent.contains(searchQuery, ignoreCase = true) ||
            voice.primaryStyleName.contains(searchQuery, ignoreCase = true) ||
            voice.description.contains(searchQuery, ignoreCase = true)
        val matchesLang = (selectedLangCode == "ALL" || voice.languageCode == selectedLangCode)
        val matchesGender = (selectedGender == null || voice.gender == selectedGender)
        val matchesStyle = (selectedStyleId == "ALL" || voice.primaryStyleId == selectedStyleId)
        matchesSearch && matchesLang && matchesGender && matchesStyle
    }

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = modifier.fillMaxSize()
    ) {
        item {
            Column {
                Text(
                    text = "Multilingual Voice Library",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Listen to real audio previews for Urdu, English, Hindi, Arabic, Pashto, and 17+ languages before selecting your studio voice.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Search and Filter Controls
        item {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.updateGallerySearch(it) },
                        placeholder = { Text("Search voices by name, language (e.g. Urdu), accent, or style…") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = MskCyanBright)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.updateGallerySearch("") }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear Search")
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("voice_search_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Language Filter Row
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                    ) {
                        FilterChip(
                            selected = selectedLangCode == "ALL",
                            onClick = { viewModel.updateGalleryLanguage("ALL") },
                            label = { Text("All Languages (${VoiceCatalog.voices.size})") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MskElectricBlue,
                                selectedLabelColor = Color.White
                            )
                        )
                        VoiceCatalog.languages.forEach { lang ->
                            FilterChip(
                                selected = selectedLangCode == lang.code,
                                onClick = { viewModel.updateGalleryLanguage(lang.code) },
                                label = { Text("${lang.flagEmoji} ${lang.name}") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MskElectricBlue,
                                    selectedLabelColor = Color.White
                                ),
                                modifier = Modifier.testTag("gallery_lang_${lang.code}")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Gender & Style Filter Row
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                    ) {
                        listOf(
                            null to "All Genders",
                            VoiceGender.FEMALE to "Female Voices",
                            VoiceGender.MALE to "Male Voices"
                        ).forEach { (gender, label) ->
                            FilterChip(
                                selected = selectedGender == gender,
                                onClick = { viewModel.updateGalleryGender(gender) },
                                label = { Text(label) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MskNeonViolet,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }

                        FilterChip(
                            selected = selectedStyleId == "ALL",
                            onClick = { viewModel.updateGalleryStyle("ALL") },
                            label = { Text("All Styles") }
                        )
                        VoiceCatalog.styles.forEach { style ->
                            FilterChip(
                                selected = selectedStyleId == style.id,
                                onClick = { viewModel.updateGalleryStyle(style.id) },
                                label = { Text(style.name) }
                            )
                        }
                    }
                }
            }
        }

        // Individual Voice Cards
        items(filteredVoices, key = { it.id }) { voice ->
            val isSelected = (voice.id == activeVoice.id)
            val isPreviewing = (voice.id == previewingVoiceId)
            val isFavorite = favoriteIds.contains(voice.id)
            val langObj = VoiceCatalog.languages.find { it.code == voice.languageCode }

            Surface(
                shape = RoundedCornerShape(22.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = if (isSelected) 8.dp else 3.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        brush = if (isSelected) {
                            Brush.horizontalGradient(listOf(MskCyanBright, MskElectricBlue, MskNeonViolet))
                        } else {
                            Brush.horizontalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.outline.copy(alpha = 0.45f),
                                    MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                                )
                            )
                        },
                        shape = RoundedCornerShape(22.dp)
                    )
                    .testTag("gallery_voice_card_${voice.id}")
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
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            if (voice.gender == VoiceGender.FEMALE) {
                                                listOf(MskNeonViolet, MskCyanBright)
                                            } else {
                                                listOf(MskElectricBlue, MskCyanBright)
                                            }
                                        )
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.GraphicEq,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = voice.name,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "${langObj?.flagEmoji ?: "🌐"} ${voice.languageName} • ${voice.accent}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        IconButton(
                            onClick = { viewModel.toggleFavoriteVoice(voice.id) },
                            modifier = Modifier.testTag("favorite_btn_${voice.id}")
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = if (isFavorite) "Remove from Favorites" else "Add to Favorites",
                                tint = if (isFavorite) Color(0xFFF43F5E) else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Badges: Gender, Style, Provider
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (voice.gender == VoiceGender.FEMALE) {
                                MskNeonViolet.copy(alpha = 0.2f)
                            } else {
                                MskElectricBlue.copy(alpha = 0.2f)
                            }
                        ) {
                            Text(
                                text = voice.gender.displayName,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (voice.gender == VoiceGender.FEMALE) Color(0xFFD8B4FE) else MskCyanBright,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = "Style: ${voice.primaryStyleName}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MskEmeraldSuccess.copy(alpha = 0.16f)
                        ) {
                            Text(
                                text = voice.provider.badge,
                                style = MaterialTheme.typography.labelSmall,
                                color = MskEmeraldSuccess,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = voice.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Sample Sentence Box
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "“${voice.sampleSentence}”",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                textDirection = TextDirection.Content
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Play Preview & Select Voice Action Buttons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.previewVoice(voice) },
                            enabled = !isPreviewing,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("gallery_preview_btn_${voice.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Headphones,
                                contentDescription = "Play Voice Preview",
                                tint = MskCyanBright,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isPreviewing) "Playing Preview…" else "Play Preview",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Button(
                            onClick = { viewModel.selectVoice(voice, navigateToStudio = true) },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) MskEmeraldSuccess else MskElectricBlue,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("gallery_select_btn_${voice.id}")
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Selected in Studio")
                            } else {
                                Text(
                                    text = "Select Voice",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            MskBrandFooter(
                onOpenResourceSection = { viewModel.selectResourceSection(it) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
