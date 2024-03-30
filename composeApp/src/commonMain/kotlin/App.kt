import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val density = LocalDensity.current
            var parentSize by remember { mutableStateOf(DpSize.Zero) }
            val colorCardStackState = rememberColorCardStackState(parentSize)
            ColorCardStack(
                modifier = Modifier
                    .weight(1f)
                    .onGloballyPositioned {
                        with(density) {
                            parentSize = DpSize(
                                width = it.size.width.toDp(),
                                height = it.size.height.toDp()
                            )
                        }
                    },
                state = colorCardStackState,
            )

            ColorPicker(
                currentColor = colorCardStackState.currentColor,
                onColorChange = { colorCardStackState.set(it) }
            )
        }
    }
}