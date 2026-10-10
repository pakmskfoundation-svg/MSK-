package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.AudioDownloadHelper
import com.example.audio.AudioPlaybackState
import com.example.audio.AudioPlayerController
import com.example.audio.GeminiTtsClient
import com.example.audio.OnDeviceTtsManager
import com.example.audio.TtsSynthesisResult
import com.example.data.AudioHistoryEntity
import com.example.data.SupportedLanguage
import com.example.data.TtsProviderType
import com.example.data.UserAccountEntity
import com.example.data.VoiceCatalog
import com.example.data.VoiceGender
import com.example.data.VoiceProfile
import com.example.data.VoiceStudioDatabase
import com.example.data.VoiceStudioRepository
import com.example.data.VoiceStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File

enum class StudioTab(val title: String) {
    STUDIO("Studio"),
    VOICES("Voice Library"),
    TOOLS("AI Tools"),
    DASHBOARD("Dashboard"),
    RESOURCES("Guide & Info")
}

data class GenerationProgressState(
    val isGenerating: Boolean = false,
    val stageMessage: String = "",
    val completedChunks: Int = 0,
    val totalChunks: Int = 1
)

class VoiceStudioViewModel(application: Application) : AndroidViewModel(application) {

    private val database = VoiceStudioDatabase.getInstance(application)
    private val repository = VoiceStudioRepository(database.dao())
    private val ttsManager = OnDeviceTtsManager(application)
    private val playerController = AudioPlayerController(viewModelScope)

    val playbackState: StateFlow<AudioPlaybackState> = playerController.state

