package com.example.data

import java.util.Locale

enum class VoiceGender(val displayName: String) {
    FEMALE("Female"),
    MALE("Male")
}

enum class TtsProviderType(
    val id: String,
    val title: String,
    val subtitle: String,
    val badge: String
) {
    GEMINI_NEURAL(
        id = "GEMINI_NEURAL",
        title = "Gemini 2.5 Neural TTS",
        subtitle = "Cloud AI • Ultra-natural multilingual studio voices (Urdu, English, Hindi, Arabic, Pashto & 18+ languages)",
        badge = "Cloud Neural AI"
    ),
    ANDROID_NATIVE(
        id = "ANDROID_NATIVE",
        title = "On-Device Speech Engine",
        subtitle = "Zero-cost browser/device synthesis • Offline WAV generation & instant fallback",
        badge = "Free Device Fallback"
    )
}

data class SupportedLanguage(
    val code: String,
    val bcp47: String,
    val name: String,
    val nativeName: String,
    val flagEmoji: String,
    val supportsRomanInput: Boolean = false,
    val sampleText: String,
    val romanSampleText: String? = null,
    val regionTag: String
)

data class VoiceStyle(
    val id: String,
    val name: String,
    val description: String,
    val promptInstruction: String,
    val defaultSpeedMultiplier: Float = 1.0f,
    val defaultPitchMultiplier: Float = 1.0f,
    val iconName: String
)

data class VoiceProfile(
    val id: String,
    val name: String,
    val geminiVoiceName: String, // Real Gemini Prebuilt Voice: Kore, Puck, Charon, Fenrir, Aoede, Zephyr, Leda, Orus
    val gender: VoiceGender,
    val languageCode: String,
    val languageName: String,
    val accent: String,
    val primaryStyleId: String,
    val primaryStyleName: String,
    val provider: TtsProviderType,
    val sampleSentence: String,
    val description: String,
    val isFeatured: Boolean = false
)

object VoiceCatalog {

