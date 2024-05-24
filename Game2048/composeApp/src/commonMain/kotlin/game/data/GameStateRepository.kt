package game.data

import com.russhwolf.settings.Settings
import game.data.model.GameStateEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GameStateRepository(private val settings: Settings) {

    companion object {
        private const val GAME_STATE_KEY = "game_state"
    }
    
    private val json = Json{allowStructuredMapKeys=true}

    fun saveGameState(gameState: GameStateEntity) {
        val stringified = json.encodeToString(gameState)
        settings.putString(GAME_STATE_KEY, stringified)
    }

    fun loadGameState(): GameStateEntity? {
        val loadedEntity = settings.getStringOrNull(GAME_STATE_KEY)
        return if (loadedEntity != null) json.decodeFromString<GameStateEntity>(loadedEntity) else null
    }
}
