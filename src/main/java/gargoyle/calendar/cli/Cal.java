package gargoyle.calendar.cli;

import gargoyle.calendar.core.CalConfig;
import gargoyle.calendar.core.CalCore;
import gargoyle.calendar.core.CalUtil;
import gargoyle.calendar.util.Args;
import gargoyle.calendar.util.beans.BeanModel;
import gargoyle.calendar.util.resources.FileSystemResource;
import gargoyle.calendar.util.resources.Resource;
import gargoyle.calendar.util.resources.Resources;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Year;
import java.util.Locale;
import java.util.Map;

public final class Cal {
    public static final String CONFIG_LOCATION = "cal.properties";
    public static final String CMD_OUT = "year";
    public static final String CMD_SHOW = "show";
    public static final String CMD_YEAR = "year";
    public static final String SUFFIX = ".png";
    public static final String FORMAT = "PNG";

    private Cal() {
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        Locale locale = Locale.getDefault();
        Map<String, String> cmd = Args.parseArgs(new String[]{CMD_YEAR, CMD_OUT, CMD_SHOW}, args);
        Year year = Year.of(Integer.parseInt(cmd.getOrDefault(CMD_YEAR, String.valueOf(CalUtil.getCurrentYear()))));
        Resource configResource = Resources.getResource(CONFIG_LOCATION, CalCore.class);
        CalConfig configuration = BeanModel.load(CalConfig.class, configResource);
        Resource out = new FileSystemResource(Paths.get(cmd.getOrDefault(CMD_OUT, year + SUFFIX)));
        new CalCore(configuration).createWrite(out, year, locale, FORMAT);
        if (!GraphicsEnvironment.isHeadless() && Desktop.isDesktopSupported()) {
            if (Boolean.parseBoolean(cmd.getOrDefault(CMD_SHOW, Boolean.FALSE.toString()))) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    URL url = out.getUrl();
                    URI uri = url.toURI();
                    desktop.browse(uri);
                }
            }
        }
    }
}
