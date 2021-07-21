package gargoyle.calendar.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Maps {
    private Maps() {
        throw new IllegalStateException();
    }

    public static <K, V> Map<K, V> of(Object... data) {
        Map<K, V> map = new HashMap<>();
        for (int i = 0, dataLength = data.length; i < dataLength; i += 2) {
            map.put((K) data[i], (V) data[i + 1]);
        }
        return Collections.unmodifiableMap(map);
    }
}
