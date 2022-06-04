package gargoyle.calendar.web.config

import io.ktor.server.application.*
import io.ktor.server.webjars.*

fun Application.configureWebjars() {
    install(Webjars) { path = "/webjars" }
}
