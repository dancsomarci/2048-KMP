package game.presentation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.russhwolf.settings.Settings
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import game.data.GameStateRepository
import game.data.HighScoresRepository
import game.domain.Cache
import game.domain.HighScores
import game.presentation.model.UiTile
import game.presentation.util.SwipeableBox

@Composable
fun GameScreen(){
    val viewModel = getViewModel(Unit, viewModelFactory {
        GameViewModel(
            cache=Cache(GameStateRepository(Settings())),
            highScores = HighScores(HighScoresRepository(Settings()))
        )
    })
    val state by viewModel.state.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HighScore: ${state.highScore}") },
                actions = {
                    IconButton(onClick = { viewModel.onEvent(GameEvent.Reset) }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reset")
                    }
                    IconButton(onClick = { viewModel.onEvent(GameEvent.Save) }) {
                        Icon(Icons.Default.Lock, contentDescription = "Save")
                    }
                }
            )
        },
        bottomBar = {
            BottomBar(
                state = state,
                onBoardSizeChange = { viewModel.onEvent(GameEvent.BoardSizeChanged(it)) }
            )
        }
    ){  padding ->
        SwipeableBox(
            onSwipeUp = { viewModel.onEvent(GameEvent.Move(GameEvent.Direction.UP)) },
            onSwipeDown = { viewModel.onEvent(GameEvent.Move(GameEvent.Direction.DOWN)) },
            onSwipeRight = { viewModel.onEvent(GameEvent.Move(GameEvent.Direction.RIGHT)) },
            onSwipeLeft = { viewModel.onEvent(GameEvent.Move(GameEvent.Direction.LEFT)) }
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray)
                    .padding(padding),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                GameBoard(
                    board = state.board,
                    size = state.boardSize,
                    modifier = Modifier.padding(20.dp)
                )
            }
        }
    }
}

@Composable
fun GameBoard(board: List<UiTile>, size: Int, modifier: Modifier = Modifier) {
    var tileSize by remember { mutableStateOf(64.dp) }
    val spacing = 4.dp
    val density = LocalDensity.current

    Box(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                val layoutSize = coordinates.size
                val boardSizePx = layoutSize.width.coerceAtMost(layoutSize.height)
                tileSize = with(density) { ((boardSizePx - (spacing.toPx() * (size - 1))) / size).toDp() }
            }
            .aspectRatio(1f)
            .size((tileSize + spacing) * size) // Adjust size to fit all tiles
            .drawBehind {
                // Draw the background empty tiles.
                for (row in 0 until size) {
                    for (col in 0 until size) {
                        drawRoundRect(
                            color = getTileColor(0),
                            topLeft = Offset(col * (tileSize.toPx() + spacing.toPx()), row * (tileSize.toPx() + spacing.toPx())),
                            size = Size(tileSize.toPx(), tileSize.toPx()),
                            cornerRadius = CornerRadius(5.dp.toPx()),
                        )
                    }
                }
            },
        contentAlignment = Alignment.TopStart // Align content to the top start for correct offsets
    ) {
        board.forEach { tile ->
            key(tile.id) {
                AnimatedTile(tile = tile, tileSize = tileSize, spacing = spacing)
            }
        }
    }
}

@Composable
fun AnimatedTile(tile: UiTile, tileSize: Dp = 64.dp, spacing: Dp = 4.dp) {
    val xOffset by animateDpAsState(targetValue = ((tileSize + spacing)) * tile.x)
    val yOffset by animateDpAsState(targetValue = ((tileSize + spacing)) * tile.y)
    val alpha by animateFloatAsState(targetValue = if (tile.merged) 0f else 1f)

    Box(
        modifier = Modifier
            .offset(x = xOffset, y = yOffset)
            .size(tileSize)
            .background(getTileColor(tile.value).copy(alpha = alpha), shape = RoundedCornerShape(5.dp))
            .alpha(alpha),
        contentAlignment = Alignment.Center
    ) {
        BasicText(
            text = tile.value.toString(),
            style = MaterialTheme.typography.h4.copy(
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 24.sp
            )
        )
    }
}

fun getTileColor(value: Int): Color {
    return when (value) {
        0 -> Color(0xFFCDC1B4)
        2 -> Color(0xFFEEE4DA)
        4 -> Color(0xFFEDE0C8)
        8 -> Color(0xFFF2B179)
        16 -> Color(0xFFF59563)
        32 -> Color(0xFFF67C5F)
        64 -> Color(0xFFF65E3B)
        128 -> Color(0xFFEDCF72)
        256 -> Color(0xFFEDCC61)
        512 -> Color(0xFFEDC850)
        1024 -> Color(0xFFEDC53F)
        2048 -> Color(0xFFEDC22E)
        else -> Color(0xFF3C3A32)
    }
}

@Composable
fun BottomBar(state: GameUiState, onBoardSizeChange: (Int) -> Unit) {
    var sliderValue by remember { mutableFloatStateOf(state.boardSize.toFloat()) }
    val (min, max) = state.boardSizeBoundaries
    
    LaunchedEffect(state.boardSize) {
        sliderValue = state.boardSize.toFloat()
    }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Board Size: ${state.boardSize}")
        Slider(
            value = sliderValue,
            onValueChange = { sliderValue = it },
            onValueChangeFinished = {
                onBoardSizeChange(sliderValue.toInt())
            },
            valueRange = min.toFloat()..max.toFloat(),
            steps = max-min-1
        )
    }
}