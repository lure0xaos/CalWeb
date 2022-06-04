package gargoyle.calendar.web.config

import kotlinx.css.CSSBuilder
import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.response.*
import java.util.ResourceBundle

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit): Unit =
    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)

operator fun ResourceBundle.get(key: String): String = if (containsKey(key)) getString(key) else key
