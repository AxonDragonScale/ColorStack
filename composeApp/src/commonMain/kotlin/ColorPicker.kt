import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.roundToInt

@Composable
fun ColorPicker(
    modifier: Modifier = Modifier,
    currentColor: Color,
    onColorChange: (Color) -> Unit,
    onPop: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(modifier = Modifier.padding(bottom = 8.dp)) {
            Spacer(modifier = Modifier.weight(1f))

            Box(modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colors.onBackground,
                        shape = RoundedCornerShape(25)
                    )
                    .clip(RoundedCornerShape(25))
                    .clickable(onClick = onPop)
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                Text(
                    text = "POP",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

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
        onPop = {}
    )
}