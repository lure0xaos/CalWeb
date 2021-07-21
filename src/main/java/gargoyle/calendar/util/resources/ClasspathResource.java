package gargoyle.calendar.util.resources;

import java.net.URL;

public class ClasspathResource extends ResourceBase {
    public ClasspathResource(String location) {
        this(location, ClasspathResource.class.getClassLoader());
    }

    public ClasspathResource(String location, ClassLoader classLoader) {
        super(Resources.getUrl(location, classLoader));
    }

    public ClasspathResource(String location, Class<?> aClass) {
        super(Resources.getUrl(location, aClass));
    }

    public ClasspathResource(URL location) {
        super(location);
    }


}
