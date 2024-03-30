import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import kotlin.random.Random

data class ColorCard private constructor(
    val id: Int,
    val color: Color,
) {
    companion object {
        private var idCounter = 0
        private fun newId(): Int = idCounter++

        fun create(color: Color) = ColorCard(
            id = newId(),
            color = color,
        )
    }

    // Rotation when card is created offsceen
    val rotationStart = Random.nextInt(-90, 90).toFloat()

    // Rotation when card is in the stack
    val rotationInStack  = Random.nextInt(-30, 30).toFloat()


    // Offset when card is created offsceen
    private var translationStart: IntOffset? = null
    fun translationStart(parentSize: DpSize, density: Density) = with(density) {
        if (translationStart == null)
            translationStart = IntOffset(x = 0, y = (parentSize.height * -2).roundToPx())
        translationStart!!
    }

    // Offset when card is in the stack
    private var translationInStack: IntOffset? = null
    fun translationInStack(density: Density) = with(density) {
        if (translationInStack == null)
            translationInStack = IntOffset(
                x = Random.nextInt(-25, 25).dp.roundToPx(),
                y = Random.nextInt(-25, 25).dp.roundToPx(),
            )
        translationInStack!!
    }

}