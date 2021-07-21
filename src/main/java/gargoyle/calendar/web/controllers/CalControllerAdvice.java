package gargoyle.calendar.web.controllers;

import gargoyle.calendar.core.CalUtil;
import gargoyle.calendar.web.services.CalConfigSessionHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.Year;

@ControllerAdvice
@RequiredArgsConstructor

public class CalControllerAdvice {
    private static final String HOLDER = "holder";
    private static final String CURRENT_YEAR = "currentYear";
    private final CalConfigSessionHolder holder;

    @ModelAttribute(HOLDER)
    public CalConfigSessionHolder getHolder() {
        return holder;
    }

    @ModelAttribute(CURRENT_YEAR)
    public Year currentYear() {
        return CalUtil.getCurrentYear();
    }
}
