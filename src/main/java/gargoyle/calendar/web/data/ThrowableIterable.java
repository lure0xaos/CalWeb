package gargoyle.calendar.web.data;

import java.util.Iterator;

final class ThrowableIterable implements Iterable<Throwable> {
    private Throwable exception;

    public ThrowableIterable(Throwable exception) {
        this.exception = exception;
    }

    @Override
    public Iterator<Throwable> iterator() {
        return new Iterator<Throwable>() {
            @Override
            public boolean hasNext() {
                return exception != null;
            }

            @Override
            public Throwable next() {
                exception = exception.getCause();
                return exception;
            }
        };
    }
}
