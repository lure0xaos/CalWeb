package gargoyle.calendar.core;

import gargoyle.calendar.util.resources.Resource;

import java.awt.*;

public final class CalConfig {
    private static final Color BACKGROUND_COLOR = null;
    private static final Color DAYS_BACK_COLOR = null;
    private static final Font DAYS_FONT = null;
    private static final Color DAYS_FORE_COLOR = null;
    private static final Color DAYS_RED_COLOR = null;
    private static final Resource IMAGE_LOCATION = null;
    private static final Color MONTH_BACK_COLOR = null;
    private static final Font MONTH_FONT = null;
    private static final Color MONTH_FORE_COLOR = null;
    private static final Color WEEKDAYS_BACK_COLOR = null;
    private static final Font WEEKDAYS_FONT = null;
    private static final Color WEEKDAYS_FORE_COLOR = null;
    private static final Color WEEKDAYS_RED_COLOR = null;
    private static final Color YEAR_BACK_COLOR = null;
    private static final Font YEAR_FONT = null;
    private static final Color YEAR_FORE_COLOR = null;
    private static final String YEAR_TEXT = null;

    private Color backgroundColor;
    private int canvasHeight;
    private int canvasWidth;
    private Color daysBackColor;
    private Font daysFont;
    private Color daysForeColor;
    private Color daysRedColor;
    private Resource imageLocation;
    private Color monthBackColor;
    private Font monthFont;
    private Color monthForeColor;
    private Color weekdaysBackColor;
    private Font weekdaysFont;
    private Color weekdaysForeColor;
    private Color weekdaysRedColor;
    private Color yearBackColor;
    private Font yearFont;
    private Color yearForeColor;
    private String yearText;

    public CalConfig() {
        backgroundColor = BACKGROUND_COLOR;
        canvasHeight = 0;
        canvasWidth = 0;
        daysBackColor = DAYS_BACK_COLOR;
        daysFont = DAYS_FONT;
        daysForeColor = DAYS_FORE_COLOR;
        daysRedColor = DAYS_RED_COLOR;
        imageLocation = IMAGE_LOCATION;
        monthBackColor = MONTH_BACK_COLOR;
        monthFont = MONTH_FONT;
        monthForeColor = MONTH_FORE_COLOR;
        weekdaysBackColor = WEEKDAYS_BACK_COLOR;
        weekdaysFont = WEEKDAYS_FONT;
        weekdaysForeColor = WEEKDAYS_FORE_COLOR;
        weekdaysRedColor = WEEKDAYS_RED_COLOR;
        yearBackColor = YEAR_BACK_COLOR;
        yearFont = YEAR_FONT;
        yearForeColor = YEAR_FORE_COLOR;
        yearText = YEAR_TEXT;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getCanvasHeight() {
        return canvasHeight;
    }

    public void setCanvasHeight(int canvasHeight) {
        this.canvasHeight = canvasHeight;
    }

    public int getCanvasWidth() {
        return canvasWidth;
    }

    public void setCanvasWidth(int canvasWidth) {
        this.canvasWidth = canvasWidth;
    }

    public Color getDaysBackColor() {
        return daysBackColor;
    }

    public void setDaysBackColor(Color daysBackColor) {
        this.daysBackColor = daysBackColor;
    }

    public Font getDaysFont() {
        return daysFont;
    }

    public void setDaysFont(Font daysFont) {
        this.daysFont = daysFont;
    }

    public Color getDaysForeColor() {
        return daysForeColor;
    }

    public void setDaysForeColor(Color daysForeColor) {
        this.daysForeColor = daysForeColor;
    }

    public Color getDaysRedColor() {
        return daysRedColor;
    }

    public void setDaysRedColor(Color daysRedColor) {
        this.daysRedColor = daysRedColor;
    }

    public Resource getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(Resource imageLocation) {
        this.imageLocation = imageLocation;
    }

    public Color getMonthBackColor() {
        return monthBackColor;
    }

    public void setMonthBackColor(Color monthBackColor) {
        this.monthBackColor = monthBackColor;
    }

    public Font getMonthFont() {
        return monthFont;
    }

    public void setMonthFont(Font monthFont) {
        this.monthFont = monthFont;
    }

    public Color getMonthForeColor() {
        return monthForeColor;
    }

    public void setMonthForeColor(Color monthForeColor) {
        this.monthForeColor = monthForeColor;
    }

    public Color getWeekdaysBackColor() {
        return weekdaysBackColor;
    }

    public void setWeekdaysBackColor(Color weekdaysBackColor) {
        this.weekdaysBackColor = weekdaysBackColor;
    }

    public Font getWeekdaysFont() {
        return weekdaysFont;
    }

    public void setWeekdaysFont(Font weekdaysFont) {
        this.weekdaysFont = weekdaysFont;
    }

    public Color getWeekdaysForeColor() {
        return weekdaysForeColor;
    }

    public void setWeekdaysForeColor(Color weekdaysForeColor) {
        this.weekdaysForeColor = weekdaysForeColor;
    }

    public Color getWeekdaysRedColor() {
        return weekdaysRedColor;
    }

    public void setWeekdaysRedColor(Color weekdaysRedColor) {
        this.weekdaysRedColor = weekdaysRedColor;
    }

    public Color getYearBackColor() {
        return yearBackColor;
    }

    public void setYearBackColor(Color yearBackColor) {
        this.yearBackColor = yearBackColor;
    }

    public Font getYearFont() {
        return yearFont;
    }

    public void setYearFont(Font yearFont) {
        this.yearFont = yearFont;
    }

    public Color getYearForeColor() {
        return yearForeColor;
    }

    public void setYearForeColor(Color yearForeColor) {
        this.yearForeColor = yearForeColor;
    }

    public String getYearText() {
        return yearText;
    }

    public void setYearText(String yearText) {
        this.yearText = yearText;
    }

    @Override
    public String toString() {
        return String.format("CalConfig{backgroundColor=%s, canvasHeight=%d, canvasWidth=%d, daysBackColor=%s, daysFont=%s, daysForeColor=%s, daysRedColor=%s, imageLocation=%s, monthBackColor=%s, monthFont=%s, monthForeColor=%s, weekdaysBackColor=%s, weekdaysFont=%s, weekdaysForeColor=%s, weekdaysRedColor=%s, yearBackColor=%s, yearFont=%s, yearForeColor=%s, yearText='%s'}", backgroundColor, canvasHeight, canvasWidth, daysBackColor, daysFont, daysForeColor, daysRedColor, imageLocation, monthBackColor, monthFont, monthForeColor, weekdaysBackColor, weekdaysFont, weekdaysForeColor, weekdaysRedColor, yearBackColor, yearFont, yearForeColor, yearText);
    }
}
