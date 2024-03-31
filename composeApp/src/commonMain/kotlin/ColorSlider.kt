import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.roundToInt

@Composable
fun ColorSlider(
    modifier: Modifier = Modifier,
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    step: Float,
) = Column(modifier = modifier) {
    Text(
        modifier = Modifier.offset(y = 4.dp).height(12.dp),
        text = label,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 12.sp,
    )
    Row(verticalAlignment = Alignment.CenterVertically) {
        Slider(
            modifier = Modifier.weight(1f).height(36.dp),
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            colors = SliderDefaults.colors(
                thumbColor = Color.Black,
                activeTrackColor = Color.Black,
                activeTickColor = Color.Transparent,
                inactiveTickColor = Color.Transparent,
            ),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Row(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colors.onBackground,
                    shape = RoundedCornerShape(25)
                )
                .clip(RoundedCornerShape(25)),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            SliderButton(
                imageVector = Icons.Default.Remove,
                onClick = { onValueChange((value - step).coerceIn(valueRange)) }
            )

            Text(
                modifier = Modifier.defaultMinSize(minWidth = 24.dp),
                text = if (step < 1) value.toString().take(4) else value.roundToInt().toString(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )

            SliderButton(
                imageVector = Icons.Default.Add,
                onClick = { onValueChange((value + step).coerceIn(valueRange)) }
            )

        }
    }
}

@Composable
fun SliderButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    onClick: () -> Unit,
) {
    Icon(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(4.dp)
            .size(16.dp),
        imageVector = imageVector,
        contentDescription = null
    )
}

@Preview
@Composable
fun ColorSliderPreview() {
    Column {
        ColorSlider(
            modifier = Modifier,
            label = "RED",
            value = 0f,
            onValueChange = {},
            valueRange = 0f.rangeTo(255f),
            step = 1f,
        )

        ColorSlider(
            modifier = Modifier,
            label = "BLUE",
            value = 100f,
            onValueChange = {},
            valueRange = 0f.rangeTo(255f),
            step = 1f,
        )

        ColorSlider(
            modifier = Modifier,
            label = "GREEN",
            value = 255f,
            onValueChange = {},
            valueRange = 0f.rangeTo(255f),
            step = 1f,
        )
    }
}
