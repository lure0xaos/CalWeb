package gargoyle.calendar.web.routes

import gargoyle.calendar.cli.Cal
import gargoyle.calendar.web.config.PATH_ROOT
import gargoyle.calendar.web.config.getSession
import gargoyle.calendar.web.data.ExceptionInfo
import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

suspend fun PipelineContext<Unit, ApplicationCall>.getDownload() {
    val session = getSession()
    runCatching {
        requireNotNull(session.output).let {
            call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Attachment.withParameter(
                    ContentDisposition.Parameters.FileName,
                    "${session.year}${Cal.SUFFIX}"
                ).toString()
            )
            call.respondFile(it.toFile())
        }
    }.onFailure {
        session.output = null
        session.error = ExceptionInfo(it)
        call.respondRedirect(PATH_ROOT)
    }
}
