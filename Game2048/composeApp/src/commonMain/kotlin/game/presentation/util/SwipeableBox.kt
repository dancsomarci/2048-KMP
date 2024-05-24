package game.presentation.util

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.abs

@Composable
fun SwipeableBox(
    modifier: Modifier = Modifier,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    onSwipeUp: () -> Unit,
    onSwipeDown: () -> Unit,
    content: @Composable () -> Unit
) {
    val swipeThreshold = 50f  // Adjust this value as needed
    var totalDragAmount by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        val horizontalDrag = abs(totalDragAmount.x) > abs(totalDragAmount.y)
                        val verticalDrag = abs(totalDragAmount.y) > abs(totalDragAmount.x)

                        if (horizontalDrag && abs(totalDragAmount.x) > swipeThreshold) {
                            if (totalDragAmount.x > 0) {
                                onSwipeRight()
                            } else {
                                onSwipeLeft()
                            }
                        } else if (verticalDrag && abs(totalDragAmount.y) > swipeThreshold) {
                            if (totalDragAmount.y > 0) {
                                onSwipeDown()
                            } else {
                                onSwipeUp()
                            }
                        }

                        // Reset the total drag amount after handling the swipe
                        totalDragAmount = Offset.Zero
                    }
                ) { change, dragAmount ->
                    change.consume()
                    totalDragAmount += dragAmount
                }
            }
    ) {
        content()
    }
}