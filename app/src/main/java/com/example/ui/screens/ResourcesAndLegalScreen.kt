package com.example.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.VoiceCatalog
import com.example.ui.VoiceStudioViewModel
import com.example.ui.components.AdSenseReservedSlotBanner
import com.example.ui.components.MskBrandFooter
import com.example.ui.theme.MskCyanBright
import com.example.ui.theme.MskElectricBlue
import com.example.ui.theme.MskEmeraldSuccess

data class ResourcePageTab(
    val id: String,
    val title: String,
    val icon: ImageVector
)

@Composable
fun ResourcesAndLegalScreen(
    viewModel: VoiceStudioViewModel,
    selectedSection: String,
    modifier: Modifier = Modifier
) {
    val pages = listOf(
        ResourcePageTab("about", "About Us", Icons.Default.Info),
        ResourcePageTab("guide", "AI Voice Guide", Icons.Default.AutoAwesome),
        ResourcePageTab("languages", "Supported Languages", Icons.Default.Language),
        ResourcePageTab("faq", "FAQ", Icons.Default.HelpOutline),
        ResourcePageTab("licensing", "Voice Licensing", Icons.Default.Verified),
        ResourcePageTab("privacy", "Privacy & Cookies", Icons.Default.PrivacyTip),
        ResourcePageTab("terms", "Terms & Conditions", Icons.Default.Gavel),
        ResourcePageTab("contact", "Contact Us", Icons.Default.MailOutline),
        ResourcePageTab("setup", "Setup & Architecture", Icons.Default.Code)
    )

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = modifier.fillMaxSize()
    ) {
        item {
            Column {
                Text(
                    text = "Knowledge Base, Guides & Legal Pages",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Original guides, supported language matrix, licensing policies, and Google AdSense-compliant transparency pages.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Section Selector Chips
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                pages.forEach { page ->
                    val active = (selectedSection == page.id)
                    FilterChip(
                        selected = active,
                        onClick = { viewModel.selectResourceSection(page.id) },
                        leadingIcon = {
                            Icon(
                                imageVector = page.icon,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        label = { Text(page.title) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MskElectricBlue,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.testTag("resource_chip_${page.id}")
                    )
                }
            }
        }

        // Active Section Content
        item {
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(22.dp))
                    .testTag("resource_content_$selectedSection")
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(20.dp)
                ) {
                    when (selectedSection) {
                        "about" -> AboutUsSectionContent()
                        "guide" -> AiVoiceGuideSectionContent()
                        "languages" -> SupportedLanguagesSectionContent()
                        "faq" -> FaqSectionContent()
                        "licensing" -> VoiceLicensingSectionContent()
                        "privacy" -> PrivacyAndCookiesSectionContent()
                        "terms" -> TermsAndConditionsSectionContent()
                        "contact" -> ContactUsSectionContent()
                        "setup" -> SetupAndArchitectureSectionContent()
                        else -> AboutUsSectionContent()
                    }
                }
            }
        }

        item {
            AdSenseReservedSlotBanner(slotName = "RESOURCES_ARTICLE_BOTTOM")
        }

        item {
            MskBrandFooter(
                onOpenResourceSection = { viewModel.selectResourceSection(it) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun AboutUsSectionContent() {
    Text(
        text = "About MSK AI Voice Studio",
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
        color = MskCyanBright
    )
    Text(
        text = "Created with passion by MSK & Team",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
    )
    Text(
        text = "MSK AI Voice Studio was founded by MSK and his friend with a clear mission: to make studio-quality, natural-sounding multilingual speech synthesis accessible to creators, educators, students, journalists, and businesses worldwide—with first-class support for Urdu (اردو & Roman Urdu), English, Hindi, Arabic, Pashto, Punjabi, and over 17 international languages.",
        style = MaterialTheme.typography.bodyMedium
    )
    Text(
        text = "Our Vision & Core Principles",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
    )
    BulletPoint("Authentic Multilingual Prosody: Engineered specifically so South Asian, Middle Eastern, and global scripts sound natural rather than robotic.")
    BulletPoint("Honest Free Tier: Visitors can generate, preview, and download real audio files immediately without submitting credit cards.")
    BulletPoint("Dual-Engine Reliability: Combines Cloud Neural AI (Gemini 2.5 Flash TTS) with a zero-cost On-Device Speech Synthesis fallback so your workflow never stops.")
    BulletPoint("Mobile-First Downloads: Every generated voiceover exports as a standard WAV/MP3 audio stream directly to your phone or desktop Downloads folder.")
}

@Composable
private fun AiVoiceGuideSectionContent() {
    Text(
        text = "Comprehensive AI Voice Generation Guide",
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
        color = MskCyanBright
    )
    Text(
        text = "How to Craft Studio-Grade Voiceovers in Urdu, Roman Urdu, English & 17+ Languages",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
    )
    BulletPoint("1. Punctuation Controls Pacing: Neural TTS models use commas (، / ,), full stops (۔ / .), and question marks (؟ / ?) to determine natural breathing pauses and pitch inflection. Use our built-in 'Fix Punctuation' tool before generating.")
    BulletPoint("2. Urdu Script vs. Roman Urdu: While MSK AI Voice Studio understands Roman Urdu ('Aap kaise hain'), converting Roman Urdu into native Urdu script ('آپ کیسے ہیں؟') using our one-tap converter gives the richest vowel pronunciation.")
    BulletPoint("3. Matching Voice Style to Content: Choose 'Podcast Host' for conversational YouTube intros, 'News Presenter' for current affairs bulletins, 'Storytelling' for audiobooks and afsanay, and 'Educational' for tutorials.")
    BulletPoint("4. Long Text Narration: When narrating articles or chapters over 1,500 characters, enable 'Long Text (4k)' mode. Our backend splits your text at natural sentence boundaries and merges the synthesized audio into a single downloadable file.")
}

@Composable
private fun SupportedLanguagesSectionContent() {
    Text(
        text = "Supported Languages & Scripts (${VoiceCatalog.languages.size} Languages)",
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
        color = MskCyanBright
    )
    Text(
        text = "MSK AI Voice Studio only displays languages and voices genuinely supported by our connected neural and on-device speech engines:",
        style = MaterialTheme.typography.bodyMedium
    )
    VoiceCatalog.languages.forEach { lang ->
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "${lang.flagEmoji} ${lang.name} — ${lang.nativeName} [${lang.bcp47}]",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Sample: ${lang.sampleText}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun FaqSectionContent() {
    Text(
        text = "Frequently Asked Questions (FAQ)",
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
        color = MskCyanBright
    )
    FaqItem(
        q = "Is MSK AI Voice Studio free to use?",
        a = "Yes! Every visitor gets 5,000 free Cloud AI characters per day without registering, or 15,000 free characters per day with a free creator account. Additionally, our On-Device Speech Engine fallback is 100% free with no character deductions."
    )
    FaqItem(
        q = "Can I type in both Urdu script and Roman Urdu?",
        a = "Yes. You can write directly in Urdu script (اردو) or Roman Urdu. We also include a one-tap 'Roman → اردو Script' AI tool to transliterate Roman Urdu for maximum pronunciation clarity."
    )
    FaqItem(
        q = "How do audio downloads work on Android, iPhone, and desktop?",
        a = "When you tap 'Download Audio', the actual synthesized WAV/MP3 file is saved directly to your device's Downloads/MSK_AI_Voice_Studio folder with a timestamped filename (e.g., msk-ai-voice-2026-10-10.wav). You can also tap 'Share' to send the audio file to WhatsApp, Google Drive, iCloud Files, or video editors."
    )
    FaqItem(
        q = "Can I use the generated audio for YouTube videos and commercial projects?",
        a = "Yes. Audio generated through MSK AI Voice Studio using our prebuilt voices may be used in YouTube videos, podcasts, e-learning courses, presentations, and commercial media in compliance with our Voice Usage & Licensing Policy."
    )
}

@Composable
private fun VoiceLicensingSectionContent() {
    Text(
        text = "Voice Usage & Licensing Information",
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
        color = MskCyanBright
    )
    BulletPoint("Commercial & Creator Rights: Users retain full rights to distribute and monetize original audio files generated with MSK AI Voice Studio across YouTube, podcasts, audiobooks, apps, and advertisements.")
    BulletPoint("Prohibited Uses: You may not use generated voices to impersonate real individuals without explicit written consent, generate deceptive political/medical misinformation, or engage in unlawful harassment.")
    BulletPoint("No Unauthorized Voice Cloning: MSK AI Voice Studio only offers prebuilt licensed studio voices and verified system voices. Custom voice cloning is never performed without verified speaker authorization.")
}

@Composable
private fun PrivacyAndCookiesSectionContent() {
    Text(
        text = "Privacy Policy & Cookie Policy",
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
        color = MskCyanBright
    )
    Text(
        text = "Effective Date: October 10, 2026 • MSK AI Voice Studio",
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    BulletPoint("Text & Audio Privacy: Text submitted for speech generation is processed solely to synthesize your requested audio file. Temporary chunk files are automatically deleted immediately after synthesis.")
    BulletPoint("Local Audio Storage: Generated audio history is stored privately on your device and can be deleted individually or cleared completely at any time from the Dashboard.")
    BulletPoint("API Key Security: Cloud API keys are managed via server-side/encrypted BuildConfig secrets (`GEMINI_API_KEY`) and are never exposed in public client HTML or third-party trackers.")
    BulletPoint("Google AdSense & Cookies: When Google AdSense advertisements are enabled on web/hybrid deployments, Google and certified partners may use cookies (`DoubleClick DART`) to serve non-intrusive ads based on prior visits. Users may opt out of personalized advertising at adssettings.google.com.")
}

@Composable
private fun TermsAndConditionsSectionContent() {
    Text(
        text = "Terms and Conditions",
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
        color = MskCyanBright
    )
    BulletPoint("1. Acceptance of Terms: By accessing MSK AI Voice Studio, you agree to abide by these Terms and our Fair Usage Policy.")
    BulletPoint("2. Fair Usage & Rate Limits: Free tier usage is subject to daily character allowances to prevent automated abuse and unexpected API billing.")
    BulletPoint("3. Service Availability: If a cloud speech provider experiences downtime or quota limits, the platform transparently notifies you and provides an On-Device Speech Synthesis fallback.")
    BulletPoint("4. Attribution: Founded by MSK and his friend. Created with passion by MSK & Team.")
}

@Composable
private fun ContactUsSectionContent() {
    Text(
        text = "Contact MSK & Team",
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
        color = MskCyanBright
    )
    Text(
        text = "Have feedback, Urdu/Pashto voice requests, or enterprise partnership inquiries? We would love to hear from you.",
        style = MaterialTheme.typography.bodyMedium
    )
    BulletPoint("Founder & Lead Engineering: MSK & Team")
    BulletPoint("Support & Inquiries Email: support@mskaivoice.studio")
    BulletPoint("Creator Community & Feedback: feedback@mskaivoice.studio")
    BulletPoint("Response Time: Within 24–48 business hours.")
}

@Composable
private fun SetupAndArchitectureSectionContent() {
    Text(
        text = "Technical Setup, Environment Variables & Deployment Guide",
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
        color = MskCyanBright
    )
    Text(
        text = "1. Environment Variable & API Key Setup",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
    )
    BulletPoint("GEMINI_API_KEY: Open the Secrets panel in Google AI Studio and set `GEMINI_API_KEY` (declared in `.env.example`). This powers `gemini-2.5-flash-preview-tts` neural voice synthesis and `gemini-3.5-flash` script generation.")
    BulletPoint("Zero-Config Fallback: Even if `GEMINI_API_KEY` is not yet configured or billing is restricted, the app automatically falls back to Android's native `TextToSpeech.synthesizeToFile` and built-in acoustic WAV synthesizer so audio generation and downloads always work.")
    Text(
        text = "2. Google AdSense Setup",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
    )
    BulletPoint("Reserved non-intrusive ad containers (`AdSenseReservedSlotBanner`) are placed at the bottom of content views, far away from Generate, Play, and Download buttons to prevent accidental clicks.")
    BulletPoint("Replace placeholder `ca-pub-XXXXXXXXXXXXXXXX` with your approved Publisher ID only after Google AdSense account verification.")
    Text(
        text = "3. Features Requiring Paid Cloud Tiers",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
    )
    BulletPoint("High-Volume Cloud Neural TTS (>15,000 chars/day): Requires a billed Gemini API / Google Cloud TTS / Azure Speech key.")
    BulletPoint("Custom Voice Cloning: Requires enterprise voice consent verification and a dedicated custom voice model endpoint.")
}

@Composable
private fun BulletPoint(text: String) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = MskEmeraldSuccess,
            modifier = Modifier
                .padding(top = 2.dp)
                .size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun FaqItem(q: String, a: String) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = q,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MskCyanBright
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = a,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
