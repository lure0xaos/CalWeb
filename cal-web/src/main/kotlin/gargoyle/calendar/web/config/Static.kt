package gargoyle.calendar.web.config

import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Routing.configureStatic() {
    static("/static") { resources("static") }
}
