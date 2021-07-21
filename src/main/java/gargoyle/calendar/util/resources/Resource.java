package gargoyle.calendar.util.resources;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public interface Resource {
    boolean exists();


    InputStream getInputStream();


    OutputStream getOutputStream();


    URL getUrl();
}
