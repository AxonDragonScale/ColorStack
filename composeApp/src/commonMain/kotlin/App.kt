import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            val colorCardStackState = rememberColorCardStackState()
            ColorCardStack(
                modifier = Modifier.weight(1f).background(Color.Gray),
                state = colorCardStackState,
            )

            ColorPicker(
                currentColor = colorCardStackState.currentColor,
                onColorChange = { colorCardStackState.set(it) }
            )
        }
    }
}