package gargoyle.calendar.util.load;

import gargoyle.calendar.util.resources.Resource;

import java.io.IOException;

@FunctionalInterface
public interface Loader<T> {

    T load(Resource resource) throws IOException;
}
