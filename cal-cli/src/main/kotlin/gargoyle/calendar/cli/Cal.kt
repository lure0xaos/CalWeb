package gargoyle.calendar.cli

import gargoyle.calendar.core.CalConfig
import gargoyle.calendar.core.CalCore
import gargoyle.calendar.core.CalUtil.currentYear
import gargoyle.calendar.util.Args.parseArgs
import gargoyle.calendar.util.BeanModel
import gargoyle.calendar.util.resources.FileSystemResource
import gargoyle.calendar.util.resources.Resource
import gargoyle.calendar.util.resources.Resources.getResource
import java.awt.Desktop
import java.awt.GraphicsEnvironment
import java.nio.file.Paths
import java.time.Year
import java.util.Locale

object Cal {
    const val CONFIG_LOCATION: String = "cal.properties"
    const val CMD_OUT: String = "year"
    const val CMD_SHOW: String = "show"
    const val CMD_YEAR: String = "year"
    const val SUFFIX: String = ".png"
    const val FORMAT: String = "PNG"

    @JvmStatic
    fun main(args: Array<String>) {
        val locale = Locale.getDefault()
        val cmd = parseArgs(arrayOf(CMD_YEAR, CMD_OUT, CMD_SHOW), args)
        val year = Year.of(cmd.getOrDefault(CMD_YEAR, currentYear.toString()).toInt())
        val configResource = getResource(CONFIG_LOCATION, CalCore::class)
        val configuration: CalConfig = BeanModel.load(CalConfig::class, configResource)
        val out: Resource = FileSystemResource(Paths.get(cmd.getOrDefault(CMD_OUT, "$year$SUFFIX")))
        CalCore(configuration).createWrite(out, year, locale, FORMAT)
        if (!GraphicsEnvironment.isHeadless() && Desktop.isDesktopSupported()) {
            if (cmd.getOrDefault(CMD_SHOW, false.toString()).toBoolean()) {
                Desktop.getDesktop().apply {
                    if (isSupported(Desktop.Action.BROWSE)) {
                        browse(out.url.toURI())
                    }
                }
            }
        }
    }
}
