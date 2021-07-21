package gargoyle.calendar.util.resources;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;

public final class Resources {

    public static final String PROTOCOL_FILE = "file";

    private Resources() {
    }


    public static Resource getResource(Path path) {
        return new FileSystemResource(path);
    }

    public static Resource getResource(String location, Class<?> aClass) {
        return new ClasspathResource(location, aClass);
    }

    public static Resource getResource(String location, ClassLoader loader) {
        return new ClasspathResource(location);
    }

    public static Resource getResource(URL url) {
        return new UrlResource(url);
    }


    public static OutputStream getURLStream(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        return connection.getOutputStream();
    }


    public static OutputStream getFileStream(URL url) throws IOException {
        String location = url.toExternalForm();
        return Files.newOutputStream(Paths.get(location.substring(PROTOCOL_FILE.length() + 2)), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    }

    public static URL getUrl(String location, ClassLoader loader) {
        URL url = loader.getResource(location);
        if (url == null) throw new RuntimeIOException(MessageFormat.format("no resource {0}", location));
        return url;
    }

    public static URL getUrl(String location, Class<?> aClass) {
        URL url = aClass.getResource(location);
        if (url == null) url = aClass.getResource('/' + location);
        if (url == null) throw new RuntimeIOException(MessageFormat.format("no resource {0}", location));
        return url;
    }
}
