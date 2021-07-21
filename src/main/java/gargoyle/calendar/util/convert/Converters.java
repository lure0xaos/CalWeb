package gargoyle.calendar.util.convert;

import gargoyle.calendar.util.Tuple;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;


public class Converters {

    private final Map<Tuple<Class, Class>, Function> converters;

    protected Converters() {
        converters = new LinkedHashMap<>();
    }

    public Converters(Map<Tuple<Class, Class>, Function> converters) {
        this.converters = new LinkedHashMap<>(converters);
    }

    protected final <S, T> Converters declareConverter(Class<S> sourceType, Class<T> targetType, Function<S, T> converter) {
        if (canConvert(sourceType, targetType)) {
            throw new IllegalArgumentException(MessageFormat.format("converter {0}->{1} already exists",
                    sourceType, targetType));
        }
        converters.put(new Tuple<>(sourceType, targetType), converter);
        return this;
    }

    public final <S, T> boolean canConvert(Class<S> sourceType, Class<T> targetType) {
        return converters.containsKey(new Tuple<Class, Class>(sourceType, targetType));
    }

    public final <S, T, V extends S> T convert(Class<T> targetType, V value) {
        if (value == null) return null;
        else {
            Class<?> valueClass = value.getClass();
            return convert((Class<S>) valueClass, targetType, value);
        }
    }

    public final <S, T, V extends S> T convert(Class<S> sourceType, Class<T> targetType, V value) {
        return value == null ? null : (sourceType == targetType ? (T) value :
                (T) Optional.ofNullable(converters.get(new Tuple<Class, Class>(sourceType, targetType)))
                        .orElseThrow(() -> new UnsupportedOperationException(MessageFormat.format(
                                "converter {0}->{1} is not supported yet", sourceType, targetType)))
                        .apply(value));
    }
}
