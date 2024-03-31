import androidx.compose.animation.core.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.random.Random

private const val POP_ANIMATION_DURATION = 500

@Composable
fun ColorCardStack(
    modifier: Modifier = Modifier,
    state: ColorCardStackState,
    parentSize: DpSize,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        val clipboardManager = LocalClipboardManager.current
        state.cardStack.forEach { card ->
            key(card.id) {
                ColorCard(
                    card = card,
                    parentSize = parentSize,
                    onPopFinish = { state.remove(card) },
                    onClick = { clipboardManager.setText(AnnotatedString(card.color.toString())) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ColorCard(
    modifier: Modifier = Modifier,
    card: ColorCard,
    parentSize: DpSize,
    onPopFinish: () -> Unit,
    onClick: () -> Unit,
) {
    var cardState by remember { mutableStateOf(CardState.Start) }
    val transition = updateTransition(targetState = cardState)

    LaunchedEffect(transition.isRunning) {
        if (!transition.isRunning && cardState == CardState.Popped)
            onPopFinish()
    }

    LaunchedEffect(card.isPopped) {
        cardState = if (card.isPopped) CardState.Popped else CardState.InStack
    }

    val rotation = animateRotation(transition)
    val offset = animateTranslation(transition, parentSize)

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
            Box {
                Text(
                    modifier = Modifier
                        .padding(4.dp)
                        .background(
                            color = Color.Black.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 4.dp),
                    text = "#${card.hexString}",
                    style = MaterialTheme.typography.body2,
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
private fun animateRotation(transition: Transition<CardState>): Float {
    // Rotation when card is created offsceen
    val rotationStart = remember { Random.nextInt(-90, 90).toFloat() }
    // Rotation when card is in the stack
    val rotationInStack = remember { Random.nextInt(-30, 30).toFloat() }
    // Rotation when card is popped
    val rotationPopped = remember { Random.nextInt(-720, 720).toFloat() }

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
    return rotation
}

@Composable
private fun animateTranslation(
    transition: Transition<CardState>,
    parentSize: DpSize
): IntOffset = with(LocalDensity.current) {

    // Offset when card is created offsceen
    val translationStart = remember {
        IntOffset(x = 0, y = (parentSize.height * -2).roundToPx())
    }

    // Offset when card is in the stack
    val translationInStack = remember {
        IntOffset(
            x = Random.nextInt(-25, 25).dp.roundToPx(),
            y = Random.nextInt(-25, 25).dp.roundToPx(),
        )
    }

    // Offset when card is popped
    val translationPopped = remember {
        IntOffset(
            x = Random.nextInt(-300, 300).dp.roundToPx(),
            y = (parentSize.height * 2).roundToPx()
        )
    }

    // Offset when card is tossed up to be popped
    val translationToss = remember {
        IntOffset(
            x = translationPopped.x / 2,
            y = -Random.nextInt(100, 300).dp.roundToPx()
        )
    }

    val offset by transition.animateIntOffset(
        transitionSpec = {
            when {
                CardState.Start isTransitioningTo CardState.InStack -> spring()
                CardState.InStack isTransitioningTo CardState.Popped -> keyframes {
                    durationMillis = POP_ANIMATION_DURATION

                    // Offset and time for top of toss. EaseOut for decelerate
                    translationToss
                        .at(POP_ANIMATION_DURATION / 2)
                        .using(EaseOut)

                    // Offset and time for end of toss when falling down. EaseIn for accelerate
                    translationPopped
                        .at(POP_ANIMATION_DURATION)
                        .using(EaseIn)
                }

                else -> spring()
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

    return offset
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
        state = rememberColorCardStackState(),
        parentSize = DpSize.Zero,
    )
}