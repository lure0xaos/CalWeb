package gargoyle.calendar.util.beans;

import gargoyle.calendar.util.convert.DefaultConverters;
import gargoyle.calendar.util.load.MapLoader;
import gargoyle.calendar.util.reflect.ReflectionException;
import gargoyle.calendar.util.reflect.Reflections;
import gargoyle.calendar.util.resources.Resource;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class BeanModel<B> {
    private static final String PROP_CLASS = "class";
    private final BeanInfo beanInfo;
    private final Class<B> type;

    private BeanModel(Class<B> type) {
        this.type = type;
        try {
            beanInfo = Introspector.getBeanInfo(type);
        } catch (IntrospectionException e) {
            String beanClassName = type.getName();
            throw new ReflectionException(MessageFormat.format("introspection failed of class {0}", beanClassName), e);
        }
    }

    public static <B> BeanModel<B> ofType(Class<B> type) {
        return new BeanModel<>(type);
    }


    public static <B> B load(Class<B> type, Resource resource) {
        B bean = Reflections.newInstance(type);
        BeanModel<B> beanModel = ofType(type);
        beanModel.fill(bean, MapLoader.loadMap(resource));
        return bean;
    }


    public static <B> B load(Class<B> type, Map<String, String> map) {
        B bean = Reflections.newInstance(type);
        BeanModel<B> beanModel = ofType(type);
        beanModel.fill(bean, map);
        return bean;
    }


    public B load(Resource resource) {
        B bean = Reflections.newInstance(type);
        fill(bean, MapLoader.loadMap(resource));
        return bean;
    }


    public B load(Map<String, String> map) {
        B bean = Reflections.newInstance(type);
        fill(bean, map);
        return bean;
    }

    public void fill(B bean, Resource resource) {
        fill(bean, MapLoader.loadMap(resource));
    }

    public void fill(B bean, Map<String, String> map) {
        forEachProperty(property -> readWrite(map, bean, property));
    }

    private void readWrite(Map<String, String> map, B bean, PropertyDescriptor property) {
        Class<?> propertyType = property.getPropertyType();
        String propertyName = property.getName();
        String value = map.get(propertyName);
        Object converted = DefaultConverters.INSTANCE.convert(String.class, propertyType, value);
        writeProperty(bean, property, converted);
    }

    private void forEachProperty(Consumer<PropertyDescriptor> action) {
        PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();
        Arrays.stream(properties)
                .filter(property -> {
                    String propertyName = property.getName();
                    return !Objects.equals(PROP_CLASS, propertyName);
                })
                .forEach(action);
    }


    public Optional<PropertyDescriptor> findProperty(String name) {
        return findProperty(property -> {
            String propertyName = property.getName();
            return Objects.equals(propertyName, name);
        });
    }

    public Optional<PropertyDescriptor> findProperty(Predicate<PropertyDescriptor> predicate) {
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        return Arrays.stream(propertyDescriptors).filter(predicate).findFirst();
    }

    public <T> T readProperty(B bean, PropertyDescriptor property) {
        String propertyName = property.getName();
        Method method = property.getReadMethod();
        if (method == null) {
            throw new ReflectionException(MessageFormat.format("no read property {0}", propertyName));
        }
        try {
            return (T) method.invoke(bean);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ReflectionException(MessageFormat.format("cannot read property {0}", propertyName), e);
        }
    }

    public <T> void writeProperty(B bean, PropertyDescriptor property, T value) {
        Method method = property.getWriteMethod();
        if (method != null) {
            try {
                method.invoke(bean, value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                String propertyName = property.getName();
                throw new ReflectionException(MessageFormat.format("cannot write property {0}", propertyName), e);
            }
        }
    }

    @Override
    public String toString() {
        return String.format("BeanModel{type=%s}", type);
    }
}
