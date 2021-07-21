package gargoyle.calendar.util.resources;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.text.MessageFormat;


public class FileSystemResource extends ResourceBase {
    public FileSystemResource(Path path) {
        super(getUrl(path));
    }


    private static URL getUrl(Path path) {
        try {
            URI uri = path.toAbsolutePath().toUri();
            return uri.toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeIOException(MessageFormat.format("cannot get url from path {0}", path), e);
        }
    }
}
