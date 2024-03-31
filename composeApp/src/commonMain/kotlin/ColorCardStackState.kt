import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

@Composable
fun rememberColorCardStackState() = remember() {
    ColorCardStackState()
}

class ColorCardStackState internal constructor() {

    var currentColor by mutableStateOf(Color.Black)
    val cardStack = mutableStateListOf<ColorCard>()

    init {
        addCurrentColor()
    }

    fun set(color: Color) {
        currentColor = color
        addCurrentColor()
    }

    fun pop() {
        val lastUnPoppedIndex = cardStack.indexOfLast { !it.isPopped }
        if (lastUnPoppedIndex in cardStack.indices) {
            cardStack[lastUnPoppedIndex] = cardStack[lastUnPoppedIndex].copy(isPopped = true)
        }
    }

    fun remove(card: ColorCard) {
        cardStack.remove(card)
    }

    private fun addCurrentColor() {
        cardStack.add(ColorCard.create(currentColor))
        if (cardStack.size > 50) cardStack.removeRange(0, 10)
    }

}