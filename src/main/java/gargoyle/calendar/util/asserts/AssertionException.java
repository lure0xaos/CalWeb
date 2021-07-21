package gargoyle.calendar.util.asserts;

public class AssertionException extends RuntimeException {
    private static final long serialVersionUID = -3556158149303554652L;

    public AssertionException(String message) {
        super(message);
    }

    public AssertionException(String message, Throwable cause) {
        super(message, cause);
    }
}
