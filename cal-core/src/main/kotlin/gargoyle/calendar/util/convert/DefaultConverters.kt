package gargoyle.calendar.util.convert

import gargoyle.calendar.util.resources.Resource
import gargoyle.calendar.util.resources.Resources
import java.awt.Color
import java.awt.Font

object DefaultConverters : Converters() {
    init {
        declareConverter(String::class, Boolean::class) { it.toBoolean() }
        declareConverter(String::class, Int::class) { it.toInt() }
        declareConverter(String::class, Long::class) { it.toLong() }
        declareConverter(String::class, Double::class) { it.toDouble() }
        declareConverter(String::class, Resource::class) { Resources.getResource(it, Converters::class) }
        declareConverter(String::class, Color::class) { parseColor(it) }
        declareConverter(String::class, Font::class) { Font.decode(it) }
    }

    private val PATTERN_COLOR = ("#[0-9a-f]{3,8}").toRegex()
    private const val SHIFT_A: UInt = 12u
    private const val SHIFT_AA: UInt = 24u
    private const val SHIFT_B: UInt = 0u
    private const val SHIFT_BB: UInt = 0u
    private const val SHIFT_G: UInt = 4u
    private const val SHIFT_GG: UInt = 8u
    private const val SHIFT_R: UInt = 8u
    private const val SHIFT_RR: UInt = 16u
    private const val MASK_BYTE: UInt = 15u
    private const val MASK_WORD: UInt = 255u
    private const val RADIX = 16

    private fun dbl(num: UInt, shift: UInt, inv: Boolean = false): UInt =
        shift(num, shift, MASK_BYTE, inv).let { it shl 4 or it }

    private fun parseColor(color: String): Color =
        if (!PATTERN_COLOR.matches(color)) Color.decode(color)
        else (color.substring(1)).toUInt(RADIX).let {
            when (color.length - 1) {
                8 -> Color(
                    sh(it, SHIFT_RR).toInt(),
                    sh(it, SHIFT_GG).toInt(),
                    sh(it, SHIFT_BB).toInt(),
                    sh(it, SHIFT_AA, true).toInt()
                )
                6 -> Color(
                    sh(it, SHIFT_RR).toInt(),
                    sh(it, SHIFT_GG).toInt(),
                    sh(it, SHIFT_BB).toInt()
                )
                4 -> Color(
                    dbl(it, SHIFT_R).toInt(),
                    dbl(it, SHIFT_G).toInt(),
                    dbl(it, SHIFT_B).toInt(),
                    dbl(it, SHIFT_A, true).toInt()
                )
                3 -> Color(
                    dbl(it, SHIFT_R).toInt(),
                    dbl(it, SHIFT_G).toInt(),
                    dbl(it, SHIFT_B).toInt()
                )
                else -> error(color)
            }
        }

    private fun sh(num: UInt, shift: UInt, inv: Boolean = false): UInt = shift(num, shift, MASK_WORD, inv)

    private fun shift(num: UInt, shift: UInt, mask: UInt, inv: Boolean): UInt =
        (num shr shift.toInt() and mask).let { if (inv) mask - it else it }
}
