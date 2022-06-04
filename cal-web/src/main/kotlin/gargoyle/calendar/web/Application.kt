package gargoyle.calendar.web

import gargoyle.calendar.web.config.*
import gargoyle.calendar.web.css.cssLayout
import gargoyle.calendar.web.routes.getDownload
import gargoyle.calendar.web.routes.getIndex
import gargoyle.calendar.web.routes.postIndex
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = 8080) {
        serverModule()
    }.start(wait = true)
}

private fun Application.serverModule() {
    configureWebjars()
    configureSession()
    configureErrors()
    routing {
        configureStatic()
        get(PATH_LAYOUT_CSS) { cssLayout() }
        get(PATH_ROOT) { getIndex() }
        post(PATH_ROOT) { postIndex() }
        get(PATH_DOWNLOAD) { getDownload() }
    }
}
