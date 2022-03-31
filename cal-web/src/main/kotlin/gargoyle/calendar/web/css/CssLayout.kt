package gargoyle.calendar.web.css

import gargoyle.calendar.web.config.respondCss
import kotlinx.css.minHeight
import kotlinx.css.padding
import kotlinx.css.px
import io.ktor.application.*
import io.ktor.util.pipeline.*

suspend fun PipelineContext<Unit, ApplicationCall>.cssLayout() {
    call.respondCss {
        rule("main > .container") {
            padding = "60px 15px 0"
            minHeight = 600.px
        }
    }
}
