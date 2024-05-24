package game.data

import com.russhwolf.settings.Settings
import game.data.model.HighScoresEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.math.max

class HighScoresRepository(private val settings: Settings) {

    companion object {
        private const val HIGH_SCORES_KEY = "high_score"
    }
    
    fun loadHighScores(): HighScoresEntity? {
        val loadedEntity = settings.getStringOrNull(HIGH_SCORES_KEY)
        return if (loadedEntity != null) Json.decodeFromString<HighScoresEntity>(loadedEntity) else null
    }

    fun saveHighScore(score: Int) {
        val newScores = HighScoresEntity(
            data = loadHighScores()?.data?.let { max(it, score) } ?: score
        )
        settings.putString(HIGH_SCORES_KEY, Json.encodeToString(newScores))
    }
}