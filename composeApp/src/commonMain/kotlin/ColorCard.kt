import androidx.compose.ui.graphics.Color

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
}