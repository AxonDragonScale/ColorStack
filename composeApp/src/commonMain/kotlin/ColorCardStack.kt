import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.random.Random

private const val POP_ANIMATION_DURATION = 500

@Composable
fun ColorCardStack(
    modifier: Modifier = Modifier,
    state: ColorCardStackState
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        state.cardStack.forEach { card ->
            key(card.id) {
                ColorCard(
                    card = card,
                    cardStackState = state,
                    onClick = { }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalStdlibApi::class)
@Composable
private fun ColorCard(
    modifier: Modifier = Modifier,
    card: ColorCard,
    cardStackState: ColorCardStackState,
    onClick: () -> Unit,
) {
    var cardState by remember { mutableStateOf(CardState.Start) }
    val transition = updateTransition(targetState = cardState)

    LaunchedEffect(transition.isRunning) {
        if (!transition.isRunning && cardState == CardState.Popped)
            cardStackState.remove(card)
    }

    LaunchedEffect(card.isPopped) {
        cardState = if (card.isPopped) CardState.Popped else CardState.InStack
    }


    val rotationStart = remember { Random.nextInt(-90, 90).toFloat() }      // Rotation when card is created offsceen
    val rotationInStack = remember { Random.nextInt(-30, 30).toFloat() }    // Rotation when card is in the stack
    val rotationPopped = remember { Random.nextInt(-720, 720).toFloat() }   // Rotation when card is popped
    val rotation by transition.animateFloat(
        transitionSpec = {
            when {
                CardState.Start isTransitioningTo CardState.InStack -> spring()
                CardState.InStack isTransitioningTo CardState.Popped ->
                    tween(durationMillis = POP_ANIMATION_DURATION, easing = EaseIn)

                else -> spring()
            }
        },
        targetValueByState = {
            when (it) {
                CardState.Start -> rotationStart
                CardState.InStack -> rotationInStack
                CardState.Popped -> rotationPopped
            }
        }
    )

    val density = LocalDensity.current
    val parentSize = cardStackState.parentSize
    val translationStart = remember { translationStart(parentSize, density) } // Offset when card is created offsceen
    val translationInStack = remember { translationInStack(density) } // Offset when card is in the stack
    val translationPopped = remember { translationPopped(parentSize, density) } // Offset when card is popped
    val translationPopToss = remember {
        translationPopToss(
            parentSize,
            density,
            translationPopped
        )
    } // Offset when card is tossed up to be popped
    val offset by transition.animateIntOffset(
        transitionSpec = {
            if (targetState == CardState.InStack) spring()
            else keyframes {
                durationMillis = POP_ANIMATION_DURATION

                // Offset and time for top of toss. EaseOut for decelerate
                translationPopToss
                    .at(POP_ANIMATION_DURATION / 2)
                    .using(EaseOut)

                // Offset and time for end of toss when falling down. EaseIn for accelerate
                translationPopped
                    .at(POP_ANIMATION_DURATION)
                    .using(EaseIn)
            }
        },
        targetValueByState = {
            when (it) {
                CardState.Start -> translationStart
                CardState.InStack -> translationInStack
                CardState.Popped -> translationPopped
            }
        }
    )

    Card(
        modifier = modifier
            .fillMaxSize(0.5f)
            .aspectRatio(1f)
            .offset { offset }
            .rotate(rotation),
        onClick = onClick,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(12.dp),
    ) {
        Card(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxSize(),
            backgroundColor = card.color,
            shape = RoundedCornerShape(8.dp),
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = "#${card.color.toArgb().toHexString(HexFormat.UpperCase).drop(2)}",
                style = MaterialTheme.typography.body1,
                color = Color.White,
            )
        }
    }
}

fun translationStart(parentSize: DpSize, density: Density) = with(density) {
    IntOffset(x = 0, y = (parentSize.height * -2).roundToPx())
}

fun translationInStack(density: Density) = with(density) {
    IntOffset(
        x = Random.nextInt(-25, 25).dp.roundToPx(),
        y = Random.nextInt(-25, 25).dp.roundToPx(),
    )
}

fun translationPopped(parentSize: DpSize, density: Density) = with(density) {
    IntOffset(
        x = Random.nextInt(-300, 300).dp.roundToPx(),
        y = (parentSize.height * 2).roundToPx()
    )
}

fun translationPopToss(parentSize: DpSize, density: Density, translationPopped: IntOffset) = with(density) {
    IntOffset(
        x = translationPopped.x / 2,
        y = -Random.nextInt(100, 300).dp.roundToPx()
    )
}

enum class CardState {
    Start,
    InStack,
    Popped,
}

@Preview
@Composable
private fun ColorCardStackPreview() {
    ColorCardStack(
        modifier = Modifier,
        state = rememberColorCardStackState(DpSize(100.dp, 100.dp))
    )
}