import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import kotlin.random.Random

data class ColorCard private constructor(
    val id: Int,
    val color: Color,
    val state: State,
    val rotationStart: Float, // Rotation when card is created offsceen
    val rotationInStack: Float, // Rotation when card is in the stack
//    val translationStart: IntOffset, // Offset when card is created offsceen
//    val translationInStack: IntOffset, // Offset when card is in the stack
) {
    companion object {
        private var idCounter = 0
        private fun newId(): Int = idCounter++
        
        fun create(color: Color) = ColorCard(
            id = newId(),
            color = color,
            state = State.Start,
            rotationStart = Random.nextInt(-90, 90).toFloat(),
            rotationInStack = Random.nextInt(-30, 30).toFloat(),
        )
    }
    
    enum class State {
        Start,
        InStack
    }
}