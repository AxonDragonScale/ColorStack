package util

import model.HSL
import model.HSV
import model.RGB
import kotlin.math.abs

/**
 * Converts an ARGB Int to RGB
 */
fun Int.toRGB() = RGB(
    red = (this shr 16) and 0xFF,
    green = (this shr 8) and 0xFF,
    blue = this and 0xFF
)

/**
 * Converts an ARGB Int to HSL
 */
fun Int.toHSL(): HSL = run {
    val red = ((this shr 16) and 0xFF) / 255f
    val green = ((this shr 8) and 0xFF) / 255f
    val blue = (this and 0xFF) / 255f

    val max = maxOf(red, green, blue)
    val min = minOf(red, green, blue)
    val deltaMaxMin = max - min

    var hue: Float = 0f
    var saturation: Float = 0f
    val lightness: Float = (max + min) / 2f

    if (deltaMaxMin != 0f) {
        hue = when (max) {
            red -> ((green - blue) / deltaMaxMin) % 6f
            green -> ((blue - red) / deltaMaxMin) + 2f
            else -> ((red - green) / deltaMaxMin) + 4f
        }
        saturation = deltaMaxMin / (1f - abs((2f * lightness) - 1f))
    }

    hue = (hue * 60f) % 360f
    if (hue < 0) hue += 360f

    HSL(
        hue = hue.coerceIn(0f, 360f),
        saturation = saturation.coerceIn(0f, 1f),
        lightness = lightness.coerceIn(0f, 1f),
    )
}

/**
 * Converts an ARGB Int to HSL
 */
fun Int.toHSV(): HSV = run {
    val red = ((this shr 16) and 0xFF) / 255f
    val green = ((this shr 8) and 0xFF) / 255f
    val blue = (this and 0xFF) / 255f

    val max = maxOf(red, green, blue)
    val min = minOf(red, green, blue)
    val deltaMaxMin = max - min

    var hue: Float = 0f
    val saturation: Float = if (max == 0f) 0f else (max - min) / max
    val value: Float = max

    if (deltaMaxMin != 0f) {
        hue = when (max) {
            red -> ((green - blue) / deltaMaxMin)
            green -> ((blue - red) / deltaMaxMin) + 2f
            else -> ((red - green) / deltaMaxMin) + 4f
        }
        hue = (hue * 60f) % 360f
        if (hue < 0) hue += 360f
    }

    HSV(
        hue = hue.coerceIn(0f, 360f),
        saturation = saturation.coerceIn(0f, 1f),
        value = value.coerceIn(0f, 1f),
    )
}