package game.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GameStateEntity(
    val board: Map<Pair<Int, Int>, Int>, //(x,y) + value
    val boardSize: Int
)

