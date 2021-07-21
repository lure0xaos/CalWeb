package gargoyle.calendar.util.reflect;

public class ReflectionException extends RuntimeException {
    private static final long serialVersionUID = -8128565529261317236L;

    public ReflectionException(String message) {
        super(message);
    }

    public ReflectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
