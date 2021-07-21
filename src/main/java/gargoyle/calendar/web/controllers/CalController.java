package gargoyle.calendar.web.controllers;

import gargoyle.calendar.core.CalConfig;
import gargoyle.calendar.core.CalCore;
import gargoyle.calendar.core.CalUtil;
import gargoyle.calendar.util.resources.FileSystemResource;
import gargoyle.calendar.util.resources.Resource;
import gargoyle.calendar.util.resources.Resources;
import gargoyle.calendar.web.data.ExceptionInfo;
import gargoyle.calendar.web.services.CalConfigSessionHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.time.Year;
import java.util.Locale;
import java.util.Map;

import static gargoyle.calendar.cli.Cal.*;

@Controller
@RequestMapping(CalController.PATH_ROOT)
@RequiredArgsConstructor
public class CalController {
    public static final String PATH_ROOT = "/";
    public static final String PATH_DOWNLOAD = "/download";
    public static final String METHOD_INDEX = "getIndex";
    private static final String ATTACHMENT_FILENAME_0_1 = "attachment;filename=\"{0}{1}\"";
    private static final String MODEL_IMAGE_LOCATION = "imageLocation";
    private static final String VIEW_INDEX = "index";
    private final CalConfigSessionHolder holder;

    @GetMapping(PATH_ROOT)
    public ModelAndView getIndex() {
        return new ModelAndView(VIEW_INDEX);
    }

    @PostMapping(PATH_ROOT)
    public ModelAndView onIndex(@RequestParam(MODEL_IMAGE_LOCATION) MultipartFile imageLocation,
                                Map<String, String> model) {
        try {
            Locale locale = LocaleContextHolder.getLocale();
            Year year = Year.of(Integer.parseInt(model.getOrDefault(CMD_YEAR, String.valueOf(CalUtil.getCurrentYear()))));
            holder.setYear(year);
            CalConfig configuration = holder.getConfiguration();
            Path tempIn = holder.getInput();
            if (!imageLocation.isEmpty()) {
                tempIn = Files.createTempFile(String.valueOf(year), SUFFIX);
                imageLocation.transferTo(tempIn);
                holder.setInput(tempIn);
                Resource resource = Resources.getResource(tempIn);
                BufferedImage image = CalUtil.readImage(resource);
                configuration.setCanvasWidth(image.getWidth());
                configuration.setCanvasHeight(image.getHeight());
            }
            if (holder.hasInput()) {
                configuration.setImageLocation(Resources.getResource(tempIn));
                Resource out = new FileSystemResource(Files.createTempFile(String.valueOf(year), SUFFIX));
                new CalCore(configuration).createWrite(out, year, locale, FORMAT);
                Path pathOut = Paths.get(out.getUrl().toURI());
                holder.setOutput(pathOut);
                holder.setError(null);
            } else {
                holder.setOutput(null);
                holder.setError(new ExceptionInfo(new FileNotFoundException()));
            }
        } catch (IOException | IllegalStateException | NumberFormatException | URISyntaxException e) {
            holder.setOutput(null);
            holder.setError(new ExceptionInfo(e));
        }
        return new ModelAndView(VIEW_INDEX);
    }

    @GetMapping(PATH_DOWNLOAD)
    public ResponseEntity<?> getDownload() {
        try {
            if (holder.hasOutput() && !holder.hasError()) {
                Path pathOut = holder.getOutput();
                return ResponseEntity.ok()
                        .contentType(MediaType.valueOf(Files.probeContentType(pathOut)))
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                MessageFormat.format(ATTACHMENT_FILENAME_0_1, holder.getYear(), SUFFIX))
                        .body(Files.readAllBytes(pathOut));
            }
        } catch (IOException e) {
            holder.setOutput(null);
            holder.setError(new ExceptionInfo(e));
        }
        return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
                .header(HttpHeaders.LOCATION,
                        MvcUriComponentsBuilder.fromMethodName(CalController.class, METHOD_INDEX)
                                .build().toUriString())
                .build();
    }

}
