@file:Suppress("DEPRECATION")

package gargoyle.calendar.util.load

import gargoyle.calendar.util.resources.Resource
import java.applet.Applet
import java.applet.AudioClip
import java.awt.Font
import java.awt.FontFormatException
import java.awt.Image
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

object DefaultLoaders : Loaders() {
    init {
        addLoader(Font::class) { loadFont(it) }
        addLoader(BufferedImage::class) { loadImage(it) }
        addLoader(Image::class) { loadImage(it) }
        addLoader(AudioClip::class) { loadAudioClip(it) }
    }

    private fun loadAudioClip(resource: Resource): AudioClip =
        Applet.newAudioClip(resource.url)

    private fun loadFont(resource: Resource): Font =
        try {
            resource.inputStream.use { stream -> Font.createFont(Font.TRUETYPE_FONT, stream) }
        } catch (e: FontFormatException) {
            error("cannot load font from $resource")
        }

    private fun loadImage(resource: Resource): BufferedImage =
        resource.inputStream.use { stream -> requireNotNull(ImageIO.read(stream)) { "cannot read image from $resource" } }
}
