package game.domain

import game.data.GameStateRepository
import game.data.model.GameStateEntity
import game.domain.model.Tile
import game.domain.util.tryWithSerializationError

class Cache(
    private val gameStateRepository: GameStateRepository
){
    fun saveGame(board: List<List<Tile?>>){
        val entityBoard: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()
        
        for (i in board.indices){
            for (j in 0 until board[0].size){
                val gameTile = board[i][j]
                if (gameTile != null && !gameTile.merged){
                    entityBoard[Pair(j, i)] = gameTile.value
                }
            }
        }
        
        tryWithSerializationError{
            gameStateRepository.saveGameState(GameStateEntity(
                board = entityBoard,
                boardSize = board.size
                ))
        }
    }
    
    fun tryLoadGame(): List<List<Tile?>>?{
        val gameStateEntity = tryWithSerializationError{
            gameStateRepository.loadGameState()
        }
        
        return gameStateEntity?.run{
            val newBoard: MutableList<MutableList<Tile?>> = mutableListOf()
            
            for (i in 0 until this.boardSize){
                val row: MutableList<Tile?> = mutableListOf()
                for (j in 0 until this.boardSize){
                    val idx = Pair(j, i)
                    row.add(this.board[idx]?.let{ Tile(value = it) })
                }
                newBoard.add(row)
            }
            
            return newBoard
        }
    }
}
