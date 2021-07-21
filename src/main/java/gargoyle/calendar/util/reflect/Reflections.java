package gargoyle.calendar.util.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;

public final class Reflections {

    private Reflections() {
    }


    public static <B> B newInstance(Class<B> configClass) {
        try {
            Constructor<B> constructor = configClass.getConstructor();
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            String configClassName = configClass.getName();
            throw new ReflectionException(MessageFormat.format("cannot instantiate class {0}", configClassName), e);
        }
    }

    public static Method getMethod(Class<?> aClass, String methodName) {
        try {
            return aClass.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            throw new ReflectionException(e.getLocalizedMessage());
        }
    }
}
