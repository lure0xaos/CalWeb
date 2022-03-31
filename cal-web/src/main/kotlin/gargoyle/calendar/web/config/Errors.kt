package gargoyle.calendar.web.config

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*

fun Routing.configureErrors() {
    install(StatusPages) {
        exception { e: Exception -> call.respondRedirect(PATH_ROOT).also { e.printStackTrace() } }
    }
}
