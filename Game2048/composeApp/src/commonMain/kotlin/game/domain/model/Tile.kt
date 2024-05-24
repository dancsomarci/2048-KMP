package game.domain.model

import com.benasher44.uuid.uuid4

data class Tile(
    val id: String = uuid4().toString(),
    var value: Int,
    var merged: Boolean = false,
)