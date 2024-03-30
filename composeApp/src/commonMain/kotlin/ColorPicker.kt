import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.roundToInt

@Composable
fun ColorPicker(
    modifier: Modifier = Modifier,
    currentColor: Color,
    onColorChange: (Color) -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        val rgb = remember(currentColor) {
            val argb = currentColor.toArgb()
            Triple(
                first = (argb and 0xFF0000) shr 16,
                second = (argb and 0xFF00) shr 8,
                third = argb and 0xFF
            )
        }
        ColorSlider(
            value = rgb.first.toFloat(),
            onValueChange = { onColorChange(Color(it.roundToInt(), rgb.second, rgb.third)) },
            valueRange = 0f.rangeTo(255f),
            step = 1f,
        )
        
        ColorSlider(
            value = rgb.second.toFloat(),
            onValueChange = { onColorChange(Color(rgb.first, it.roundToInt(), rgb.third)) },
            valueRange = 0f.rangeTo(255f),
            step = 1f,
        )

        ColorSlider(
            value = rgb.third.toFloat(),
            onValueChange = { onColorChange(Color(rgb.first, rgb.second, it.roundToInt())) },
            valueRange = 0f.rangeTo(255f),
            step = 1f,
        )
    }
}

@Preview
@Composable
fun ColorPickerPreview() {
    ColorPicker(
        modifier = Modifier,
        currentColor = Color.Magenta,
        onColorChange = {},
    )
}