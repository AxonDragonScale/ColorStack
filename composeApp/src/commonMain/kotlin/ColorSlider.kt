import androidx.compose.animation.core.updateTransition
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
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    step: Float,
) {
    Row(
        modifier = modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Slider(
            modifier = Modifier.weight(1f),
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            colors = SliderDefaults.colors(
                activeTickColor = Color.Transparent,
                inactiveTickColor = Color.Transparent,
            ),
        )

        Spacer(modifier = Modifier.width(12.dp))

        Row(
            modifier = Modifier
                .border(
                    width = 2.dp,
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
                modifier = Modifier.defaultMinSize(minWidth = 40.dp),
                text = if (step < 1) value.toString().take(4) else value.roundToInt().toString(),
                fontSize = 16.sp,
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
            .padding(8.dp)
            .size(20.dp),
        imageVector = imageVector,
        contentDescription = null
    )
}

@Preview
@Composable
fun ColorSliderPreview() {
    ColorSlider(
        modifier = Modifier,
        value = 123f,
        onValueChange = {},
        valueRange = 0f.rangeTo(255f),
        step = 1f,
    )
}
