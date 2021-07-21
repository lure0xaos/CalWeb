package gargoyle.calendar.util.resources;

public class RuntimeIOException extends RuntimeException {
    private static final long serialVersionUID = -8128565529261317236L;

    public RuntimeIOException(String message) {
        super(message);
    }

    public RuntimeIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