    val languages: List<SupportedLanguage> = listOf(
        SupportedLanguage(
            code = "ur",
            bcp47 = "ur-PK",
            name = "Urdu",
            nativeName = "اردو (Urdu & Roman Urdu)",
            flagEmoji = "🇵🇰",
            supportsRomanInput = true,
            sampleText = "ایم ایس کے اے آئی وائس اسٹوڈیو میں خوش آمدید۔ آپ کے الفاظ، ہر آواز، مصنوعی ذہانت کے ساتھ۔",
            romanSampleText = "MSK AI Voice Studio mein khush aamdeed. Aap ke alfaaz, har aawaz, AI ke saath.",
            regionTag = "Pakistan / India"
        ),
        SupportedLanguage(
            code = "en",
            bcp47 = "en-US",
            name = "English",
            nativeName = "English (US / UK / Global)",
            flagEmoji = "🇺🇸",
            sampleText = "Welcome to MSK AI Voice Studio. Turn your words into lifelike, natural-sounding speech powered by artificial intelligence.",
            regionTag = "Global"
        ),
        SupportedLanguage(
            code = "hi",
            bcp47 = "hi-IN",
            name = "Hindi",
            nativeName = "हिन्दी (Hindi)",
            flagEmoji = "🇮🇳",
            sampleText = "एमएसके एआई वॉइस स्टूडियो में आपका स्वागत है। अपने शब्दों को प्राकृतिक और स्पष्ट आवाज़ में बदलें।",
            regionTag = "India"
        ),
        SupportedLanguage(
            code = "ar",
            bcp47 = "ar-SA",
            name = "Arabic",
            nativeName = "العربية (Arabic)",
            flagEmoji = "🇸🇦",
            sampleText = "مرحباً بكم في استوديو MSK للذكاء الاصطناعي الصوتي. حوّل كلماتك إلى كلام طبيعي واضح ومؤثر.",
            regionTag = "MENA"
        ),
        SupportedLanguage(
            code = "ps",
            bcp47 = "ps-AF",
            name = "Pashto",
            nativeName = "پښتو (Pashto)",
            flagEmoji = "🇦🇫",
            sampleText = "د ایم ایس کی مصنوعي ځیرکتیا غږ سټوډیو ته ښه راغلاست. خپلې خبرې په طبیعي غږ بدلې کړئ.",
            regionTag = "Pakistan / Afghanistan"
        ),
        SupportedLanguage(
            code = "pa",
            bcp47 = "pa-PK",
            name = "Punjabi",
            nativeName = "پنجابی / ਪੰਜਾਬੀ (Punjabi)",
            flagEmoji = "🇵🇰",
            sampleText = "ایم ایس کے اے آئی وائس اسٹوڈیو وچ جی آیاں نوں۔ اپنے بول قدرتی آواز وچ سنو۔",
            regionTag = "Punjab"
        ),
        SupportedLanguage(
            code = "bn",
            bcp47 = "bn-BD",
            name = "Bengali",
            nativeName = "বাংলা (Bengali)",
            flagEmoji = "🇧🇩",
            sampleText = "এমএসকে এআই ভয়েস স্টুডিওতে স্বাগতম। আপনার লেখাকে প্রাণবন্ত ও প্রাকৃতিক কণ্ঠে রূপান্তর করুন।",
            regionTag = "Bangladesh / India"
        ),
        SupportedLanguage(
            code = "fa",
            bcp47 = "fa-IR",
            name = "Persian",
            nativeName = "فارسی (Persian / Farsi)",
            flagEmoji = "🇮🇷",
            sampleText = "به استودیوی صدای هوش مصنوعی MSK خوش آمدید. متن خود را به گفتار طبیعی تبدیل کنید.",
            regionTag = "Iran / Tajikistan"
        ),
        SupportedLanguage(
            code = "tr",
            bcp47 = "tr-TR",
            name = "Turkish",
            nativeName = "Türkçe (Turkish)",
            flagEmoji = "🇹🇷",
            sampleText = "MSK Yapay Zeka Ses Stüdyosuna hoş geldiniz. Metinlerinizi doğal ve akıcı sese dönüştürün.",
            regionTag = "Türkiye"
        ),
        SupportedLanguage(
            code = "es",
            bcp47 = "es-ES",
            name = "Spanish",
            nativeName = "Español (Spanish)",
            flagEmoji = "🇪🇸",
            sampleText = "Bienvenido a MSK AI Voice Studio. Convierte tu texto en voz natural con inteligencia artificial.",
            regionTag = "Spain / LatAm"
        ),
        SupportedLanguage(
            code = "fr",
            bcp47 = "fr-FR",
            name = "French",
            nativeName = "Français (French)",
            flagEmoji = "🇫🇷",
            sampleText = "Bienvenue sur MSK AI Voice Studio. Transformez vos textes en voix naturelles grâce à l'IA.",
            regionTag = "France / Canada"
        ),
        SupportedLanguage(
            code = "de",
            bcp47 = "de-DE",
            name = "German",
            nativeName = "Deutsch (German)",
            flagEmoji = "🇩🇪",
            sampleText = "Willkommen im MSK AI Voice Studio. Verwandeln Sie Text in natürlich klingende Sprache.",
            regionTag = "Germany / DACH"
        ),
        SupportedLanguage(
            code = "zh",
            bcp47 = "zh-CN",
            name = "Chinese",
            nativeName = "中文 (Mandarin Chinese)",
            flagEmoji = "🇨🇳",
            sampleText = "欢迎来到 MSK AI 语音工作室。用人工智能将您的文字转化为自然流畅的语音。",
            regionTag = "China / Taiwan"
        ),
        SupportedLanguage(
            code = "ja",
            bcp47 = "ja-JP",
            name = "Japanese",
            nativeName = "日本語 (Japanese)",
            flagEmoji = "🇯🇵",
            sampleText = "MSK AIボイススタジオへようこそ。テキストを自然でクリアな音声に変換します。",
            regionTag = "Japan"
        ),
        SupportedLanguage(
            code = "ko",
            bcp47 = "ko-KR",
            name = "Korean",
            nativeName = "한국어 (Korean)",
            flagEmoji = "🇰🇷",
            sampleText = "MSK AI 보이스 스튜디오에 오신 것을 환영합니다. 텍스트를 자연스러운 음성으로 변환하세요.",
            regionTag = "South Korea"
        ),
        SupportedLanguage(
            code = "pt",
            bcp47 = "pt-BR",
            name = "Portuguese",
            nativeName = "Português (Portuguese)",
            flagEmoji = "🇧🇷",
            sampleText = "Bem-vindo ao MSK AI Voice Studio. Transforme seu texto em fala natural com inteligência artificial.",
            regionTag = "Brazil / Portugal"
        ),
        SupportedLanguage(
            code = "id",
            bcp47 = "id-ID",
            name = "Indonesian",
            nativeName = "Bahasa Indonesia",
            flagEmoji = "🇮🇩",
            sampleText = "Selamat datang di MSK AI Voice Studio. Ubah teks Anda menjadi suara alami bertenaga AI.",
            regionTag = "Indonesia"
        )
    )

