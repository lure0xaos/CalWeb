package gargoyle.calendar.web.routes

import gargoyle.calendar.cli.Cal
import gargoyle.calendar.core.CalCore
import gargoyle.calendar.core.CalUtil
import gargoyle.calendar.util.resources.FileSystemResource
import gargoyle.calendar.util.resources.Resources
import gargoyle.calendar.web.config.MODEL_IMAGE_LOCATION
import gargoyle.calendar.web.config.MODEL_YEAR
import gargoyle.calendar.web.config.PATH_ROOT
import gargoyle.calendar.web.config.getSession
import gargoyle.calendar.web.config.setSession
import gargoyle.calendar.web.data.ExceptionInfo
import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.util.pipeline.*
import java.io.FileNotFoundException
import java.time.Year
import java.util.Locale
import kotlin.io.path.createTempFile
import kotlin.io.path.outputStream

suspend fun PipelineContext<Unit, ApplicationCall>.postIndex() {
    val locale = Locale.getDefault()
    val session = getSession()
    call.receiveMultipart().forEachPart { part ->
        when {
            part.name == MODEL_IMAGE_LOCATION && part is PartData.FileItem -> {
                session.input =
                    createTempFile((call.parameters[MODEL_YEAR] ?: CalUtil.currentYear).toString(), Cal.SUFFIX)
                        .also { path ->
                            part.streamProvider().use { stream ->
                                path.outputStream().buffered().use { stream.copyTo(it) }
                            }
                        }
                        .let { path ->
                            runCatching { CalUtil.readImage(Resources.getResource(path)) }.onSuccess {
                                with(session.configuration) {
                                    canvasWidth = it.width
                                    canvasHeight = it.height
                                }
                            }.getOrNull()?.let { path }
                        }
            }
            part.name == Cal.CMD_YEAR && part is PartData.FormItem ->
                session.year = Year.of(part.value.toInt())
        }
        part.dispose()
    }
    session.output = runCatching {
        session.configuration.imageLocation = Resources.getResource(session.input ?: throw FileNotFoundException())
        with(session.year!!) {
            createTempFile(toString(), Cal.SUFFIX).also {
                CalCore(session.configuration).createWrite(FileSystemResource(it), this, locale, Cal.FORMAT)
            }
        }
    }.also { result -> session.error = result.exceptionOrNull()?.let { ExceptionInfo(it) } }
        .getOrNull()
    setSession(session)
    call.respondRedirect(PATH_ROOT)

}
