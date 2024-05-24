package game.domain

import game.domain.model.Tile
import kotlin.random.Random

class Game2048(
    private val gridSize: Int
) {

    private lateinit var _board: Array<Array<Tile?>>
    
    constructor(initialBoard: List<List<Tile?>>) : this(initialBoard.size) {
        _board = Array(initialBoard.size) { row ->
            Array(initialBoard.size) { col ->
                initialBoard[row][col]
            }
        }
    }
    
    val board: List<List<Tile?>>
        get() = _board.map { row ->
                row.map { tile ->
                    tile?.copy()
                }
            }
    
    val score: Int
        get() = _board.flatten().filterNotNull().sumOf { it.value }

    private fun initializeBoard() {
        _board =  Array(gridSize) { Array(gridSize) { null } }
        addNewTile()
        addNewTile()
    }

    init {
        initializeBoard()
    }
    
    fun reset(){
        initializeBoard()
    }
    
    private fun addNewTile() {
        val emptyTiles = mutableListOf<Pair<Int, Int>>()
        for (i in _board.indices) {
            for (j in _board[i].indices) {
                if (_board[i][j] == null) {
                    emptyTiles.add(Pair(i, j))
                }
            }
        }
        if (emptyTiles.isNotEmpty()) {
            val (i, j) = emptyTiles.random()
            _board[i][j] = Tile(
                value = if (Random.nextInt(10) < 9) 2 else 4,
            )
        }
    }

    // Extension function to transpose a 2D array
    private fun Array<Array<Tile?>>.transpose(): Array<Array<Tile?>> {
        val newBoard = Array(this.size) { Array<Tile?>(this[0].size){null} }
        for (i in this.indices) {
            for (j in this[i].indices) {
                newBoard[j][i] = this[i][j]
            }
        }
        return newBoard
    }
    
    // Extension function to reverse rows of a 2D array
    private fun Array<Array<Tile?>>.reverse():Array<Array<Tile?>> {
        return this.map { it.reversedArray() }.toTypedArray()
    }
    
    // Extension function to compress non-zero values to the left
    private fun Array<Array<Tile?>>.compress(): Array<Array<Tile?>> {
        val newBoard = Array(this.size) { Array<Tile?>(this[0].size){null} }
        for (i in this.indices) {
            var pos = 0
            for (j in this[i].indices) {
                if (this[i][j] != null) {
                    newBoard[i][pos] = this[i][j]
                    pos++
                }
            }
        }
        return newBoard
    }
    
    // Extension function to merge equal adjacent values and compress again
    private fun Array<Array<Tile?>>.merge(): Array<Array<Tile?>> {
        for (i in this.indices) {
            for (j in 0 until this[i].size - 1) {
                if (this[i][j] != null && this[i][j]?.value == this[i][j + 1]?.value) {
                    this[i][j]?.value = this[i][j]?.value?.times(2)!!
                    this[i][j + 1]?.merged = true
                }
            }
        }
        return this
    }

    private fun moveLeft(): Array<Array<Tile?>> = _board.compress().merge().compress()
    private fun moveRight(): Array<Array<Tile?>> = _board.reverse().compress().merge().compress().reverse()
    private fun moveUp(): Array<Array<Tile?>> = _board.transpose().compress().merge().compress().transpose()
    private fun moveDown(): Array<Array<Tile?>> = _board.transpose().reverse().compress().merge().compress().reverse().transpose()

    enum class Direction{
        UP, DOWN, LEFT, RIGHT
    }

    fun makeMove(direction: Direction) {
        //remove merged tiles
        for (i in 0 until _board.size){
            for (j in 0 until _board[0].size){
                if (_board[i][j]?.merged == true){
                    _board[i][j] = null
                }
            }
        }
        
        //move tiles (merged ones are also preserved)
        val savedBoard = board
        _board = when (direction) {
            Direction.UP -> moveUp()
            Direction.DOWN -> moveDown()
            Direction.LEFT -> moveLeft()
            Direction.RIGHT -> moveRight()
        }
        
        if (savedBoard != board){
            addNewTile()
        }
    }

    fun checkWin(): Boolean {
        return _board.any { row -> row.any { it?.value == 2048 } }
    }

    fun checkGameOver(): Boolean {
        if (_board.any { row -> row.any{ it == null} }) {
            return false
        }
        for (i in _board.indices) {
            for (j in 0 until _board[i].size - 1) {
                if (_board[i][j]?.value == _board[i][j + 1]?.value || _board[j][i]?.value == _board[j + 1][i]?.value) {
                    return false
                }
            }
        }
        return true
    }
}