    val styles: List<VoiceStyle> = listOf(
        VoiceStyle(
            id = "natural",
            name = "Natural Conversation",
            description = "Warm, balanced, everyday conversational tone",
            promptInstruction = "Speak in a warm, natural, conversational tone",
            defaultSpeedMultiplier = 1.0f,
            defaultPitchMultiplier = 1.0f,
            iconName = "chat"
        ),
        VoiceStyle(
            id = "narrator",
            name = "Professional Narrator",
            description = "Crisp, authoritative documentary and corporate narration",
            promptInstruction = "Speak like a clear, composed professional documentary narrator",
            defaultSpeedMultiplier = 0.96f,
            defaultPitchMultiplier = 0.98f,
            iconName = "mic"
        ),
        VoiceStyle(
            id = "news",
            name = "News Presenter",
            description = "Articulate broadcast journalism delivery with clear cadence",
            promptInstruction = "Speak with the articulate, confident cadence of a prime-time news anchor",
            defaultSpeedMultiplier = 1.05f,
            defaultPitchMultiplier = 1.0f,
            iconName = "newspaper"
        ),
        VoiceStyle(
            id = "podcast",
            name = "Podcast Host",
            description = "Engaging, intimate studio mic presence",
            promptInstruction = "Speak like an engaging, friendly podcast host talking closely into a studio microphone",
            defaultSpeedMultiplier = 1.02f,
            defaultPitchMultiplier = 1.0f,
            iconName = "podcasts"
        ),
        VoiceStyle(
            id = "storytelling",
            name = "Storytelling",
            description = "Expressive pacing with immersive dramatic nuance",
            promptInstruction = "Speak with expressive storytelling warmth, vivid pacing, and narrative wonder",
            defaultSpeedMultiplier = 0.94f,
            defaultPitchMultiplier = 1.02f,
            iconName = "menu_book"
        ),
        VoiceStyle(
            id = "emotional",
            name = "Emotional Narration",
            description = "Deeply empathetic, heartfelt poetry and literary delivery",
            promptInstruction = "Speak with heartfelt emotion, empathy, and gentle poetic resonance",
            defaultSpeedMultiplier = 0.90f,
            defaultPitchMultiplier = 0.97f,
            iconName = "favorite"
        ),
        VoiceStyle(
            id = "calm",
            name = "Calm & Relaxing",
            description = "Soothing, unhurried voice for mindfulness and reflection",
            promptInstruction = "Speak in a calm, soothing, deeply relaxing and peaceful tone",
            defaultSpeedMultiplier = 0.88f,
            defaultPitchMultiplier = 0.95f,
            iconName = "spa"
        ),
        VoiceStyle(
            id = "energetic",
            name = "Energetic",
            description = "Upbeat, enthusiastic delivery for promos and creators",
            promptInstruction = "Speak with upbeat energy, enthusiasm, and vibrant excitement",
            defaultSpeedMultiplier = 1.10f,
            defaultPitchMultiplier = 1.05f,
            iconName = "bolt"
        ),
        VoiceStyle(
            id = "educational",
            name = "Educational",
            description = "Patient, structured explanation for tutorials and e-learning",
            promptInstruction = "Speak clearly and patiently like an inspiring educator explaining a concept",
            defaultSpeedMultiplier = 0.95f,
            defaultPitchMultiplier = 1.0f,
            iconName = "school"
        ),
        VoiceStyle(
            id = "advertising",
            name = "Advertising",
            description = "Polished, persuasive brand commercial voiceover",
            promptInstruction = "Speak in a polished, persuasive, high-impact commercial voiceover style",
            defaultSpeedMultiplier = 1.04f,
            defaultPitchMultiplier = 1.02f,
            iconName = "campaign"
        ),
        VoiceStyle(
            id = "audiobook",
            name = "Audiobook Narration",
            description = "Sustained, effortless listening comfort for long chapters",
            promptInstruction = "Speak like a classic audiobook narrator with steady rhythm and clarity",
            defaultSpeedMultiplier = 0.95f,
            defaultPitchMultiplier = 0.98f,
            iconName = "auto_stories"
        )
    )

