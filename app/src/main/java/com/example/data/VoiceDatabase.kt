package com.example.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "audio_history")
data class AudioHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val textSnippet: String,
    val fullText: String,
    val languageCode: String,
    val languageName: String,
    val voiceId: String,
    val voiceName: String,
    val gender: String,
    val styleName: String,
    val providerName: String,
    val filePath: String,
    val fileName: String,
    val mimeType: String,
    val formatExtension: String,
    val durationMs: Long,
    val fileSizeBytes: Long,
    val speed: Float,
    val pitch: Float,
    val characterCount: Int,
    val createdAt: Long = System.currentTimeMillis(),
    val isSavedForever: Boolean = true
)

@Entity(tableName = "favorite_voices")
data class FavoriteVoiceEntity(
    @PrimaryKey val voiceId: String,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_account")
data class UserAccountEntity(
    @PrimaryKey val id: Int = 1,
    val isLoggedIn: Boolean = false,
    val displayName: String = "Guest Creator",
    val email: String = "",
    val authMethod: String = "Free Guest Tier",
    val dailyAllowanceChars: Int = 5000,
    val usedCharsToday: Int = 0,
    val lastResetEpochDay: Long = System.currentTimeMillis() / 86_400_000L,
    val totalGenerations: Int = 0,
    val totalDownloads: Int = 0,
    val autoDeleteTempAudio: Boolean = false
)

@Dao
interface VoiceStudioDao {
    @Query("SELECT * FROM audio_history ORDER BY createdAt DESC")
    fun observeHistory(): Flow<List<AudioHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(item: AudioHistoryEntity): Long

    @Query("DELETE FROM audio_history WHERE id = :id")
    suspend fun deleteHistoryById(id: Long)

    @Query("DELETE FROM audio_history")
    suspend fun clearAllHistory()

    @Query("SELECT * FROM favorite_voices")
    fun observeFavorites(): Flow<List<FavoriteVoiceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(entity: FavoriteVoiceEntity)

    @Query("DELETE FROM favorite_voices WHERE voiceId = :voiceId")
    suspend fun removeFavorite(voiceId: String)

    @Query("SELECT * FROM user_account WHERE id = 1")
    fun observeAccount(): Flow<UserAccountEntity?>

    @Query("SELECT * FROM user_account WHERE id = 1")
    suspend fun getAccountOnce(): UserAccountEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAccount(account: UserAccountEntity)
}

@Database(
    entities = [
        AudioHistoryEntity::class,
        FavoriteVoiceEntity::class,
        UserAccountEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class VoiceStudioDatabase : RoomDatabase() {
    abstract fun dao(): VoiceStudioDao

    companion object {
        @Volatile
        private var INSTANCE: VoiceStudioDatabase? = null

        fun getInstance(context: Context): VoiceStudioDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VoiceStudioDatabase::class.java,
                    "msk_ai_voice_studio.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class VoiceStudioRepository(private val dao: VoiceStudioDao) {
    val historyFlow: Flow<List<AudioHistoryEntity>> = dao.observeHistory()
    val favoritesFlow: Flow<List<FavoriteVoiceEntity>> = dao.observeFavorites()
    val accountFlow: Flow<UserAccountEntity?> = dao.observeAccount()

    suspend fun ensureAccountInitialized(): UserAccountEntity {
        val currentDay = System.currentTimeMillis() / 86_400_000L
        val existing = dao.getAccountOnce()
        return if (existing == null) {
            val fresh = UserAccountEntity(lastResetEpochDay = currentDay)
            dao.saveAccount(fresh)
            fresh
        } else if (existing.lastResetEpochDay != currentDay) {
            val reset = existing.copy(
                usedCharsToday = 0,
                lastResetEpochDay = currentDay
            )
            dao.saveAccount(reset)
            reset
        } else {
            existing
        }
    }

    suspend fun recordGenerationUsage(charCount: Int, historyItem: AudioHistoryEntity): Long {
        val acct = ensureAccountInitialized()
        dao.saveAccount(
            acct.copy(
                usedCharsToday = (acct.usedCharsToday + charCount).coerceAtMost(acct.dailyAllowanceChars),
                totalGenerations = acct.totalGenerations + 1
            )
        )
        return dao.insertHistory(historyItem)
    }

    suspend fun recordDownload() {
        val acct = ensureAccountInitialized()
        dao.saveAccount(acct.copy(totalDownloads = acct.totalDownloads + 1))
    }

    suspend fun toggleFavorite(voiceId: String, isCurrentlyFav: Boolean) {
        if (isCurrentlyFav) {
            dao.removeFavorite(voiceId)
        } else {
            dao.addFavorite(FavoriteVoiceEntity(voiceId = voiceId))
        }
    }

    suspend fun signInUser(name: String, email: String, method: String) {
        val acct = ensureAccountInitialized()
        dao.saveAccount(
            acct.copy(
                isLoggedIn = true,
                displayName = name.trim().ifBlank { "MSK Studio Creator" },
                email = email.trim(),
                authMethod = method,
                dailyAllowanceChars = 15000 // Registered Free Account gets 15,000 chars/day
            )
        )
    }

    suspend fun signOutOrDeleteAccount(clearHistory: Boolean) {
        if (clearHistory) {
            dao.clearAllHistory()
        }
        val currentDay = System.currentTimeMillis() / 86_400_000L
        dao.saveAccount(
            UserAccountEntity(
                id = 1,
                isLoggedIn = false,
                displayName = "Guest Creator",
                email = "",
                authMethod = "Free Guest Tier",
                dailyAllowanceChars = 5000,
                usedCharsToday = 0,
                lastResetEpochDay = currentDay
            )
        )
    }

    suspend fun deleteHistoryItem(item: AudioHistoryEntity) {
        try {
            val file = java.io.File(item.filePath)
            if (file.exists()) file.delete()
        } catch (_: Exception) {}
        dao.deleteHistoryById(item.id)
    }

    suspend fun clearAllHistoryFiles(items: List<AudioHistoryEntity>) {
        items.forEach {
            try {
                val f = java.io.File(it.filePath)
                if (f.exists()) f.delete()
            } catch (_: Exception) {}
        }
        dao.clearAllHistory()
    }
}
