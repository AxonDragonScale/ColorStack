import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
    onClick: () -> Unit,
) {
    var state by remember { mutableStateOf(State.Start) }
    val transition = updateTransition(targetState = state)

    LaunchedEffect(card) {
        state = State.InStack
    }

    val rotation by transition.animateFloat(transitionSpec = { spring() }) {
        when (state) {
            State.Start -> card.rotationStart
            State.InStack -> card.rotationInStack
        }
    }

    Card(
        modifier = modifier
            .fillMaxSize(0.5f)
            .aspectRatio(1f)
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

enum class State {
    Start,
    InStack
}

@Preview
@Composable
private fun ColorCardStackPreview() {
    ColorCardStack(
        modifier = Modifier,
        state = rememberColorCardStackState()
    )
}