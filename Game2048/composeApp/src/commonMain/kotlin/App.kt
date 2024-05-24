
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import game.presentation.GameScreen
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App() {
    MaterialTheme {
        GameScreen()
    }
}