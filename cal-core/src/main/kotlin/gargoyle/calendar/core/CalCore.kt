package gargoyle.calendar.core

import gargoyle.calendar.util.resources.Resource
import java.awt.Color
import java.awt.Font
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage
import java.time.DayOfWeek
import java.time.Month
import java.time.Year
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.ChronoField
import java.time.temporal.TemporalAdjusters
import java.time.temporal.TemporalField
import java.util.Locale
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

class CalCore(private val config: CalConfig) {
    fun createWrite(write: Resource, year: Year, locale: Locale, formatName: String) {
        val image = createImage(year, locale)
        CalUtil.writeImage(image, write, formatName)
    }

    private fun createImage(year: Year, locale: Locale): BufferedImage {
        val canvasWidth = config.canvasWidth
        val canvasHeight = config.canvasHeight
        val canvas = CalUtil.createImage(canvasWidth, canvasHeight)
        printImage(canvas)
        printYearTitle(canvas, year)
        printYear(canvas, canvasHeight > canvasWidth, year, locale)
        return canvas
    }

    private fun printImage(canvas: BufferedImage) {
        val canvasWidth = config.canvasWidth
        val canvasHeight = config.canvasHeight
        val original = CalUtil.readImage(config.imageLocation)
        val originalWidth = original.width
        val originalHeight = original.height
        val inner: Rectangle2D = CalUtil.createRectangle(0.0, 0.0, originalWidth.toDouble(), originalHeight.toDouble())
        val area =
            CalUtil.fit(CalUtil.createRectangle(0.0, 0.0, canvasWidth.toDouble(), canvasHeight.toDouble()), inner)
        CalUtil.fill(canvas, config.backgroundColor, area)
        val areaWidth = area.width
        val areaHeight = area.height
        CalUtil.drawImage(canvas, area, CalUtil.scale(original, areaWidth.toInt(), areaHeight.toInt()))
    }

    private fun printYearTitle(canvas: BufferedImage, year: Year) {
        val lineHeight = CalUtil.lineHeight(canvas, config.daysFont)
        val canvasWidth = canvas.width
        val area: Rectangle2D = CalUtil.createRectangle(0.0, 0.0, canvasWidth.toDouble(), lineHeight.toDouble())
        val text = interpolate(config.yearText, mapOf(PARAM_YEAR to year))
        write(canvas, area, text, config.yearFont, config.yearForeColor, config.yearBackColor, true)
    }

    private fun interpolate(text: String, params: Map<String, Any>): String {
        var ret = text
        params.forEach { (key, value) ->
            ret = ret.replace("{$key}", value.toString())
        }
        return ret
    }

    private fun printYear(canvas: BufferedImage, portrait: Boolean, year: Year, locale: Locale) {
        val yearLength = getLength(ChronoField.MONTH_OF_YEAR)
        val sqrt = sqrt(yearLength.toDouble())
        val floor = floor(sqrt).toInt()
        val ceil = ceil(sqrt).toInt()
        val monthsPerRow = if (portrait) floor else ceil
        val monthsPerCol = if (portrait) ceil else floor
        val monthWidth = canvas.width.toDouble() / monthsPerRow
        val yearLineHeight = CalUtil.lineHeight(canvas, config.yearFont).toDouble()
        val monthHeight = (canvas.height - yearLineHeight) / monthsPerCol
        val dayHeight = CalUtil.lineHeight(canvas, config.daysFont).toDouble()
        for (month in Month.values()) {
            val monthIndex = month.ordinal
            val col = monthIndex % monthsPerRow
            val row = monthIndex / monthsPerRow
            val x = col * monthWidth
            val y = yearLineHeight + row * monthHeight
            val area: Rectangle2D = CalUtil.createRectangle(x, y, monthWidth, monthHeight)
            val yearMonth = year.atMonth(month)
            printMonth(canvas, dayHeight, yearMonth, area, locale)
        }
    }

    private fun printMonth(
        canvas: BufferedImage,
        dayHeight: Double,
        yearMonth: YearMonth,
        monthArea: Rectangle2D,
        locale: Locale
    ) {
        val monthHeight = CalUtil.lineHeight(canvas, config.monthFont)
        val monthAreaX = monthArea.x
        val monthAreaY = monthArea.y
        val monthAreaWidth = monthArea.width
        val area: Rectangle2D = CalUtil.createRectangle(monthAreaX, monthAreaY, monthAreaWidth, monthHeight.toDouble())
        val month = yearMonth.month
        printMonthTitle(canvas, monthArea, month, locale)
        val dayWidth = area.width / getLength(ChronoField.DAY_OF_WEEK)
        val areaX = area.x
        val weekdaysY = area.y + monthHeight
        printWeekdays(canvas, areaX, weekdaysY, dayWidth, dayHeight, locale)
        val daysY = area.y + monthHeight + dayHeight
        printBackDays(canvas, areaX, daysY, dayWidth, dayHeight)
        printDays(canvas, yearMonth, areaX, daysY, dayWidth, dayHeight)
    }

