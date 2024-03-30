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
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

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

    LaunchedEffect(card) {
        cardState = CardState.InStack
    }

    val rotation by transition.animateFloat(transitionSpec = { spring() }) {
        when (it) {
            CardState.Start -> card.rotationStart
            CardState.InStack -> card.rotationInStack
        }
    }

    val density = LocalDensity.current
    val offset by transition.animateIntOffset(transitionSpec = { spring() }) {
        when (it) {
            CardState.Start -> card.translationStart(cardStackState.parentSize, density)
            CardState.InStack -> card.translationInStack(density)
        }
    }

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

enum class CardState {
    Start,
    InStack
}

@Preview
@Composable
private fun ColorCardStackPreview() {
    ColorCardStack(
        modifier = Modifier,
        state = rememberColorCardStackState(DpSize(100.dp, 100.dp))
    )
}