package gargoyle.calendar.web.data;

import gargoyle.calendar.util.Interpolation;
import lombok.Value;

@Value
public class ExceptionInfo {
    private static final String TAIL = "Exception";

    String message;
    String details;

    public ExceptionInfo(Exception exception) {
        message = Interpolation.splitWords(Interpolation.cutTail(exception.getClass().getSimpleName(), TAIL),
                Interpolation.SPLIT_WORDS_OPTIONS.FIRST_UPPER);
        details = Interpolation.findFirstNotEmpty(new ThrowableIterable(exception), Throwable::getLocalizedMessage);
    }

}