    private fun printMonthTitle(canvas: BufferedImage, monthArea: Rectangle2D, month: Month, locale: Locale) {
        val monthName = month.getDisplayName(TextStyle.FULL_STANDALONE, locale)
        val lineHeight = CalUtil.lineHeight(canvas, config.daysFont)
        val monthAreaX = monthArea.x
        val monthAreaY = monthArea.y
        val monthAreaWidth = monthArea.width
        val area: Rectangle2D = CalUtil.createRectangle(monthAreaX, monthAreaY, monthAreaWidth, lineHeight.toDouble())
        write(canvas, area, monthName, config.monthFont, config.monthForeColor, config.monthBackColor, true)
    }

    private fun printWeekdays(
        canvas: BufferedImage,
        areaX: Double,
        areaY: Double,
        dayWidth: Double,
        dayHeight: Double,
        locale: Locale
    ) {
        val backColor = config.weekdaysBackColor
        for (dayOfWeek in DayOfWeek.values()) {
            val day = dayOfWeek.ordinal
            val x = areaX + day * dayWidth
            val dayArea: Rectangle2D = CalUtil.createRectangle(x, areaY, dayWidth, dayHeight)
            val foreColor = if (isWeekend(dayOfWeek)) config.weekdaysRedColor else config.weekdaysForeColor
            val weekdayName = dayOfWeek.getDisplayName(TextStyle.SHORT, locale)
            write(canvas, dayArea, weekdayName, config.weekdaysFont, foreColor, backColor, true)
        }
    }

    private fun printBackDays(
        canvas: BufferedImage,
        areaX: Double,
        areaY: Double,
        dayWidth: Double,
        dayHeight: Double
    ) {
        val backColor = config.daysBackColor
        val weekLength = getLength(ChronoField.DAY_OF_WEEK)
        val l = getLength(ChronoField.ALIGNED_WEEK_OF_MONTH) / weekLength
        for (row in 0 until l) {
            for (col in 0 until weekLength) {
                val x = areaX + col * dayWidth
                val y = areaY + row * dayHeight
                val dayArea = CalUtil.createRectangle(x, y, dayWidth, dayHeight)
                write(canvas, dayArea, null, null, null, backColor, false)
            }
        }
    }

    private fun printDays(
        canvas: BufferedImage,
        yearMonth: YearMonth,
        areaX: Double,
        areaY: Double,
        dayWidth: Double,
        dayHeight: Double
    ) {
        val firstDay = yearMonth.atEndOfMonth().with(TemporalAdjusters.firstDayOfMonth())
        val weekdayIndex = firstDay.dayOfWeek
        val range = firstDay.range(ChronoField.DAY_OF_MONTH)
        val weekLength = getLength(ChronoField.DAY_OF_WEEK).toInt()
        val maximum = range.maximum.toInt()
        val minimum = range.minimum.toInt()
        for (day in minimum..maximum) {
            val idx = (weekdayIndex.ordinal + day - 1).toLong()
            val col = idx % weekLength
            val row = idx / weekLength
            val x = areaX + col * dayWidth
            val y = areaY + row * dayHeight
            val color = if (isWeekend(yearMonth.atDay(day).dayOfWeek)) config.daysRedColor else config.daysForeColor
            val dayArea: Rectangle2D = CalUtil.createRectangle(x, y, dayWidth, dayHeight)
            write(canvas, dayArea, day.toString(), config.daysFont, color, null, true)
        }
    }

    override fun toString(): String {
        return String.format("CalCore{config=%s}", config)
    }

    companion object {
        private const val PARAM_YEAR = "year"
        private fun write(
            canvas: BufferedImage,
            area: Rectangle2D,
            text: String?,
            font: Font?,
            foreColor: Color?,
            backColor: Color?,
            fill: Boolean
        ) {
            if (backColor != null) {
                val fillArea = if (fill || text == null) area else CalUtil.metricText(
                    canvas,
                    requireNotNull(font),
                    area,
                    text
                )
                CalUtil.fill(canvas, backColor, fillArea)
            }
            if (text != null) {
                CalUtil.drawText(canvas, text, requireNotNull(foreColor), requireNotNull(font), area)
            }
        }

        private fun getLength(field: TemporalField): Long {
            val range = field.range()
            return range.maximum - range.minimum + 1
        }

        private fun isWeekend(day: DayOfWeek): Boolean {
            return day == DayOfWeek.SUNDAY
        }
    }
}
