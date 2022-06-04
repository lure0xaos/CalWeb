package gargoyle.calendar.web.config

import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*


fun Application.configureErrors() {
    install(StatusPages) {
        exception { call, e: Exception -> call.respondRedirect(PATH_ROOT).also { e.printStackTrace() } }
    }
}
