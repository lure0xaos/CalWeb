package gargoyle.calendar.web.config

import io.ktor.http.content.*
import io.ktor.routing.*

fun Routing.configureStatic() {
    static("/static") { resources("static") }
}
