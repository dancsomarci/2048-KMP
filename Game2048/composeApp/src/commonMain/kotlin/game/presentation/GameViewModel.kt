package game.presentation

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import game.domain.Cache
import game.domain.Game2048
import game.domain.HighScores
import game.presentation.model.UiTile
import game.presentation.model.asUiTiles
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(
    private val cache: Cache,
    private val highScores: HighScores
) : ViewModel(){
    
    private val _state = MutableStateFlow(GameUiState())
    val state = _state.asStateFlow()
    
    private lateinit var game: Game2048
    
    init{
        viewModelScope.launch {
            val possibleGameState =  cache.tryLoadGame()
            game = if (possibleGameState != null){
                Game2048(possibleGameState)
            } else{
                Game2048(_state.value.boardSize)
            }
            
            _state.update {
                it.copy(
                    board = game.board.asUiTiles(),
                    score = game.score,
                    highScore = highScores.load(),
                    boardSize = game.board.size
                )
            }
        }
    }

    fun onEvent(event: GameEvent){
        viewModelScope.launch {
            when(event){
                is GameEvent.Move -> move(event.dir)
                GameEvent.Reset -> reset()
                is GameEvent.BoardSizeChanged -> changeBoardSize(event.newSize)
                GameEvent.Save -> save()
            }
        }
    }
    
    private fun save(){
        cache.saveGame(game.board)
        // TODO Possible improvement: Add Proper Error handling
    }
    
    private fun changeBoardSize(newSize: Int){
        val (min, max) = _state.value.boardSizeBoundaries
        if (newSize in min..max){
            game = Game2048(newSize)
            game.reset()
            _state.update {
                it.copy(
                    board = game.board.asUiTiles(),
                    score = game.score,
                    boardSize = newSize
                )
            }
        }
    }
    
    private fun move(dir: GameEvent.Direction){
        game.makeMove(dir.asGame2048Direction())
        val highScore = if (game.score > _state.value.highScore){
            highScores.save(game.score)
            game.score
        } else _state.value.highScore
        
        if (game.checkGameOver()){
            _state.update { it.copy(
                board = game.board.asUiTiles(),
                score = game.score,
                highScore = highScore,
                gameOver = true
            )}
        } else if (game.checkWin()){
            _state.update { it.copy(
                board = game.board.asUiTiles(),
                score = game.score,
                highScore = highScore,
                gameWon = true
                )}
        } else {
            _state.update {
                it.copy(
                    board = game.board.asUiTiles(),
                    score = game.score,
                    highScore = highScore,
                )
            }
        }
    }
    
    private fun reset(){
        game.reset()
        _state.update {
            it.copy(
                board = game.board.asUiTiles(),
                score = game.score
            )
        }
    }
}

data class GameUiState(
    val board: List<UiTile> = emptyList(),
    val score: Int = 0,
    val boardSize: Int = 4,
    val boardSizeBoundaries: Pair<Int, Int> = Pair(3, 7),
    val gameOver: Boolean = false,
    val gameWon: Boolean = false,
    val highScore: Int = 0
)

sealed class GameEvent{
    enum class Direction {
        UP, DOWN, LEFT, RIGHT;
    
        fun asGame2048Direction(): Game2048.Direction {
            return when (this) {
                UP -> Game2048.Direction.UP
                DOWN -> Game2048.Direction.DOWN
                LEFT -> Game2048.Direction.LEFT
                RIGHT -> Game2048.Direction.RIGHT
            }
        }
    }
    
    data object Reset: GameEvent()
    data class Move(val dir: Direction): GameEvent()
    data class BoardSizeChanged(val newSize: Int): GameEvent()
    data object Save: GameEvent()
}

