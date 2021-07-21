package gargoyle.calendar.web.services;

import gargoyle.calendar.core.CalConfig;
import gargoyle.calendar.core.CalCore;
import gargoyle.calendar.util.beans.BeanModel;
import gargoyle.calendar.util.resources.Resource;
import gargoyle.calendar.util.resources.Resources;
import gargoyle.calendar.web.data.ExceptionInfo;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.Year;

import static gargoyle.calendar.cli.Cal.CONFIG_LOCATION;
import static org.springframework.web.context.WebApplicationContext.SCOPE_SESSION;

@Service
@Scope(value = SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Data
public class CalConfigSessionHolder {
    private Year year;
    private Path input;
    private Path output;
    private CalConfig configuration;
    private ExceptionInfo error;

    public CalConfig getConfiguration() {
        if (configuration == null) {
            Resource configResource = Resources.getResource(CONFIG_LOCATION, CalCore.class);
            configuration = BeanModel.load(CalConfig.class, configResource);
        }
        return configuration;
    }

    public boolean hasInput() {
        return input != null;
    }

    public boolean hasOutput() {
        return output != null;
    }

    public boolean hasError() {
        return error != null;
    }
}
