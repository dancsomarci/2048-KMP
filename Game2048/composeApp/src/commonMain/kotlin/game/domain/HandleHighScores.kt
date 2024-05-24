package game.domain

import game.data.HighScoresRepository
import game.domain.util.tryWithSerializationError

class HighScores(
    private val highScoresRepository: HighScoresRepository
){
    fun load() = tryWithSerializationError {
        highScoresRepository.loadHighScores()?.data ?: 0
    }
    
    fun save(score: Int) = tryWithSerializationError {
        highScoresRepository.saveHighScore(score)
    }
}