    val voices: List<VoiceProfile> = listOf(
        // Urdu Voices (Native Script + Roman Urdu)
        VoiceProfile(
            id = "ur_female_zoya_kore",
            name = "Zoya (زویا) — Neural Studio",
            geminiVoiceName = "Kore",
            gender = VoiceGender.FEMALE,
            languageCode = "ur",
            languageName = "Urdu",
            accent = "Pakistani Standard (Islamabad / Lahore)",
            primaryStyleId = "natural",
            primaryStyleName = "Natural Conversation",
            provider = TtsProviderType.GEMINI_NEURAL,
            sampleSentence = "السلام علیکم! ایم ایس کے اے آئی وائس اسٹوڈیو میں خوش آمدید۔ آپ اردو یا رومن اردو میں لکھ سکتے ہیں۔",
            description = "Crystal-clear Urdu & Roman Urdu female voice with natural conversational warmth and literary articulation.",
            isFeatured = true
        ),
        VoiceProfile(
            id = "ur_male_hamza_charon",
            name = "Hamza (حمزہ) — Neural Studio",
            geminiVoiceName = "Charon",
            gender = VoiceGender.MALE,
            languageCode = "ur",
            languageName = "Urdu",
            accent = "Pakistani Standard (Karachi / Broadcast)",
            primaryStyleId = "narrator",
            primaryStyleName = "Professional Narrator",
            provider = TtsProviderType.GEMINI_NEURAL,
            sampleSentence = "یہ آواز ایم ایس کے اے آئی وائس اسٹوڈیو کے جدید ماڈل سے تیار کی گئی ہے۔",
            description = "Rich, resonant male Urdu baritone ideal for documentaries, news bulletins, and Urdu storytelling.",
            isFeatured = true
        ),
        VoiceProfile(
            id = "ur_female_ayesha_aoede",
            name = "Ayesha (عائشہ) — Dastaan Voice",
            geminiVoiceName = "Aoede",
            gender = VoiceGender.FEMALE,
            languageCode = "ur",
            languageName = "Urdu",
            accent = "Literary Urdu (Dakhni / Standard)",
            primaryStyleId = "storytelling",
            primaryStyleName = "Storytelling",
            provider = TtsProviderType.GEMINI_NEURAL,
            sampleSentence = "ہر کہانی کا ایک نیا سفر ہوتا ہے، اور ہر لفظ میں ایک احساس چھپا ہوتا ہے۔",
            description = "Expressive Urdu female narrator tailored for afsanay, audiobooks, poetry, and emotional narration."
        ),
        VoiceProfile(
            id = "ur_male_bilal_fenrir",
            name = "Bilal (بلال) — News & Podcast",
            geminiVoiceName = "Fenrir",
            gender = VoiceGender.MALE,
            languageCode = "ur",
            languageName = "Urdu",
            accent = "Pakistani Broadcast",
            primaryStyleId = "news",
            primaryStyleName = "News Presenter",
            provider = TtsProviderType.GEMINI_NEURAL,
            sampleSentence = "آج کی اہم خبروں اور تجزیوں کے ساتھ ایم ایس کے اسٹوڈیو سے میں ہوں بلال۔",
            description = "Authoritative male voice engineered for Urdu news reporting, YouTube commentary, and podcasts."
        ),

        // English Voices
        VoiceProfile(
            id = "en_female_aria_kore",
            name = "Aria — Studio Neural",
            geminiVoiceName = "Kore",
            gender = VoiceGender.FEMALE,
            languageCode = "en",
            languageName = "English",
            accent = "American (General US)",
            primaryStyleId = "natural",
            primaryStyleName = "Natural Conversation",
            provider = TtsProviderType.GEMINI_NEURAL,
            sampleSentence = "Welcome to MSK AI Voice Studio. Turn your scripts into studio-grade voiceovers in seconds.",
            description = "Flagship English female neural voice with lifelike breath pacing and conversational clarity.",
            isFeatured = true
        ),
        VoiceProfile(
            id = "en_male_marcus_puck",
            name = "Marcus — Podcast & Creator",
            geminiVoiceName = "Puck",
            gender = VoiceGender.MALE,
            languageCode = "en",
            languageName = "English",
            accent = "American (Warm Studio)",
            primaryStyleId = "podcast",
            primaryStyleName = "Podcast Host",
            provider = TtsProviderType.GEMINI_NEURAL,
            sampleSentence = "Hey everyone, welcome back to the show! Today we're exploring next-generation AI voice synthesis.",
            description = "Dynamic, engaging male English voice built for podcasts, YouTube videos, and commercial explainers.",
            isFeatured = true
        ),
        VoiceProfile(
            id = "en_male_arthur_charon",
            name = "Arthur — Documentary Baritone",
            geminiVoiceName = "Charon",
            gender = VoiceGender.MALE,
            languageCode = "en",
            languageName = "English",
            accent = "British / Transatlantic",
            primaryStyleId = "audiobook",
            primaryStyleName = "Audiobook Narration",
            provider = TtsProviderType.GEMINI_NEURAL,
            sampleSentence = "Across centuries of human invention, the spoken word has remained our most powerful medium.",
            description = "Deep, composed male narrator suited for audiobooks, historical documentaries, and executive presentations."
        ),
        VoiceProfile(
            id = "en_female_claire_zephyr",
            name = "Claire — Calm & E-Learning",
            geminiVoiceName = "Zephyr",
            gender = VoiceGender.FEMALE,
            languageCode = "en",
            languageName = "English",
            accent = "International English",
            primaryStyleId = "educational",
            primaryStyleName = "Educational",
            provider = TtsProviderType.GEMINI_NEURAL,
            sampleSentence = "In this lesson, we will walk step by step through the principles of acoustic modeling.",
            description = "Clear, soothing female voice designed for e-learning courses, guided meditation, and tutorials."
        ),

        // Hindi Voices
        VoiceProfile(
            id = "hi_female_ananya_leda",
            name = "Ananya (अनन्या) — Neural Hindi",
            geminiVoiceName = "Leda",
            gender = VoiceGender.FEMALE,
            languageCode = "hi",
            languageName = "Hindi",
            accent = "Standard Hindi (New Delhi)",
            primaryStyleId = "natural",
            primaryStyleName = "Natural Conversation",
            provider = TtsProviderType.GEMINI_NEURAL,
            sampleSentence = "नमस्ते! एमएसके एआई वॉइस स्टूडियो में आप अपनी हिंदी स्क्रिप्ट को जीवंत आवाज़ दे सकते हैं।",
            description = "Natural, articulate Hindi female voice for stories, educational videos, and regional announcements.",
            isFeatured = true
        ),
        VoiceProfile(
            id = "hi_male_kabir_orus",
            name = "Kabir (कबीर) — Hindi Narrator",
            geminiVoiceName = "Orus",
            gender = VoiceGender.MALE,
            languageCode = "hi",
            languageName = "Hindi",
            accent = "Standard Hindi (Broadcast)",
            primaryStyleId = "narrator",
            primaryStyleName = "Professional Narrator",
            provider = TtsProviderType.GEMINI_NEURAL,
            sampleSentence = "यह आवाज़ कृत्रिम बुद्धिमत्ता द्वारा अत्यंत स्पष्ट और प्रभावशाली शैली में तैयार की गई है।",
            description = "Deep, confident Hindi male voice for documentaries, news summaries, and audiobooks."
        ),

        // Arabic Voices
        VoiceProfile(
            id = "ar_female_layla_aoede",
            name = "Layla (ليلى) — Arabic Studio",
            geminiVoiceName = "Aoede",
            gender = VoiceGender.FEMALE,
            languageCode = "ar",
            languageName = "Arabic",
            accent = "Modern Standard Arabic (Fusha)",
            primaryStyleId = "narrator",
            primaryStyleName = "Professional Narrator",
            provider = TtsProviderType.GEMINI_NEURAL,
            sampleSentence = "أهلاً بكم في استوديو MSK الصوتي، حيث تتحول الكلمات العربية إلى صوت احترافي.",
            description = "Fluent Modern Standard Arabic female voice with classical diction and smooth vowel cadence."
        ),
        VoiceProfile(
            id = "ar_male_tariq_fenrir",
            name = "Tariq (طارق) — Arabic Broadcast",
            geminiVoiceName = "Fenrir",
            gender = VoiceGender.MALE,
            languageCode = "ar",
            languageName = "Arabic",
            accent = "Modern Standard Arabic (Gulf / Levant)",
            primaryStyleId = "news",
            primaryStyleName = "News Presenter",
            provider = TtsProviderType.GEMINI_NEURAL,
            sampleSentence = "نقدم لكم أحدث تقنيات تحويل النص إلى كلام طبيعي باللغة العربية الفصحى.",
            description = "Commanding Arabic male broadcast voice for news, documentaries, and educational narration."
        ),

        // Pashto Voices
        VoiceProfile(
            id = "ps_male_zalan_charon",
            name = "Zalan (ځلان) — Pashto Neural",
            geminiVoiceName = "Charon",
            gender = VoiceGender.MALE,
            languageCode = "ps",
            languageName = "Pashto",
            accent = "Standard Pashto (Peshawar / Kabul)",
            primaryStyleId = "natural",
            primaryStyleName = "Natural Conversation",
            provider = TtsProviderType.GEMINI_NEURAL,
            sampleSentence = "ستاسو هره لیکنه اوس په روښانه او طبیعي پښتو غږ بدلېدلی شي.",
            description = "Authentic Pashto male voice supporting literary and conversational Pashto scripts.",
            isFeatured = true
        ),
        VoiceProfile(
            id = "ps_female_spogmai_kore",
            name = "Spogmai (سپوږمۍ) — Pashto Studio",
            geminiVoiceName = "Kore",
            gender = VoiceGender.FEMALE,
            languageCode = "ps",
            languageName = "Pashto",
            accent = "Standard Pashto",
            primaryStyleId = "storytelling",
            primaryStyleName = "Storytelling",
            provider = TtsProviderType.GEMINI_NEURAL,
            sampleSentence = "د ایم ایس کی غږ سټوډیو سره خپلې کیسې او شعرونه په خوږ غږ واورئ.",
            description = "Warm, expressive Pashto female voice for educational programs, stories, and announcements."
        ),

        // Punjabi, Bengali, Persian, Turkish, European & Asian Neural Voices
        VoiceProfile(
            id = "pa_male_haris_puck",
            name = "Haris (حارث) — Punjabi Studio",
            geminiVoiceName = "Puck",
            gender = VoiceGender.MALE,
            languageCode = "pa",
            languageName = "Punjabi",
            accent = "Lahori / Standard Punjabi",
            primaryStyleId = "natural",
            primaryStyleName = "Natural Conversation",
            provider = TtsProviderType.GEMINI_NEURAL,
            sampleSentence = "ایم ایس کے اے آئی وائس اسٹوڈیو وچ پنجابی زبان دے وچ وی شاندار آواز موجود اے۔",
            description = "Friendly, expressive Punjabi male voice supporting both Shahmukhi and Gurmukhi scripts."
        ),
        VoiceProfile(
            id = "bn_female_nusrat_zephyr",
            name = "Nusrat (নুসরাত) — Bengali Neural",
            geminiVoiceName = "Zephyr",
            gender = VoiceGender.FEMALE,
            languageCode = "bn",
            languageName = "Bengali",
            accent = "Dhaka / Kolkata Standard",
            primaryStyleId = "storytelling",
            primaryStyleName = "Storytelling",
            provider = TtsProviderType.GEMINI_NEURAL,
            sampleSentence = "এমএসকে এআই ভয়েস স্টুডিওর মাধ্যমে বাংলায় চমৎকার অডিও তৈরি করুন।",
            description = "Melodic Bengali female voice crafted for literature, audiobooks, and digital content."
        ),
        VoiceProfile(
            id = "fa_female_parisa_aoede",
            name = "Parisa (پریسا) — Persian Studio",
            geminiVoiceName = "Aoede",
            gender = VoiceGender.FEMALE,
            languageCode = "fa",
            languageName = "Persian",
            accent = "Tehran Standard",
            primaryStyleId = "emotional",
            primaryStyleName = "Emotional Narration",
            provider = TtsProviderType.GEMINI_NEURAL,
            sampleSentence = "صدای طبیعی و دلنشین فارسی با فناوری هوش مصنوعی در استودیو ام اس کی.",
            description = "Poetic, articulate Persian female voice for narration, poetry, and cultural broadcasts."
        ),
        VoiceProfile(
            id = "tr_male_emre_orus",
            name = "Emre — Turkish Studio",
            geminiVoiceName = "Orus",
            gender = VoiceGender.MALE,
            languageCode = "tr",
            languageName = "Turkish",
            accent = "Istanbul Standard",
            primaryStyleId = "narrator",
            primaryStyleName = "Professional Narrator",
            provider = TtsProviderType.GEMINI_NEURAL,
            sampleSentence = "MSK AI Ses Stüdyosu ile Türkçe metinlerinizi profesyonel seslendirmeye dönüştürün.",
            description = "Clear Istanbul-accented Turkish male voice for commercial and educational voiceovers."
        ),
        VoiceProfile(
            id = "es_female_sofia_kore",
            name = "Sofía — Español Neural",
            geminiVoiceName = "Kore",
            gender = VoiceGender.FEMALE,
            languageCode = "es",
            languageName = "Spanish",
            accent = "Neutral Spanish",
            primaryStyleId = "advertising",
            primaryStyleName = "Advertising",
            provider = TtsProviderType.GEMINI_NEURAL,
            sampleSentence = "Transforma cualquier texto en una locución profesional con MSK AI Voice Studio.",
            description = "Vibrant Spanish female voice suitable for global Spanish audiences."
        ),
        VoiceProfile(
            id = "fr_female_camille_leda",
            name = "Camille — Français Studio",
            geminiVoiceName = "Leda",
            gender = VoiceGender.FEMALE,
            languageCode = "fr",
            languageName = "French",
            accent = "Parisian French",
            primaryStyleId = "narrator",
            primaryStyleName = "Professional Narrator",
            provider = TtsProviderType.GEMINI_NEURAL,
            sampleSentence = "Créez des voix off naturelles et élégantes en quelques instants.",
            description = "Refined French female voice with smooth intonation for luxury branding and narration."
        ),
        VoiceProfile(
            id = "de_male_lukas_charon",
            name = "Lukas — Deutsch Studio",
            geminiVoiceName = "Charon",
            gender = VoiceGender.MALE,
            languageCode = "de",
            languageName = "German",
            accent = "High German (Hochdeutsch)",
            primaryStyleId = "educational",
            primaryStyleName = "Educational",
            provider = TtsProviderType.GEMINI_NEURAL,
            sampleSentence = "Erstellen Sie präzise und natürlich klingende Sprachaufnahmen mit künstlicher Intelligenz.",
            description = "Clear High German male voice for technical documentation, tutorials, and corporate videos."
        ),
        VoiceProfile(
            id = "zh_female_lin_zephyr",
            name = "Lin (林) — Mandarin Neural",
            geminiVoiceName = "Zephyr",
            gender = VoiceGender.FEMALE,
            languageCode = "zh",
            languageName = "Chinese",
            accent = "Standard Mandarin",
            primaryStyleId = "natural",
            primaryStyleName = "Natural Conversation",
            provider = TtsProviderType.GEMINI_NEURAL,
            sampleSentence = "使用 MSK AI 语音工作室，轻松生成自然逼真的中文配音。",
            description = "Fluent Mandarin female voice with accurate tonal inflection."
        ),
        VoiceProfile(
            id = "ja_female_haruka_aoede",
            name = "Haruka (遥) — Japanese Studio",
            geminiVoiceName = "Aoede",
            gender = VoiceGender.FEMALE,
            languageCode = "ja",
            languageName = "Japanese",
            accent = "Tokyo Standard",
            primaryStyleId = "natural",
            primaryStyleName = "Natural Conversation",
            provider = TtsProviderType.GEMINI_NEURAL,
            sampleSentence = "MSK AIボイススタジオで、高品質な日本語ナレーションを作成しましょう。",
            description = "Polite, natural Tokyo-standard Japanese voice for presentations and media."
        ),
        VoiceProfile(
            id = "ko_male_minho_puck",
            name = "Min-ho (민호) — Korean Studio",
            geminiVoiceName = "Puck",
            gender = VoiceGender.MALE,
            languageCode = "ko",
            languageName = "Korean",
            accent = "Seoul Standard",
            primaryStyleId = "podcast",
            primaryStyleName = "Podcast Host",
            provider = TtsProviderType.GEMINI_NEURAL,
            sampleSentence = "MSK AI 보이스 스튜디오와 함께 생생하고 자연스러운 한국어 음성을 만들어 보세요.",
            description = "Modern Seoul-standard Korean male voice for creators and tutorials."
        ),
        VoiceProfile(
            id = "pt_male_lucas_orus",
            name = "Lucas — Português Neural",
            geminiVoiceName = "Orus",
            gender = VoiceGender.MALE,
            languageCode = "pt",
            languageName = "Portuguese",
            accent = "Brazilian Portuguese",
            primaryStyleId = "energetic",
            primaryStyleName = "Energetic",
            provider = TtsProviderType.GEMINI_NEURAL,
            sampleSentence = "Gere locuções incríveis em português com a tecnologia do MSK AI Voice Studio.",
            description = "Warm, energetic Brazilian Portuguese male voice."
        ),
        VoiceProfile(
            id = "id_female_putri_kore",
            name = "Putri — Indonesian Studio",
            geminiVoiceName = "Kore",
            gender = VoiceGender.FEMALE,
            languageCode = "id",
            languageName = "Indonesian",
            accent = "Jakarta Standard",
            primaryStyleId = "natural",
            primaryStyleName = "Natural Conversation",
            provider = TtsProviderType.GEMINI_NEURAL,
            sampleSentence = "Buat sulih suara Bahasa Indonesia yang jernih dan alami dalam hitungan detik.",
            description = "Clear Indonesian female voice for social media, education, and business."
        ),

        // On-Device Native Fallback Voices (Always Free, Zero API Cost)
        VoiceProfile(
            id = "device_ur_default",
            name = "Device Urdu Synthesizer",
            geminiVoiceName = "Kore",
            gender = VoiceGender.FEMALE,
            languageCode = "ur",
            languageName = "Urdu",
            accent = "On-Device System Voice (ur-PK)",
            primaryStyleId = "natural",
            primaryStyleName = "Natural Conversation",
            provider = TtsProviderType.ANDROID_NATIVE,
            sampleSentence = "یہ آواز آپ کے موبائل کے بلٹ ان اسپیچ انجن سے بغیر کسی انٹرنیٹ بل کے تیار کی گئی ہے۔",
            description = "Uses your device's built-in Urdu speech synthesizer. Free unlimited fallback with WAV download."
        ),
        VoiceProfile(
            id = "device_en_default",
            name = "Device English Synthesizer",
            geminiVoiceName = "Puck",
            gender = VoiceGender.MALE,
            languageCode = "en",
            languageName = "English",
            accent = "On-Device System Voice (en-US)",
            primaryStyleId = "natural",
            primaryStyleName = "Natural Conversation",
            provider = TtsProviderType.ANDROID_NATIVE,
            sampleSentence = "This voice is synthesized directly on your device with zero API usage and instant WAV export.",
            description = "Uses your device's built-in English speech engine. Works offline and never consumes cloud credits."
        ),
        VoiceProfile(
            id = "device_hi_default",
            name = "Device Hindi Synthesizer",
            geminiVoiceName = "Leda",
            gender = VoiceGender.FEMALE,
            languageCode = "hi",
            languageName = "Hindi",
            accent = "On-Device System Voice (hi-IN)",
            primaryStyleId = "natural",
            primaryStyleName = "Natural Conversation",
            provider = TtsProviderType.ANDROID_NATIVE,
            sampleSentence = "यह आवाज़ आपके डिवाइस के इनबिल्ट स्पीच इंजन द्वारा तैयार की गई है।",
            description = "On-device Hindi speech synthesis with direct WAV export."
        )
    )

