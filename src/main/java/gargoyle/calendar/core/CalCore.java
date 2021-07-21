package gargoyle.calendar.core;

import gargoyle.calendar.util.Interpolation;
import gargoyle.calendar.util.Maps;
import gargoyle.calendar.util.resources.Resource;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.*;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.ValueRange;
import java.util.Locale;
import java.util.Objects;


public final class CalCore {

    private static final String PARAM_YEAR = "year";
    private final CalConfig config;

    public CalCore(CalConfig config) {
        this.config = config;
    }

    private static void write(BufferedImage canvas, Rectangle2D area, String text, Font font, Color foreColor, Color backColor, boolean fill) {
        if (backColor != null) {
            Rectangle2D fillArea = fill || text == null ? area : CalUtil.metricText(canvas, Objects.requireNonNull(font), area, text);
            CalUtil.fill(canvas, backColor, fillArea);
        }
        if (text != null) {
            CalUtil.drawText(canvas, text, Objects.requireNonNull(foreColor), Objects.requireNonNull(font), area);
        }
    }

    private static long getLength(TemporalField field) {
        ValueRange range = field.range();
        return range.getMaximum() - range.getMinimum() + 1;
    }

    private static boolean isWeekend(DayOfWeek day) {
        return day == DayOfWeek.SUNDAY;
    }

    public void createWrite(Resource write, Year year, Locale locale, String formatName) throws IOException {
        BufferedImage image = createImage(year, locale);
        CalUtil.writeImage(image, write, formatName);
    }


    private BufferedImage createImage(Year year, Locale locale) throws IOException {
        int canvasWidth = config.getCanvasWidth();
        int canvasHeight = config.getCanvasHeight();
        BufferedImage canvas = CalUtil.createImage(canvasWidth, canvasHeight);
        printImage(canvas);
        printYearTitle(canvas, year);
        printYear(canvas, canvasHeight > canvasWidth, year, locale);
        return canvas;
    }

    private void printImage(BufferedImage canvas) throws IOException {
        int canvasWidth = config.getCanvasWidth();
        int canvasHeight = config.getCanvasHeight();
        BufferedImage original = CalUtil.readImage(config.getImageLocation());
        int originalWidth = original.getWidth();
        int originalHeight = original.getHeight();
        Rectangle2D inner = CalUtil.createRectangle(0, 0, originalWidth, originalHeight);
        Rectangle2D area = CalUtil.fit(CalUtil.createRectangle(0, 0, canvasWidth, canvasHeight), inner);
        CalUtil.fill(canvas, config.getBackgroundColor(), area);
        double areaWidth = area.getWidth();
        double areaHeight = area.getHeight();
        CalUtil.drawImage(canvas, area, CalUtil.scale(original, (int) areaWidth, (int) areaHeight));
    }

    private void printYearTitle(BufferedImage canvas, Year year) {
        int lineHeight = CalUtil.lineHeight(canvas, config.getDaysFont());
        int canvasWidth = canvas.getWidth();
        Rectangle2D area = CalUtil.createRectangle(0, 0, canvasWidth, lineHeight);
        String text = Interpolation.interpolate(config.getYearText(), Maps.of(PARAM_YEAR, year));
        write(canvas, area, text, config.getYearFont(), config.getYearForeColor(), config.getYearBackColor(), true);
    }

    private void printYear(BufferedImage canvas, boolean portrait, Year year, Locale locale) {
        long yearLength = getLength(ChronoField.MONTH_OF_YEAR);
        double sqrt = Math.sqrt(yearLength);
        int floor = (int) Math.floor(sqrt);
        int ceil = (int) Math.ceil(sqrt);
        int monthsPerRow = portrait ? floor : ceil;
        int monthsPerCol = portrait ? ceil : floor;
        double monthWidth = (double) canvas.getWidth() / monthsPerRow;
        double yearLineHeight = CalUtil.lineHeight(canvas, config.getYearFont());
        double monthHeight = (canvas.getHeight() - yearLineHeight) / monthsPerCol;
        double dayHeight = CalUtil.lineHeight(canvas, config.getDaysFont());
        for (Month month : Month.values()) {
            int monthIndex = month.ordinal();
            int col = monthIndex % monthsPerRow;
            int row = monthIndex / monthsPerRow;
            double x = col * monthWidth;
            double y = yearLineHeight + row * monthHeight;
            Rectangle2D area = CalUtil.createRectangle(x, y, monthWidth, monthHeight);
            YearMonth yearMonth = year.atMonth(month);
            printMonth(canvas, dayHeight, yearMonth, area, locale);
        }
    }

