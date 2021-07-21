package gargoyle.calendar.util.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;

abstract class ResourceBase implements Resource {

    private final URL url;

    protected ResourceBase(URL url) {
        this.url = url;
    }

    @Override
    public final boolean exists() {
        try (InputStream ignored = url.openStream()) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }


    @Override
    public final InputStream getInputStream() {
        try {
            URLConnection connection = url.openConnection();
            return connection.getInputStream();
        } catch (IOException e) {
            throw new RuntimeIOException(MessageFormat.format("cannot open stream to read from {0}", url), e);
        }
    }

    @Override
    public final OutputStream getOutputStream() {
        URL url = this.url;
        try {
            String protocol = url.getProtocol();
            switch (protocol) {
                case Resources.PROTOCOL_FILE:
                    return Resources.getFileStream(url);
                default:
                    return Resources.getURLStream(url);
            }
        } catch (IOException e) {
            throw new RuntimeIOException(MessageFormat.format("cannot open to write {0}", url), e);
        }
    }


    @Override
    public final URL getUrl() {
        return url;
    }

    @Override
    public final String toString() {
        Class<? extends ResourceBase> resourceClass = getClass();
        String className = resourceClass.getName();
        return String.format("%s{url=%s}", className, url);
    }
}