    private val romanUrduKeywords = setOf(
        "kya", "hai", "hain", "aap", "mein", "hum", "tum", "aur", "لیکن", "nahi", "nhi",
        "bohat", "bahut", "shukriya", "salam", "assalam", "walaikum", "pakistan", "karachi",
        "lahore", "islamabad", "dil", "pyar", "dost", "zindagi", "khush", "aamdeed", "alfaaz",
        "aawaz", "kahaani", "waqt", "لوگ", "kesay", "kaise", "kahan", "kyun", "acha", "theek"
    )

    /**
     * Automatically detects the language of user-entered text, including native Urdu script,
     * Pashto letters, Arabic, Hindi Devanagari, Roman Urdu, CJK, etc.
     */
    fun detectLanguageFromText(text: String): SupportedLanguage? {
        val trimmed = text.trim()
        if (trimmed.length < 3) return null

        // Check Pashto-specific characters (ټ, ډ, ړ, ږ, ښ, ڼ, ۍ, ئ)
        if (trimmed.any { it in listOf('ټ', 'ډ', 'ړ', 'ږ', 'ښ', 'ڼ', 'ۍ') }) {
            return languages.find { it.code == "ps" }
        }
        // Check Urdu-specific characters (ٹ, ڈ, ڑ, ں, ے, ہ, ھ, گ, چ, پ)
        if (trimmed.any { it in listOf('ٹ', 'ڈ', 'ڑ', 'ں', 'ے', 'ہ', 'ھ', 'گ', 'چ', 'پ') }) {
            return languages.find { it.code == "ur" }
        }
        // Check Arabic / Persian script range
        if (trimmed.any { it in '\u0600'..'\u06FF' }) {
            return languages.find { it.code == "ar" }
        }
        // Check Devanagari (Hindi)
        if (trimmed.any { it in '\u0900'..'\u097F' }) {
            return languages.find { it.code == "hi" }
        }
        // Check Gurmukhi (Punjabi)
        if (trimmed.any { it in '\u0A00'..'\u0A7F' }) {
            return languages.find { it.code == "pa" }
        }
        // Check Bengali
        if (trimmed.any { it in '\u0980'..'\u09FF' }) {
            return languages.find { it.code == "bn" }
        }
        // Check Hangul (Korean)
        if (trimmed.any { it in '\uAC00'..'\uD7AF' }) {
            return languages.find { it.code == "ko" }
        }
        // Check Hiragana / Katakana (Japanese)
        if (trimmed.any { it in '\u3040'..'\u30FF' }) {
            return languages.find { it.code == "ja" }
        }
        // Check CJK Unified Ideographs (Chinese)
        if (trimmed.any { it in '\u4E00'..'\u9FFF' }) {
            return languages.find { it.code == "zh" }
        }

        // Check Roman Urdu words
        val words = trimmed.lowercase(Locale.ROOT)
            .split(Regex("[^a-z]+"))
            .filter { it.isNotBlank() }
        val romanMatches = words.count { it in romanUrduKeywords }
        if (romanMatches >= 2 || (words.size in 1..4 && romanMatches >= 1)) {
            return languages.find { it.code == "ur" }
        }

        return null
    }
}
