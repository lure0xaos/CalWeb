package gargoyle.calendar.util.load;

import gargoyle.calendar.util.resources.Resource;

import javax.imageio.ImageIO;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;

public final class DefaultLoaders extends Loaders {
    public static final DefaultLoaders INSTANCE = new DefaultLoaders();

    private DefaultLoaders() {
        addLoader(Font.class, DefaultLoaders::loadFont);
        addLoader(BufferedImage.class, DefaultLoaders::loadImage);
        addLoader(Image.class, DefaultLoaders::loadImage);
        addLoader(AudioClip.class, DefaultLoaders::loadAudioClip);
    }


    private static AudioClip loadAudioClip(Resource resource) {
        URL url = resource.getUrl();
        return Applet.newAudioClip(url);
    }


    private static Font loadFont(Resource resource) throws IOException {
        try (InputStream stream = resource.getInputStream()) {
            return Font.createFont(Font.TRUETYPE_FONT, stream);
        } catch (FontFormatException e) {
            throw new IOException(MessageFormat.format("cannot load font from {0}", resource), e);
        }
    }


    private static BufferedImage loadImage(Resource resource) throws IOException {
        try (InputStream stream = resource.getInputStream()) {
            return ImageIO.read(stream);
        }
    }
}
