package gargoyle.calendar.web

import gargoyle.calendar.web.config.PATH_DOWNLOAD
import gargoyle.calendar.web.config.PATH_LAYOUT_CSS
import gargoyle.calendar.web.config.PATH_ROOT
import gargoyle.calendar.web.config.configureErrors
import gargoyle.calendar.web.config.configureSession
import gargoyle.calendar.web.config.configureStatic
import gargoyle.calendar.web.config.configureWebjars
import gargoyle.calendar.web.css.cssLayout
import gargoyle.calendar.web.routes.getDownload
import gargoyle.calendar.web.routes.getIndex
import gargoyle.calendar.web.routes.postIndex
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

@Suppress("BlockingMethodInNonBlockingContext")
fun main() {
    embeddedServer(Netty, port = 8080) {
        configureWebjars()
        configureSession()
        routing {
            configureErrors()
            configureStatic()
            get(PATH_LAYOUT_CSS) { cssLayout() }
            get(PATH_ROOT) { getIndex() }
            post(PATH_ROOT) { postIndex() }
            get(PATH_DOWNLOAD) { getDownload() }
        }
    }.start(wait = true)
}
