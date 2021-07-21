package gargoyle.calendar.util;

import gargoyle.calendar.util.asserts.Assertions;
import gargoyle.calendar.util.beans.BeanModel;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class Args {
    private static final String EQ = "=";

    private Args() {
    }


    public static <C> C parseArgs(Class<C> configClass, String[] args) {
        BeanModel<C> beanModel = BeanModel.ofType(configClass);
        return beanModel.load(parseArgs(args));
    }

    public static Map<String, String> parseArgs(String[] args) {
        return Arrays.stream(args).map(arg -> arg.split(EQ, 2))
                .filter((pair) -> pair.length > 1)
                .collect(Collectors.toMap(pair -> {
                            String key = pair[0];
                            return key.trim();
                        }, split -> {
                            String value = split[1];
                            return value.trim();
                        },
                        (key, val) -> val,
                        () -> new LinkedHashMap<>(args.length)));
    }


    public static Map<String, String> parseArgs(String[] keys, String[] args) {
        Assertions.assertTrue(keys.length >= args.length, "no matched args name");
        return IntStream.range(0, args.length).boxed().
                collect(Collectors.toMap(
                        i -> keys[i],
                        i -> args[i],
                        (key, val) -> val,
                        () -> new LinkedHashMap<>(keys.length)));
    }
}
