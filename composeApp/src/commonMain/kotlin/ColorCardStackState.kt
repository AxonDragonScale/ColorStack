import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

@Composable
fun rememberColorCardStackState() = remember() {
    ColorCardStackState()
}

class ColorCardStackState internal constructor() {

    var currentColor by mutableStateOf(Color.Black)
    var currentArgb by mutableStateOf(0)

    val cardStack = mutableStateListOf<ColorCard>()

    init {
        set(Color.Black)
    }

    fun set(color: Color) {
        currentColor = color
        val card = ColorCard.create(currentColor)
        currentArgb = card.argb
        cardStack.add(card)
        if (cardStack.size > 50) cardStack.removeRange(0, 10)
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

}