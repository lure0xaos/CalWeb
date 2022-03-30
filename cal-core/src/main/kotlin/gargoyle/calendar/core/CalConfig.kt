package gargoyle.calendar.core

import gargoyle.calendar.util.resources.Resource
import java.awt.Color
import java.awt.Font

class CalConfig {
    lateinit var backgroundColor: Color
    var canvasHeight: Int = 0
    var canvasWidth: Int = 0
    lateinit var daysBackColor: Color
    lateinit var daysFont: Font
    lateinit var daysForeColor: Color
    lateinit var daysRedColor: Color
    lateinit var imageLocation: Resource
    lateinit var monthBackColor: Color
    lateinit var monthFont: Font
    lateinit var monthForeColor: Color
    lateinit var weekdaysBackColor: Color
    lateinit var weekdaysFont: Font
    lateinit var weekdaysForeColor: Color
    lateinit var weekdaysRedColor: Color
    lateinit var yearBackColor: Color
    lateinit var yearFont: Font
    lateinit var yearForeColor: Color
    lateinit var yearText: String

    override fun toString(): String =
        """CalConfig{
        |backgroundColor=$backgroundColor,
        |canvasHeight=$canvasHeight,
        |canvasWidth=$canvasWidth,
        |daysBackColor=$daysBackColor,
        |daysFont=$daysFont,
        |daysForeColor=$daysForeColor,
        |daysRedColor=$daysRedColor,
        |imageLocation=$imageLocation,
        |monthBackColor=$monthBackColor,
        |monthFont=$monthFont,
        |monthForeColor=$monthBackColor,
        |weekdaysBackColor=$weekdaysBackColor,
        |weekdaysFont=$weekdaysFont,
        |weekdaysForeColor=$weekdaysForeColor,
        |weekdaysRedColor=$weekdaysRedColor,
        |yearBackColor=$yearBackColor,
        |yearFont=$yearFont,
        |yearForeColor=$yearForeColor,
        |yearText='$yearText'}""".trimMargin()

}