    val historyList: StateFlow<List<AudioHistoryEntity>> = repository.historyFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteVoiceIds: StateFlow<Set<String>> = repository.favoritesFlow
        .map { list -> list.map { it.voiceId }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    val userAccount: StateFlow<UserAccountEntity> = repository.accountFlow
        .map { it ?: UserAccountEntity() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserAccountEntity())

    // Theme & Navigation State
    private val _isDarkMode = MutableStateFlow(true)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _currentTab = MutableStateFlow(StudioTab.STUDIO)
    val currentTab: StateFlow<StudioTab> = _currentTab.asStateFlow()

    // Studio Editor & Voice Configuration State
    private val _inputText = MutableStateFlow(
        "السلام علیکم! ایم ایس کے اے آئی وائس اسٹوڈیو میں خوش آمدید۔ آپ اردو، رومن اردو، انگریزی یا کسی بھی زبان میں قدرتی آواز تیار کر سکتے ہیں۔"
    )
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    private val _selectedLanguage = MutableStateFlow(VoiceCatalog.languages.first()) // Urdu default
    val selectedLanguage: StateFlow<SupportedLanguage> = _selectedLanguage.asStateFlow()

    private val _autoDetectLanguage = MutableStateFlow(true)
    val autoDetectLanguage: StateFlow<Boolean> = _autoDetectLanguage.asStateFlow()

    private val _selectedGenderFilter = MutableStateFlow<VoiceGender?>(null) // null = Both Male & Female
    val selectedGenderFilter: StateFlow<VoiceGender?> = _selectedGenderFilter.asStateFlow()

    private val _selectedVoice = MutableStateFlow(VoiceCatalog.voices.first())
    val selectedVoice: StateFlow<VoiceProfile> = _selectedVoice.asStateFlow()

    private val _selectedStyle = MutableStateFlow(VoiceCatalog.styles.first())
    val selectedStyle: StateFlow<VoiceStyle> = _selectedStyle.asStateFlow()

    private val _selectedProvider = MutableStateFlow(TtsProviderType.GEMINI_NEURAL)
    val selectedProvider: StateFlow<TtsProviderType> = _selectedProvider.asStateFlow()

    private val _speakingSpeed = MutableStateFlow(1.0f)
    val speakingSpeed: StateFlow<Float> = _speakingSpeed.asStateFlow()

    private val _speakingPitch = MutableStateFlow(1.0f)
    val speakingPitch: StateFlow<Float> = _speakingPitch.asStateFlow()

    private val _speakingVolume = MutableStateFlow(1.0f)
    val speakingVolume: StateFlow<Float> = _speakingVolume.asStateFlow()

    private val _longTextMode = MutableStateFlow(false)
    val longTextMode: StateFlow<Boolean> = _longTextMode.asStateFlow()

    private val _downloadFormat = MutableStateFlow("wav")
    val downloadFormat: StateFlow<String> = _downloadFormat.asStateFlow()

    // Generation Progress & Status Messages
    private val _generationProgress = MutableStateFlow(GenerationProgressState())
    val generationProgress: StateFlow<GenerationProgressState> = _generationProgress.asStateFlow()

    private val _previewingVoiceId = MutableStateFlow<String?>(null)
    val previewingVoiceId: StateFlow<String?> = _previewingVoiceId.asStateFlow()

    private val _aiToolBusy = MutableStateFlow(false)
    val aiToolBusy: StateFlow<Boolean> = _aiToolBusy.asStateFlow()

    private val _bannerNotice = MutableStateFlow<String?>(null)
    val bannerNotice: StateFlow<String?> = _bannerNotice.asStateFlow()

    private val _downloadSuccessBanner = MutableStateFlow<String?>(null)
    val downloadSuccessBanner: StateFlow<String?> = _downloadSuccessBanner.asStateFlow()

    // Voice Gallery Filters
    private val _gallerySearchQuery = MutableStateFlow("")
    val gallerySearchQuery: StateFlow<String> = _gallerySearchQuery.asStateFlow()

    private val _galleryLanguageCode = MutableStateFlow("ALL")
    val galleryLanguageCode: StateFlow<String> = _galleryLanguageCode.asStateFlow()

    private val _galleryGender = MutableStateFlow<VoiceGender?>(null)
    val galleryGender: StateFlow<VoiceGender?> = _galleryGender.asStateFlow()

    private val _galleryStyleId = MutableStateFlow("ALL")
    val galleryStyleId: StateFlow<String> = _galleryStyleId.asStateFlow()

    // Active Info Page in Resources Tab
    private val _selectedResourceSection = MutableStateFlow("about")
    val selectedResourceSection: StateFlow<String> = _selectedResourceSection.asStateFlow()

    init {
        viewModelScope.launch {
            repository.ensureAccountInitialized()
        }
    }

    fun maxCharsPerGeneration(): Int = if (_longTextMode.value) 4000 else 1500

    fun toggleThemeMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun selectTab(tab: StudioTab) {
        _currentTab.value = tab
    }

    fun selectResourceSection(sectionId: String) {
        _selectedResourceSection.value = sectionId
        _currentTab.value = StudioTab.RESOURCES
    }

    fun dismissBannerNotice() {
        _bannerNotice.value = null
    }

    fun dismissDownloadSuccessBanner() {
        _downloadSuccessBanner.value = null
    }

    fun updateInputText(newText: String) {
        val cap = maxCharsPerGeneration()
        val clipped = if (newText.length > cap) newText.take(cap) else newText
        _inputText.value = clipped

        if (_autoDetectLanguage.value && clipped.length >= 6) {
            val detected = VoiceCatalog.detectLanguageFromText(clipped)
            if (detected != null && detected.code != _selectedLanguage.value.code) {
                selectLanguage(detected, loadSampleText = false)
            }
        }
    }

    fun toggleAutoDetectLanguage(enabled: Boolean) {
        _autoDetectLanguage.value = enabled
    }

    fun selectLanguage(language: SupportedLanguage, loadSampleText: Boolean = false) {
        _selectedLanguage.value = language
        if (loadSampleText) {
            _inputText.value = language.sampleText
        }
        // Automatically pick a matching voice for this language & current gender preference
        val matchingVoices = VoiceCatalog.voices.filter { it.languageCode == language.code }
        val preferred = matchingVoices.firstOrNull {
            _selectedGenderFilter.value == null || it.gender == _selectedGenderFilter.value
        } ?: matchingVoices.firstOrNull()
        if (preferred != null) {
            _selectedVoice.value = preferred
        }
    }

    fun loadRomanUrduSample() {
        val ur = VoiceCatalog.languages.first { it.code == "ur" }
        _selectedLanguage.value = ur
        _inputText.value = ur.romanSampleText ?: ur.sampleText
        val urVoice = VoiceCatalog.voices.firstOrNull { it.languageCode == "ur" }
        if (urVoice != null) _selectedVoice.value = urVoice
    }

    fun selectGenderFilter(gender: VoiceGender?) {
        _selectedGenderFilter.value = gender
        if (gender != null && _selectedVoice.value.gender != gender) {
            val match = VoiceCatalog.voices.firstOrNull {
                it.languageCode == _selectedLanguage.value.code && it.gender == gender
            } ?: VoiceCatalog.voices.firstOrNull { it.gender == gender }
            if (match != null) {
                _selectedVoice.value = match
            }
        }
    }

    fun selectVoice(voice: VoiceProfile, navigateToStudio: Boolean = false) {
        _selectedVoice.value = voice
        val lang = VoiceCatalog.languages.find { it.code == voice.languageCode }
        if (lang != null) {
            _selectedLanguage.value = lang
        }
        val style = VoiceCatalog.styles.find { it.id == voice.primaryStyleId }
        if (style != null) {
            _selectedStyle.value = style
        }
        _selectedProvider.value = voice.provider
        if (navigateToStudio) {
            _currentTab.value = StudioTab.STUDIO
            _bannerNotice.value = "Selected voice: ${voice.name} (${voice.languageName})"
        }
    }

    fun selectStyle(style: VoiceStyle) {
        _selectedStyle.value = style
    }

    fun selectProvider(provider: TtsProviderType) {
        _selectedProvider.value = provider
    }

    fun updateSpeed(speed: Float) {
        _speakingSpeed.value = speed.coerceIn(0.5f, 2.0f)
    }

    fun updatePitch(pitch: Float) {
        _speakingPitch.value = pitch.coerceIn(0.5f, 1.8f)
    }

    fun updateVolume(volume: Float) {
        val clamped = volume.coerceIn(0.1f, 1.0f)
        _speakingVolume.value = clamped
        playerController.setVolume(clamped)
    }

    fun setLongTextMode(enabled: Boolean) {
        _longTextMode.value = enabled
        if (enabled) {
            _bannerNotice.value = "Long Text Narration enabled: up to 4,000 characters with automatic sentence chunking & WAV combining."
        }
    }

    fun setDownloadFormat(formatExt: String) {
        _downloadFormat.value = formatExt
    }

    fun clearText() {
        _inputText.value = ""
    }

    // Voice Gallery Filter Setters
    fun updateGallerySearch(query: String) {
        _gallerySearchQuery.value = query
    }

    fun updateGalleryLanguage(langCode: String) {
        _galleryLanguageCode.value = langCode
    }

    fun updateGalleryGender(gender: VoiceGender?) {
        _galleryGender.value = gender
    }

    fun updateGalleryStyle(styleId: String) {
        _galleryStyleId.value = styleId
    }

    fun toggleFavoriteVoice(voiceId: String) {
        viewModelScope.launch {
            val isFav = favoriteVoiceIds.value.contains(voiceId)
            repository.toggleFavorite(voiceId, isFav)
        }
    }

    /**
     * Generates or loads a cached preview audio file for a specific VoiceProfile and plays it immediately.
     */
    fun previewVoice(voice: VoiceProfile) {
        if (_previewingVoiceId.value == voice.id) return
        viewModelScope.launch {
            _previewingVoiceId.value = voice.id
            val cacheDir = File(getApplication<Application>().cacheDir, "voice_previews").apply { mkdirs() }
            val cachedFile = File(cacheDir, "preview_${voice.id}.wav")

            if (cachedFile.exists() && cachedFile.length() > 100) {
                playerController.loadAndPlay(
                    file = cachedFile,
                    title = "Preview: ${voice.name}",
                    voiceName = voice.name,
                    autoPlay = true,
                    initialVolume = _speakingVolume.value
                )
                _previewingVoiceId.value = null
                return@launch
            }

            val lang = VoiceCatalog.languages.find { it.code == voice.languageCode }
                ?: VoiceCatalog.languages.first()
            val style = VoiceCatalog.styles.find { it.id == voice.primaryStyleId }
                ?: VoiceCatalog.styles.first()

            val result = ttsManager.synthesizeTextToAudioFile(
                text = voice.sampleSentence,
                language = lang,
                voice = voice,
                style = style,
                speed = 1.0f,
                pitch = 1.0f,
                volume = _speakingVolume.value,
                preferredProvider = voice.provider,
                onProgress = { _, _, _ -> }
            )

            when (result) {
                is TtsSynthesisResult.Success -> {
                    runCatching { result.audioFile.copyTo(cachedFile, overwrite = true) }
                    playerController.loadAndPlay(
                        file = result.audioFile,
                        title = "Preview: ${voice.name}",
                        voiceName = voice.name,
                        autoPlay = true,
                        initialVolume = _speakingVolume.value
                    )
                    if (result.noticeMessage != null) {
                        _bannerNotice.value = result.noticeMessage
                    }
                }
                is TtsSynthesisResult.Error -> {
                    _bannerNotice.value = result.message
                }
            }
            _previewingVoiceId.value = null
        }
    }

    /**
     * Main Generate Audio workflow with free-tier usage validation, real chunk progress,
     * audio player loading, and Room history persistence.
     */
    fun generateAudio() {
        val text = _inputText.value.trim()
        if (text.isEmpty()) {
            _bannerNotice.value = "Please enter or paste text in the editor before generating audio."
            return
        }
        if (_generationProgress.value.isGenerating) return

        viewModelScope.launch {
            val account = repository.ensureAccountInitialized()
            val remaining = (account.dailyAllowanceChars - account.usedCharsToday).coerceAtLeast(0)
            val usingCloudProvider = (_selectedProvider.value == TtsProviderType.GEMINI_NEURAL)

            if (usingCloudProvider && text.length > remaining) {
                _bannerNotice.value =
                    "Daily Cloud AI character limit reached ($remaining chars left). Switched to Free On-Device Speech Engine so you can continue generating!"
                _selectedProvider.value = TtsProviderType.ANDROID_NATIVE
            }

            _downloadSuccessBanner.value = null
            _generationProgress.value = GenerationProgressState(
                isGenerating = true,
                stageMessage = "Preparing your voice…",
                completedChunks = 0,
                totalChunks = 1
            )

            val lang = _selectedLanguage.value
            val voice = _selectedVoice.value
            val style = _selectedStyle.value
            val speed = _speakingSpeed.value
            val pitch = _speakingPitch.value
            val volume = _speakingVolume.value
            val provider = _selectedProvider.value

            val result = ttsManager.synthesizeTextToAudioFile(
                text = text,
                language = lang,
                voice = voice,
                style = style,
                speed = speed,
                pitch = pitch,
                volume = volume,
                preferredProvider = provider,
                onProgress = { msg, done, total ->
                    _generationProgress.value = GenerationProgressState(
                        isGenerating = true,
                        stageMessage = msg,
                        completedChunks = done,
                        totalChunks = total
                    )
                }
            )

            _generationProgress.value = GenerationProgressState(isGenerating = false)

            when (result) {
                is TtsSynthesisResult.Success -> {
                    val snippet = if (text.length > 90) text.take(90) + "…" else text
                    val title = "${lang.name} • ${voice.name.substringBefore("—").trim()}"
                    val historyItem = AudioHistoryEntity(
                        title = title,
                        textSnippet = snippet,
                        fullText = text,
                        languageCode = lang.code,
                        languageName = lang.name,
                        voiceId = voice.id,
                        voiceName = voice.name,
                        gender = voice.gender.displayName,
                        styleName = style.name,
                        providerName = result.providerUsed,
                        filePath = result.audioFile.absolutePath,
                        fileName = result.audioFile.name,
                        mimeType = "audio/wav",
                        formatExtension = "wav",
                        durationMs = result.durationMs,
                        fileSizeBytes = result.audioFile.length(),
                        speed = speed,
                        pitch = pitch,
                        characterCount = text.length
                    )
                    val charCharge = if (result.providerUsed.contains("Gemini")) text.length else 0
                    repository.recordGenerationUsage(charCharge, historyItem)

                    playerController.loadAndPlay(
                        file = result.audioFile,
                        title = title,
                        voiceName = "${voice.name} (${style.name})",
                        autoPlay = true,
                        initialVolume = volume
                    )

                    if (result.noticeMessage != null) {
                        _bannerNotice.value = result.noticeMessage
                    }
                }
                is TtsSynthesisResult.Error -> {
                    _bannerNotice.value = result.message
                }
            }
        }
    }

    // Audio Player Controls
    fun togglePlayPause() = playerController.togglePlayPause()
    fun replayAudio() = playerController.replay()
    fun seekAudio(fraction: Float) = playerController.seekToFraction(fraction)

    fun playHistoryItem(item: AudioHistoryEntity) {
        val file = File(item.filePath)
        if (!file.exists()) {
            _bannerNotice.value = "Audio file is no longer cached on disk. Re-generating now…"
            _inputText.value = item.fullText
            generateAudio()
            return
        }
        playerController.loadAndPlay(
            file = file,
            title = item.title,
            voiceName = "${item.voiceName} (${item.styleName})",
            autoPlay = true,
            initialVolume = _speakingVolume.value
        )
    }

    /**
     * Downloads the currently loaded audio (or a history item's audio) to the user's device Downloads folder.
     */
    fun downloadCurrentAudio(specificFilePath: String? = null) {
        val path = specificFilePath ?: playbackState.value.currentFilePath
        if (path.isNullOrBlank()) {
            _bannerNotice.value = "Please generate or preview an audio clip before downloading."
            return
        }
        val sourceFile = File(path)
        viewModelScope.launch {
            val outcome = AudioDownloadHelper.saveAudioToDownloads(
                context = getApplication(),
                sourceFile = sourceFile,
                requestedFormatExt = _downloadFormat.value
            )
            if (outcome.success) {
                repository.recordDownload()
                _downloadSuccessBanner.value = outcome.message
            } else {
                _bannerNotice.value = outcome.message
            }
        }
    }

    fun shareCurrentAudio(specificFilePath: String? = null, title: String = "MSK AI Voice") {
        val path = specificFilePath ?: playbackState.value.currentFilePath ?: return
        val file = File(path)
        AudioDownloadHelper.shareAudioFile(getApplication(), file, title)
    }

    /**
     * Executes one of the AI Tools from the Tools Hub or Studio toolbar.
     */
    fun runAiStudioTool(toolId: String, topicPrompt: String = "") {
        viewModelScope.launch {
            _aiToolBusy.value = true
            when (toolId) {
                "urdu_tts" -> {
                    selectLanguage(VoiceCatalog.languages.first { it.code == "ur" }, loadSampleText = true)
                    _currentTab.value = StudioTab.STUDIO
                    _bannerNotice.value = "Switched to Urdu Text-to-Speech (Supports Urdu & Roman Urdu)."
                }
                "english_tts" -> {
                    selectLanguage(VoiceCatalog.languages.first { it.code == "en" }, loadSampleText = true)
                    _currentTab.value = StudioTab.STUDIO
                    _bannerNotice.value = "Switched to English Studio Neural Voices."
                }
                "hindi_tts" -> {
                    selectLanguage(VoiceCatalog.languages.first { it.code == "hi" }, loadSampleText = true)
                    _currentTab.value = StudioTab.STUDIO
                    _bannerNotice.value = "Switched to Hindi Text-to-Speech Studio."
                }
                "pashto_tts" -> {
                    selectLanguage(VoiceCatalog.languages.first { it.code == "ps" }, loadSampleText = true)
                    _currentTab.value = StudioTab.STUDIO
                    _bannerNotice.value = "Switched to Pashto (پښتو) Text-to-Speech Studio."
                }
                "long_text" -> {
                    setLongTextMode(true)
                    _currentTab.value = StudioTab.STUDIO
                }
                "cleanup" -> {
                    val cleaned = GeminiTtsClient.enhanceOrGenerateScript(
                        toolType = "cleanup",
                        inputText = _inputText.value,
                        targetLanguage = _selectedLanguage.value
                    )
                    _inputText.value = cleaned
                    _currentTab.value = StudioTab.STUDIO
                    _bannerNotice.value = "Text punctuation and breathing pauses optimized for speech!"
                }
                "roman_to_urdu" -> {
                    val converted = GeminiTtsClient.enhanceOrGenerateScript(
                        toolType = "roman_to_urdu",
                        inputText = _inputText.value,
                        targetLanguage = VoiceCatalog.languages.first { it.code == "ur" }
                    )
                    selectLanguage(VoiceCatalog.languages.first { it.code == "ur" }, loadSampleText = false)
                    _inputText.value = converted
                    _currentTab.value = StudioTab.STUDIO
                    _bannerNotice.value = "Converted Roman Urdu to Urdu script (اردو) for natural pronunciation!"
                }
                "story", "podcast", "news", "educational" -> {
                    val styleIdMap = mapOf(
                        "story" to "storytelling",
                        "podcast" to "podcast",
                        "news" to "news",
                        "educational" to "educational"
                    )
                    val targetStyleId = styleIdMap[toolId] ?: "natural"
                    VoiceCatalog.styles.find { it.id == targetStyleId }?.let { _selectedStyle.value = it }

                    val script = GeminiTtsClient.enhanceOrGenerateScript(
                        toolType = toolId,
                        inputText = topicPrompt.ifBlank { _inputText.value },
                        targetLanguage = _selectedLanguage.value
                    )
                    _inputText.value = script
                    _currentTab.value = StudioTab.STUDIO
                    _bannerNotice.value = "Generated ${ _selectedStyle.value.name } script! Tap 'Generate Audio' to listen."
                }
            }
            _aiToolBusy.value = false
        }
    }

    // Account & History actions
    fun signInAccount(name: String, email: String, method: String) {
        viewModelScope.launch {
            repository.signInUser(name, email, method)
            _bannerNotice.value = "Signed in as $name — Daily free allowance upgraded to 15,000 characters!"
        }
    }

    fun signOutOrDeleteAccount(deleteAllData: Boolean) {
        viewModelScope.launch {
            repository.signOutOrDeleteAccount(clearHistory = deleteAllData)
            _bannerNotice.value = if (deleteAllData) {
                "Account and local audio history deleted."
            } else {
                "Signed out to Free Guest Tier."
            }
        }
    }

    fun deleteHistoryItem(item: AudioHistoryEntity) {
        viewModelScope.launch {
            repository.deleteHistoryItem(item)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.clearAllHistoryFiles(historyList.value)
            _bannerNotice.value = "Cleared all saved audio history."
        }
    }

    override fun onCleared() {
        super.onCleared()
        playerController.releasePlayer()
        ttsManager.shutdown()
    }
}
