package gargoyle.calendar.core

import gargoyle.calendar.util.load.DefaultLoaders
import gargoyle.calendar.util.resources.Resource
import java.awt.Color
import java.awt.Font
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage
import java.time.Year
import javax.imageio.ImageIO

object CalUtil {
    fun createImage(width: Int, height: Int): BufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

    fun drawImage(canvas: BufferedImage, area: Rectangle2D, image: BufferedImage) {
        val areaX = area.x
        val dx2 = areaX + area.width
        val areaY = area.y
        val dy2 = areaY + area.height
        val imageWidth = image.width
        val imageHeight = image.height
        val g = canvas.graphics
        g.drawImage(image, areaX.toInt(), areaY.toInt(), dx2.toInt(), dy2.toInt(), 0, 0, imageWidth, imageHeight, null)
    }

    fun drawText(canvas: BufferedImage, text: String, color: Color, font: Font, area: Rectangle2D) {
        val g = canvas.graphics
        val savedColor = g.color
        val gFont = g.font
        val clip = g.clip
        g.color = color
        g.font = font
        g.clip = area
        val fm = g.getFontMetrics(font)
        val x = area.x + (area.width - fm.stringWidth(text)) / 2
        val lineHeight = lineHeight(canvas, font).toDouble()
        val y = area.y + (if (area.height == 0.0) lineHeight else (area.height - lineHeight) / 2) + fm.ascent
        g.drawString(text, x.toInt(), y.toInt())
        g.font = gFont
        g.color = savedColor
        g.clip = clip
    }

    fun lineHeight(canvas: BufferedImage, font: Font): Int =
        canvas.graphics.getFontMetrics(font).height

    fun fill(canvas: BufferedImage, color: Color, area: Rectangle2D) {
        val g = canvas.graphics
        val gColor = g.color
        g.color = color
        val areaX = area.x
        val areaY = area.y
        val areaWidth = area.width
        val areaHeight = area.height
        g.fillRect(areaX.toInt(), areaY.toInt(), areaWidth.toInt(), areaHeight.toInt())
        g.color = gColor
    }

    fun fit(outer: Rectangle2D, inner: Rectangle2D): Rectangle2D {
        val width: Double
        val height: Double
        if (inner.width > inner.height) {
            width = outer.width
            height = outer.height / outer.width * width
        } else {
            height = outer.height
            width = outer.width / outer.height * height
        }
        val x = outer.x + (outer.width - width) / 2
        val y = outer.y + (outer.height - height) / 2
        return createRectangle(x, y, width, height)
    }

    val currentYear: Year
        get() = Year.now()

    fun metricText(canvas: BufferedImage, font: Font, area: Rectangle2D, text: String): Rectangle2D {
        val g = canvas.graphics
        val bounds = g.getFontMetrics(font).getStringBounds(text, g)
        val width = bounds.width
        val height = bounds.height
        val x = area.x + (area.width - width) / 2
        val y = area.y + if (area.height == 0.0) height else (area.height - height) / 2
        return createRectangle(x, y, width, height)
    }

    fun readImage(image: Resource): BufferedImage = DefaultLoaders.load(BufferedImage::class, image)

    fun scale(src: BufferedImage, width: Int, height: Int): BufferedImage =
        BufferedImage(width, height, src.type)
            .also {
                it.graphics.drawImage(src, 0, 0, it.width, it.height, 0, 0, src.width, src.height, null)
            }

    fun writeImage(image: RenderedImage, resource: Resource, formatName: String) {
        resource.outputStream.use { stream -> ImageIO.write(image, formatName, stream) }
    }

    fun createRectangle(x: Double, y: Double, width: Double, height: Double): Rectangle2D.Double =
        Rectangle2D.Double(x, y, width, height)
}
