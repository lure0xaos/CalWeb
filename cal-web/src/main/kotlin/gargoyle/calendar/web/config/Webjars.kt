package gargoyle.calendar.web.config

import io.ktor.application.*
import io.ktor.webjars.*

fun Application.configureWebjars() {
    install(Webjars) { path = "/webjars" }
}
