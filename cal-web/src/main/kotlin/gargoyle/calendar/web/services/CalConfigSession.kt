package gargoyle.calendar.web.services

import gargoyle.calendar.cli.Cal.CONFIG_LOCATION
import gargoyle.calendar.core.CalConfig
import gargoyle.calendar.core.CalCore
import gargoyle.calendar.util.BeanModel
import gargoyle.calendar.util.resources.Resources.getResource
import gargoyle.calendar.web.data.ExceptionInfo
import java.nio.file.Path
import java.time.Year

data class CalConfigSession(
    var year: Year? = null
) {
    var configuration: CalConfig = BeanModel.load(CalConfig::class, getResource(CONFIG_LOCATION, CalCore::class))

    var input: Path? = null
    var output: Path? = null
    var error: ExceptionInfo? = null

    override fun toString(): String =
        "CalConfigSession(year=$year, input=$input, output=$output, configuration=$configuration, error=$error)"
}
