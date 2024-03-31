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
import util.toHSL
import util.toHSV
import util.toRGB
import kotlin.math.roundToInt

@Composable
fun ColorPicker(
    modifier: Modifier = Modifier,
    currentArgb: Int,
    onColorChange: (Color) -> Unit,
    onPop: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .background(Color.White.copy(0.8f), RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        var selectedIndex by remember { mutableStateOf(0) }
        Row(modifier = Modifier.padding(bottom = 4.dp)) {
            SegmentedButton(
                modifier = Modifier,
                items = listOf("RGB", "HSL", "HSV"),
                selectedIndex = selectedIndex,
                onSelectionChange = { selectedIndex = it },
            )

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colors.onBackground,
                        shape = RoundedCornerShape(25)
                    )
                    .clip(RoundedCornerShape(25))
                    .clickable(onClick = onPop)
                    .padding(vertical = 6.dp, horizontal = 20.dp)
            ) {
                Text(
                    text = "POP",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        when (selectedIndex) {
            0 -> RGBSliders(currentArgb, onColorChange)
            1 -> HSLSliders(currentArgb, onColorChange)
            2 -> HSVSliders(currentArgb, onColorChange)
        }

    }
}

@Composable
fun SegmentedButton(
    modifier: Modifier = Modifier,
    items: List<String>,
    selectedIndex: Int,
    onSelectionChange: (Int) -> Unit,
) {
    Row(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.onBackground,
                shape = RoundedCornerShape(25)
            )
            .clip(RoundedCornerShape(25))
            .padding(4.dp),
    ) {
        items.forEachIndexed { i, item ->
            val bgColor = if (selectedIndex == i) Color.Black.copy(0.4f) else Color.Transparent
            Text(
                modifier = Modifier
                    .background(bgColor, RoundedCornerShape(25))
                    .clip(RoundedCornerShape(25))
                    .clickable { onSelectionChange(i) }
                    .padding(vertical = 2.dp, horizontal = 20.dp),
                text = item,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
            )

            if (i != items.lastIndex)
                Spacer(modifier = Modifier.width(2.dp))
        }
    }
}

@Composable
private fun RGBSliders(
    currentArgb: Int,
    onColorChange: (Color) -> Unit,
) {
    val rgb = remember(currentArgb) { currentArgb.toRGB() }
    ColorSlider(
        label = "RED",
        value = rgb.red.toFloat(),
        onValueChange = { onColorChange(Color(it.roundToInt(), rgb.green, rgb.blue)) },
        valueRange = 0f.rangeTo(255f),
        step = 1f,
    )

    ColorSlider(
        label = "GREEN",
        value = rgb.green.toFloat(),
        onValueChange = { onColorChange(Color(rgb.red, it.roundToInt(), rgb.blue)) },
        valueRange = 0f.rangeTo(255f),
        step = 1f,
    )

    ColorSlider(
        label = "BLUE",
        value = rgb.blue.toFloat(),
        onValueChange = { onColorChange(Color(rgb.red, rgb.green, it.roundToInt())) },
        valueRange = 0f.rangeTo(255f),
        step = 1f,
    )
}


@Composable
private fun HSLSliders(
    currentArgb: Int,
    onColorChange: (Color) -> Unit,
) {
    val hsl = remember(currentArgb) { currentArgb.toHSL() }
    ColorSlider(
        label = "HUE",
        value = hsl.hue,
        onValueChange = { onColorChange(Color.hsl(it, hsl.saturation, hsl.lightness)) },
        valueRange = 0f.rangeTo(360f),
        step = 1f,
    )

    ColorSlider(
        label = "SATURATION",
        value = hsl.saturation * 100f,
        onValueChange = { onColorChange(Color.hsl(hsl.hue, it / 100f, hsl.lightness)) },
        valueRange = 0f.rangeTo(100f),
        step = 1f,
    )

    ColorSlider(
        label = "LIGHTNESS",
        value = hsl.lightness * 100f,
        onValueChange = { onColorChange(Color.hsl(hsl.hue, hsl.saturation, it / 100f)) },
        valueRange = 0f.rangeTo(100f),
        step = 1f,
    )
}


@Composable
private fun HSVSliders(
    currentArgb: Int,
    onColorChange: (Color) -> Unit,
) {
    val hsv = remember(currentArgb) { currentArgb.toHSV() }
    ColorSlider(
        label = "HUE",
        value = hsv.hue,
        onValueChange = { onColorChange(Color.hsv(it, hsv.saturation, hsv.value)) },
        valueRange = 0f.rangeTo(360f),
        step = 1f,
    )

    ColorSlider(
        label = "SATURATION",
        value = hsv.saturation * 100f,
        onValueChange = { onColorChange(Color.hsv(hsv.hue, it / 100f, hsv.value)) },
        valueRange = 0f.rangeTo(100f),
        step = 1f,
    )

    ColorSlider(
        label = "VALUE / BRIGHTNESS",
        value = hsv.value * 100f,
        onValueChange = { onColorChange(Color.hsv(hsv.hue, hsv.saturation, it / 100f)) },
        valueRange = 0f.rangeTo(100f),
        step = 1f,
    )
}

@Preview
@Composable
fun ColorPickerPreview() {
    Box(modifier = Modifier.background(Color.Black)) {
        ColorPicker(
            modifier = Modifier,
            currentArgb = Color.Magenta.toArgb(),
            onColorChange = {},
            onPop = {}
        )
    }
}