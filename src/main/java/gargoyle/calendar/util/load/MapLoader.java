package gargoyle.calendar.util.load;

import gargoyle.calendar.util.resources.Resource;
import gargoyle.calendar.util.resources.RuntimeIOException;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;


public final class MapLoader {

    private MapLoader() {
    }

    public static Map<String, String> loadMap(Resource resource) {
        Properties properties = new Properties();
        try (InputStream stream = resource.getInputStream()) {
            properties.load(stream);
        } catch (IOException e) {
            throw new RuntimeIOException(MessageFormat.format("cannot load configuration {0}", resource), e);
        }
        // noinspection ChainedMethodCall
        return properties.entrySet().stream().collect(Collectors.toMap(
                entry -> {
                    Object key = entry.getKey();
                    return String.valueOf(key);
                },
                entry -> {
                    Object value = entry.getValue();
                    return String.valueOf(value);
                },
                (key, val) -> val, LinkedHashMap::new));
    }
}
