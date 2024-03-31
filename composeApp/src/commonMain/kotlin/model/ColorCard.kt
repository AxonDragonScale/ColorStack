import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

@OptIn(ExperimentalStdlibApi::class)
data class ColorCard private constructor(
    val id: Int,
    val color: Color,
    val isPopped: Boolean,
) {
    companion object {
        private var idCounter = 0
        private fun newId(): Int = idCounter++

        fun create(color: Color) = ColorCard(
            id = newId(),
            color = color,
            isPopped = false,
        )
    }

    val argb = color.toArgb()
    val hexString = argb.toHexString(HexFormat.UpperCase).drop(2)
}