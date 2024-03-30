import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

@Composable
fun rememberColorCardStackState() = remember { ColorCardStackState() }

class ColorCardStackState internal constructor() {
    
    var currentColor by mutableStateOf(Color.Red)
    val cardStack = mutableStateListOf<ColorCard>()
    
    init {
        addCurrentColor()
    }
    
    fun set(color: Color) {
        currentColor = color
        addCurrentColor()
    }
    
    private fun addCurrentColor() {
        cardStack.add(ColorCard.create(currentColor))
        if (cardStack.size > 110) cardStack.removeRange(0, 10)
    }
    
}