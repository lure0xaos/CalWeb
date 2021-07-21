package gargoyle.calendar.util.asserts;

public final class Assertions {
    private Assertions() {
    }

    public static void assertTrue(boolean condition, String message) {
        if (!condition) throw new AssertionException(message);
    }

    public static void assertNotNull(Object value, String message) {
        if (value == null) throw new AssertionException(message);
    }
}
