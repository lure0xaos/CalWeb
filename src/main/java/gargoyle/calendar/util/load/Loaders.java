package gargoyle.calendar.util.load;

import gargoyle.calendar.util.resources.Resource;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;

public class Loaders {

    private final Map<Class, Loader> loaders;

    protected Loaders() {
        loaders = new LinkedHashMap<>();
    }

    public Loaders(Map<Class, Loader> loaders) {
        this.loaders = new LinkedHashMap<>(loaders);
    }

    protected final <T> Loaders addLoader(Class<T> targetType, Loader<T> loader) {
        if (canLoad(targetType)) {
            throw new IllegalArgumentException(MessageFormat.format("loader {0} already exists", targetType));
        }
        loaders.put(targetType, loader);
        return this;
    }

    public final <T> boolean canLoad(Class<T> targetType) {
        return loaders.containsKey(targetType);
    }

    public final <T> T load(Class<T> targetType, Resource value) throws IOException {
        if (!loaders.containsKey(targetType)) throw new UnsupportedOperationException(MessageFormat.format(
                "loader {0} is not supported yet", targetType));
        Loader<T> loader = loaders.get(targetType);
        return loader.load(value);
    }

    @Override
    public final String toString() {
        return String.format("Loaders{loaders=%s}", loaders);
    }
}
