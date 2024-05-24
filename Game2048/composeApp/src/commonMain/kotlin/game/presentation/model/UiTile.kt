package game.presentation.model

import com.benasher44.uuid.uuid4
import game.domain.model.Tile

data class UiTile(
    val id: String = uuid4().toString(),
    var value: Int,
    var merged: Boolean = false,
    val x: Int,
    val y: Int
)

fun List<List<Tile?>>.asUiTiles() : List<UiTile>{
    val  tiles = mutableListOf<UiTile>()
    //set the coordinates of tiles
    for (i in indices){
        for (j in 0 until this[0].size){
            val gameTile = this[i][j]
            if (gameTile != null){
                tiles.add(
                    UiTile(
                    id = gameTile.id,
                    value = gameTile.value,
                    merged = gameTile.merged,
                    x = j,
                    y = i
                )
                )
            }
        }
    }
    return tiles
}