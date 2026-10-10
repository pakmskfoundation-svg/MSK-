package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.StudioTab
import com.example.ui.VoiceStudioViewModel
import com.example.ui.components.MskAnimatedSoundwaveLogo
import com.example.ui.screens.AccountAndHistoryScreen
import com.example.ui.screens.AiToolsScreen
import com.example.ui.screens.ResourcesAndLegalScreen
import com.example.ui.screens.StudioHomeScreen
import com.example.ui.screens.VoiceGalleryScreen
import com.example.ui.theme.MskCyanBright
import com.example.ui.theme.MskElectricBlue
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val studioViewModel: VoiceStudioViewModel = viewModel()
            val isDarkMode by studioViewModel.isDarkMode.collectAsStateWithLifecycle()

            MyApplicationTheme(darkTheme = isDarkMode) {
                MskVoiceStudioApp(
                    viewModel = studioViewModel,
                    isDarkMode = isDarkMode
                )
            }
        }
    }
}

@Composable
fun MskVoiceStudioApp(
    viewModel: VoiceStudioViewModel,
    isDarkMode: Boolean
) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val inputText by viewModel.inputText.collectAsStateWithLifecycle()
    val selectedLanguage by viewModel.selectedLanguage.collectAsStateWithLifecycle()
    val autoDetect by viewModel.autoDetectLanguage.collectAsStateWithLifecycle()
    val selectedGenderFilter by viewModel.selectedGenderFilter.collectAsStateWithLifecycle()
    val selectedVoice by viewModel.selectedVoice.collectAsStateWithLifecycle()
    val selectedStyle by viewModel.selectedStyle.collectAsStateWithLifecycle()
    val selectedProvider by viewModel.selectedProvider.collectAsStateWithLifecycle()
    val speed by viewModel.speakingSpeed.collectAsStateWithLifecycle()
    val pitch by viewModel.speakingPitch.collectAsStateWithLifecycle()
    val volume by viewModel.speakingVolume.collectAsStateWithLifecycle()
    val longTextMode by viewModel.longTextMode.collectAsStateWithLifecycle()
    val downloadFormat by viewModel.downloadFormat.collectAsStateWithLifecycle()
    val generationProgress by viewModel.generationProgress.collectAsStateWithLifecycle()
    val playbackState by viewModel.playbackState.collectAsStateWithLifecycle()
    val userAccount by viewModel.userAccount.collectAsStateWithLifecycle()
    val previewingVoiceId by viewModel.previewingVoiceId.collectAsStateWithLifecycle()
    val aiToolBusy by viewModel.aiToolBusy.collectAsStateWithLifecycle()
    val bannerNotice by viewModel.bannerNotice.collectAsStateWithLifecycle()
    val downloadSuccessBanner by viewModel.downloadSuccessBanner.collectAsStateWithLifecycle()

    val gallerySearch by viewModel.gallerySearchQuery.collectAsStateWithLifecycle()
    val galleryLang by viewModel.galleryLanguageCode.collectAsStateWithLifecycle()
    val galleryGender by viewModel.galleryGender.collectAsStateWithLifecycle()
    val galleryStyle by viewModel.galleryStyleId.collectAsStateWithLifecycle()
    val favoriteIds by viewModel.favoriteVoiceIds.collectAsStateWithLifecycle()
    val historyList by viewModel.historyList.collectAsStateWithLifecycle()
    val selectedResourceSection by viewModel.selectedResourceSection.collectAsStateWithLifecycle()

    // BackHandler for secondary tabs to return to the main Studio Home tab
    BackHandler(enabled = currentTab != StudioTab.STUDIO) {
        viewModel.selectTab(StudioTab.STUDIO)
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isWideScreen = maxWidth >= 700.dp

        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            topBar = {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    tonalElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            MskAnimatedSoundwaveLogo(
                                isAnimating = generationProgress.isGenerating || playbackState.isPlaying,
                                modifier = Modifier.weight(1f)
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                IconButton(
                                    onClick = { viewModel.toggleThemeMode() },
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                                        .testTag("theme_toggle_button")
                                ) {
                                    Icon(
                                        imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                        contentDescription = if (isDarkMode) "Switch to Light Mode" else "Switch to Dark Mode",
                                        tint = MskCyanBright
                                    )
                                }
                            }
                        }

                        // Dismissible Status / Notice Banner
                        AnimatedVisibility(visible = !bannerNotice.isNullOrBlank()) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MskElectricBlue.copy(alpha = 0.18f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 4.dp)
                                    .border(1.dp, MskCyanBright.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                    .testTag("status_notice_banner")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = MskCyanBright,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = bannerNotice ?: "",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier.weight(1f)
                                    )
                                    IconButton(
                                        onClick = { viewModel.dismissBannerNotice() },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Dismiss Notice",
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            },
            bottomBar = {
                if (!isWideScreen) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 8.dp
                    ) {
                        StudioTab.entries.forEach { tab ->
                            val selected = (currentTab == tab)
                            NavigationBarItem(
                                selected = selected,
                                onClick = { viewModel.selectTab(tab) },
                                icon = {
                                    Icon(
                                        imageVector = tab.icon(),
                                        contentDescription = tab.title
                                    )
                                },
                                label = {
                                    Text(
                                        text = tab.title,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Color.White,
                                    selectedTextColor = MskCyanBright,
                                    indicatorColor = MskElectricBlue
                                ),
                                modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (isWideScreen) {
                    NavigationRail(
                        containerColor = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        StudioTab.entries.forEach { tab ->
                            val selected = (currentTab == tab)
                            NavigationRailItem(
                                selected = selected,
                                onClick = { viewModel.selectTab(tab) },
                                icon = {
                                    Icon(
                                        imageVector = tab.icon(),
                                        contentDescription = tab.title
                                    )
                                },
                                label = { Text(tab.title) },
                                modifier = Modifier.testTag("rail_tab_${tab.name.lowercase()}")
                            )
                        }
                    }
                }

                Box(
                    contentAlignment = Alignment.TopCenter,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val contentModifier = Modifier
                        .fillMaxSize()
                        .widthIn(max = 840.dp)

                    when (currentTab) {
                        StudioTab.STUDIO -> StudioHomeScreen(
                            viewModel = viewModel,
                            inputText = inputText,
                            selectedLanguage = selectedLanguage,
                            autoDetect = autoDetect,
                            selectedGenderFilter = selectedGenderFilter,
                            selectedVoice = selectedVoice,
                            selectedStyle = selectedStyle,
                            selectedProvider = selectedProvider,
                            speed = speed,
                            pitch = pitch,
                            volume = volume,
                            longTextMode = longTextMode,
                            downloadFormat = downloadFormat,
                            generationProgress = generationProgress,
                            playbackState = playbackState,
                            userAccount = userAccount,
                            previewingVoiceId = previewingVoiceId,
                            aiToolBusy = aiToolBusy,
                            downloadSuccessBanner = downloadSuccessBanner,
                            modifier = contentModifier
                        )
                        StudioTab.VOICES -> VoiceGalleryScreen(
                            viewModel = viewModel,
                            searchQuery = gallerySearch,
                            selectedLangCode = galleryLang,
                            selectedGender = galleryGender,
                            selectedStyleId = galleryStyle,
                            activeVoice = selectedVoice,
                            previewingVoiceId = previewingVoiceId,
                            favoriteIds = favoriteIds,
                            modifier = contentModifier
                        )
                        StudioTab.TOOLS -> AiToolsScreen(
                            viewModel = viewModel,
                            aiToolBusy = aiToolBusy,
                            modifier = contentModifier
                        )
                        StudioTab.DASHBOARD -> AccountAndHistoryScreen(
                            viewModel = viewModel,
                            userAccount = userAccount,
                            historyList = historyList,
                            favoriteIds = favoriteIds,
                            modifier = contentModifier
                        )
                        StudioTab.RESOURCES -> ResourcesAndLegalScreen(
                            viewModel = viewModel,
                            selectedSection = selectedResourceSection,
                            modifier = contentModifier
                        )
                    }
                }
            }
        }
    }
}

private fun StudioTab.icon(): ImageVector = when (this) {
    StudioTab.STUDIO -> Icons.Default.GraphicEq
    StudioTab.VOICES -> Icons.Default.RecordVoiceOver
    StudioTab.TOOLS -> Icons.Default.AutoAwesome
    StudioTab.DASHBOARD -> Icons.Default.AccountCircle
    StudioTab.RESOURCES -> Icons.AutoMirrored.Filled.MenuBook
}
