package game.domain.util

fun <T> tryWithSerializationError(block: () -> T): T {
    try{
        return block()
    } catch (e: Exception) {
        throw RuntimeException("Error during Serialization!", e)
    }
}