    private void printMonth(BufferedImage canvas, double dayHeight, YearMonth yearMonth, Rectangle2D monthArea, Locale locale) {
        int monthHeight = CalUtil.lineHeight(canvas, config.getMonthFont());
        double monthAreaX = monthArea.getX();
        double monthAreaY = monthArea.getY();
        double monthAreaWidth = monthArea.getWidth();
        Rectangle2D area = CalUtil.createRectangle(monthAreaX, monthAreaY, monthAreaWidth, monthHeight);
        Month month = yearMonth.getMonth();
        printMonthTitle(canvas, monthArea, month, locale);
        double dayWidth = area.getWidth() / getLength(ChronoField.DAY_OF_WEEK);
        double areaX = area.getX();
        double weekdaysY = area.getY() + monthHeight;
        printWeekdays(canvas, areaX, weekdaysY, dayWidth, dayHeight, locale);
        double daysY = area.getY() + monthHeight + dayHeight;
        printBackDays(canvas, areaX, daysY, dayWidth, dayHeight);
        printDays(canvas, yearMonth, areaX, daysY, dayWidth, dayHeight);
    }

    private void printMonthTitle(BufferedImage canvas, Rectangle2D monthArea, Month month, Locale locale) {
        String monthName = month.getDisplayName(TextStyle.FULL_STANDALONE, locale);
        int lineHeight = CalUtil.lineHeight(canvas, config.getDaysFont());
        double monthAreaX = monthArea.getX();
        double monthAreaY = monthArea.getY();
        double monthAreaWidth = monthArea.getWidth();
        Rectangle2D area = CalUtil.createRectangle(monthAreaX, monthAreaY, monthAreaWidth, lineHeight);
        write(canvas, area, monthName, config.getMonthFont(), config.getMonthForeColor(), config.getMonthBackColor(), true);
    }

    private void printWeekdays(BufferedImage canvas, double areaX, double areaY, double dayWidth, double dayHeight, Locale locale) {
        Color backColor = config.getWeekdaysBackColor();
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            int day = dayOfWeek.ordinal();
            double x = areaX + day * dayWidth;
            Rectangle2D dayArea = CalUtil.createRectangle(x, areaY, dayWidth, dayHeight);
            Color foreColor = isWeekend(dayOfWeek) ? config.getWeekdaysRedColor() : config.getWeekdaysForeColor();
            String weekdayName = dayOfWeek.getDisplayName(TextStyle.SHORT, locale);
            write(canvas, dayArea, weekdayName, config.getWeekdaysFont(), foreColor, backColor, true);
        }
    }

    private void printBackDays(BufferedImage canvas, double areaX, double areaY, double dayWidth, double dayHeight) {
        Color backColor = config.getDaysBackColor();
        long weekLength = getLength(ChronoField.DAY_OF_WEEK);
        long l = getLength(ChronoField.ALIGNED_WEEK_OF_MONTH) / weekLength;
        for (int row = 0; row < l; row++) {
            for (int col = 0; col < weekLength; col++) {
                double x = areaX + col * dayWidth;
                double y = areaY + row * dayHeight;
                Rectangle2D.Double dayArea = CalUtil.createRectangle(x, y, dayWidth, dayHeight);
                write(canvas, dayArea, null, null, null, backColor, false);
            }
        }
    }

    private void printDays(BufferedImage canvas, YearMonth yearMonth, double areaX, double areaY, double dayWidth, double dayHeight) {
        LocalDate firstDay = yearMonth.atEndOfMonth().with(TemporalAdjusters.firstDayOfMonth());
        DayOfWeek weekdayIndex = firstDay.getDayOfWeek();
        ValueRange range = firstDay.range(ChronoField.DAY_OF_MONTH);
        int weekLength = (int) getLength(ChronoField.DAY_OF_WEEK);
        int maximum = (int) range.getMaximum();
        int minimum = (int) range.getMinimum();
        for (int day = minimum; day <= maximum; day++) {
            long idx = weekdayIndex.ordinal() + day - 1;
            long col = idx % weekLength;
            long row = idx / weekLength;
            double x = areaX + col * dayWidth;
            double y = areaY + row * dayHeight;
            Color color = isWeekend(yearMonth.atDay(day).getDayOfWeek()) ? config.getDaysRedColor() : config.getDaysForeColor();
            Rectangle2D dayArea = CalUtil.createRectangle(x, y, dayWidth, dayHeight);
            write(canvas, dayArea, String.valueOf(day), config.getDaysFont(), color, null, true);
        }
    }

    @Override
    public String toString() {
        return String.format("CalCore{config=%s}", config);
    }
}
