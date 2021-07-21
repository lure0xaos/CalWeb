package gargoyle.calendar.util.convert;

import gargoyle.calendar.util.resources.Resource;
import gargoyle.calendar.util.resources.Resources;

import java.awt.*;
import java.util.regex.Pattern;


public final class DefaultConverters extends Converters {
    public static final DefaultConverters INSTANCE = new DefaultConverters();
    private static final Pattern PATTERN_COLOR = Pattern.compile("#[0-9a-f]{3,8}");
    private static final int SHIFT_A = 12;
    private static final int SHIFT_AA = 24;
    private static final int SHIFT_B = 0;
    private static final int SHIFT_BB = 0;
    private static final int SHIFT_G = 4;
    private static final int SHIFT_GG = 8;
    private static final int SHIFT_R = 8;
    private static final int SHIFT_RR = 16;
    private static final int MASK_BYTE = 0xF;
    private static final int MASK_WORD = 0xFF;
    private static final int RADIX = 16;

    private DefaultConverters() {
        declareConverter(String.class, boolean.class, Boolean::valueOf);
        declareConverter(String.class, Boolean.class, Boolean::valueOf);
        declareConverter(String.class, int.class, Integer::valueOf);
        declareConverter(String.class, Integer.class, Integer::valueOf);
        declareConverter(String.class, long.class, Long::valueOf);
        declareConverter(String.class, Long.class, Long::valueOf);
        declareConverter(String.class, double.class, Double::valueOf);
        declareConverter(String.class, Double.class, Double::valueOf);
        declareConverter(String.class, Resource.class, location -> Resources.getResource(location, Converters.class));
        declareConverter(String.class, Color.class, DefaultConverters::parseColor);
        declareConverter(String.class, Font.class, Font::decode);
    }

    private static int dbl(int num, int shift) {
        return dbl(num, shift, false);
    }

    private static int dbl(int num, int shift, boolean inv) {
        int shiftF = shift(num, shift, MASK_BYTE, inv);
        return shiftF << 4 | shiftF;
    }

    private static Color parseColor(String color) {
        if (!PATTERN_COLOR.matcher(color).matches()) {
            return Color.decode(color);
        }
        int rgb = Long.valueOf(color.substring(1), RADIX).intValue();
        switch (color.length() - 1) {
            case 8:
                return new Color(sh(rgb, SHIFT_RR), sh(rgb, SHIFT_GG), sh(rgb, SHIFT_BB), sh(rgb, SHIFT_AA, true));
            case 6:
                return new Color(sh(rgb, SHIFT_RR), sh(rgb, SHIFT_GG), sh(rgb, SHIFT_BB));
            case 4:
                return new Color(dbl(rgb, SHIFT_R), dbl(rgb, SHIFT_G), dbl(rgb, SHIFT_B), dbl(rgb, SHIFT_A, true));
            case 3:
                return new Color(dbl(rgb, SHIFT_R), dbl(rgb, SHIFT_G), dbl(rgb, SHIFT_B));
            default:
                throw new IllegalArgumentException(color);
        }
    }

    private static int sh(int num, int shift) {
        return sh(num, shift, false);
    }

    private static int sh(int num, int shift, boolean inv) {
        return shift(num, shift, MASK_WORD, inv);
    }

    private static int shift(int num, int shift, int mask, boolean inv) {
        int expr = num >> shift & mask;
        return inv ? mask - expr : expr;
    }
}
