package gargoyle.calendar.web

import gargoyle.calendar.cli.Cal
import gargoyle.calendar.core.CalCore
import gargoyle.calendar.core.CalUtil
import gargoyle.calendar.util.resources.FileSystemResource
import gargoyle.calendar.util.resources.Resources
import gargoyle.calendar.web.data.ExceptionInfo
import gargoyle.calendar.web.services.CalConfigSession
import gargoyle.calendar.web.services.GsonSessionSerializer
import kotlinx.css.CSSBuilder
import kotlinx.css.minHeight
import kotlinx.css.padding
import kotlinx.css.px
import kotlinx.html.ATarget
import kotlinx.html.ButtonType
import kotlinx.html.FormEncType
import kotlinx.html.FormMethod
import kotlinx.html.InputType
import kotlinx.html.LinkRel
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.footer
import kotlinx.html.form
import kotlinx.html.h1
import kotlinx.html.h4
import kotlinx.html.head
import kotlinx.html.header
import kotlinx.html.i
import kotlinx.html.input
import kotlinx.html.label
import kotlinx.html.li
import kotlinx.html.link
import kotlinx.html.main
import kotlinx.html.meta
import kotlinx.html.nav
import kotlinx.html.script
import kotlinx.html.span
import kotlinx.html.strong
import kotlinx.html.title
import kotlinx.html.ul
import kotlinx.html.unsafe
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.sessions.*
import io.ktor.webjars.*
import java.io.BufferedOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.time.Year
import java.util.Locale
import java.util.ResourceBundle
import kotlin.collections.set
import kotlin.io.path.outputStream

@Suppress("BlockingMethodInNonBlockingContext")
fun main() {
    embeddedServer(Netty, port = 8080) {

        routing {

            get(PATH_ROOT) {
                val application = ResourceBundle.getBundle("application")
                val messages = ResourceBundle.getBundle("messages")
                val session = call.sessions.get<CalConfigSession>() ?: CalConfigSession()
                call.sessions.set(session)
                call.respondHtml {
                    head {
                        meta(charset = application["application.encoding"])
                        meta(content = "width=device-width, initial-scale=1", name = "viewport")
                        link(
                            href = "webjars/bootstrap/css/bootstrap.min.css",
                            rel = LinkRel.stylesheet,
                            type = ContentType.Text.CSS.toString()
                        )
                        link(
                            href = "webjars/bootstrap-icons/font/bootstrap-icons.css",
                            rel = LinkRel.stylesheet,
                            type = ContentType.Text.CSS.toString()
                        )
                        link(href = PATH_STATIC_STYLE, rel = LinkRel.stylesheet, type = ContentType.Text.CSS.toString())
                        title(application["application.title"])
                    }
                    body(classes = "d-flex flex-column h-100") {
                        header {
                            nav("navbar navbar-expand-md navbar-dark fixed-top bg-dark") {
                                div(classes = "container-fluid") {
                                    a(classes = "navbar-brand", href = PATH_ROOT) { +application["application.name"] }
                                    button(classes = "navbar-toggler", type = ButtonType.button) {
                                        attributes["data-bs-target"] = "#navbarCollapse"
                                        attributes["data-bs-toggle"] = "collapse"
                                        span(classes = "navbar-toggler-icon")
                                    }
                                    div(classes = "collapse navbar-collapse") {
                                        attributes["id"] = "navbarCollapse"
                                        ul(classes = "navbar-nav me-auto mb-2 mb-md-0") {
                                            li(classes = "nav-item") {
                                                a(classes = "nav-link active", href = PATH_ROOT) { +"Home" }
                                            }
                                        }
                                        div(classes = "d-flex")
                                    }
                                }
                            }
                        }
                        main(classes = "footer mt-auto py-3 bg-light flex-shrink-0") {
                            div(classes = "container") {
                                h1(classes = "mt-5 page-title") {
                                    +messages["index.title"]
                                }
                                if (session.error != null) div(classes = "row") {
                                    div(classes = "col") { }
                                    div(classes = "col") {
                                        div(classes = "alert alert-danger") {
                                            i(classes = "bi bi-exclamation-triangle-fill")
                                            strong { +session.error!!.message }
                                            div { +session.error!!.details }
                                        }
                                    }
                                }
                                if (session.output != null) div(classes = "row") {
                                    div(classes = "col") { }
                                    div(classes = "col") {
                                        div(classes = "alert alert-success") {
                                            h4 { +messages.getString("success") }
                                            +messages.getString("download")
                                            a(href = PATH_DOWNLOAD, target = ATarget.blank) { +session.year.toString() }
                                        }
                                    }
                                }
                                form(
                                    classes = "form",
                                    encType = FormEncType.multipartFormData,
                                    method = FormMethod.post
                                ) {
                                    div(classes = "row") {
                                        div(classes = "col") {
                                            label(classes = "form-label") { +messages["year"] }
                                        }
                                        div(classes = "col") {
                                            input(classes = "form-control", type = InputType.number, name = "year") {
                                                attributes["value"] = CalUtil.currentYear.toString()
                                            }
                                        }
                                    }
                                    div(classes = "row") {
                                        div(classes = "col") {
                                            label(classes = "form-label") { +messages["imageLocation"] }
                                        }
                                        div(classes = "col") {
                                            input(
                                                classes = "form-control",
                                                type = InputType.file,
                                                name = "imageLocation"
                                            )
                                        }
                                    }
                                    div(classes = "row") {
                                        div(classes = "col") { }
                                        div(classes = "col") {
                                            button(type = ButtonType.submit, classes = "btn btn-primary") {
                                                i(classes = "bi bi-caret-right-square-fill") { }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        footer(classes = "footer mt-auto py-3 bg-light") {
                            div(classes = "container text-muted") {
                                div(classes = "text-left float-start") {
                                    +application["application.description"]
                                    +application["application.build"]
                                }
                                div(classes = "text-right float-end") {
                                    unsafe { +"&copy;" }
                                    +application["application.inceptionYear"]
                                    a(href = application["application.organization.url"], target = ATarget.blank) {
                                        +application["application.organization.name"]
                                    }
                                }
                            }
                        }
                        script(src = "webjars/jquery/jquery.min.js") {}
                        script(src = "webjars/popper.js/umd/popper.min.js") {}
                        script(src = "webjars/bootstrap/js/bootstrap.min.js") {}
                    }
                }
            }

            post(PATH_ROOT) {
                val session = call.sessions.get<CalConfigSession>() ?: CalConfigSession()
                call.receiveMultipart().forEachPart { part ->
                    when {
                        part.name == MODEL_IMAGE_LOCATION && part is PartData.FileItem -> {
                            session.input = Files.createTempFile(
                                (call.parameters[Cal.CMD_YEAR]
                                    ?: CalUtil.currentYear.toString()).toString(), Cal.SUFFIX
                            ).also { path ->
                                part.streamProvider().use { stream ->
                                    path.outputStream().buffered().use<BufferedOutputStream, Unit> { stream.copyTo(it) }
                                }
                            }
                            try {
                                val image = CalUtil.readImage(Resources.getResource(session.input!!))
                                session.configuration.canvasWidth = image.width
                                session.configuration.canvasHeight = image.height
                            } catch (e: Exception) {
                                session.input = null
                            }
                        }
                        part.name == Cal.CMD_YEAR && part is PartData.FormItem ->
                            session.year = Year.of(part.value.toInt())
                    }
                    part.dispose()
                }

                try {
                    if (session.input != null) {
                        session.configuration.imageLocation = Resources.getResource(session.input!!)
                        val out = FileSystemResource(Files.createTempFile(session.year.toString(), Cal.SUFFIX))
                        CalCore(session.configuration).createWrite(out, session.year!!, Locale.getDefault(), Cal.FORMAT)
                        session.output = Paths.get(out.url.toURI())
                        session.error = null
                    } else {
                        session.output = null
                        session.error = ExceptionInfo(FileNotFoundException())
                    }
                } catch (e: Exception) {
                    session.output = null
                    session.error = ExceptionInfo(e)
                }
                call.sessions.set(session)
                call.respondRedirect(PATH_ROOT)
            }

            get(PATH_DOWNLOAD) {
                val session = call.sessions.get<CalConfigSession>() ?: CalConfigSession()
                try {
                    if (session.output != null && session.error == null) {
                        val pathOut = session.output!!
                        call.response.header(
                            HttpHeaders.ContentDisposition,
                            ContentDisposition.Attachment.withParameter(
                                ContentDisposition.Parameters.FileName,
                                "${session.year}${Cal.SUFFIX}"
                            ).toString()
                        )
                        call.respondFile(pathOut.toFile())
                    }
                } catch (e: IOException) {
                    session.output = null
                    session.error = ExceptionInfo(e)
                }
                call.respondRedirect(PATH_ROOT)
            }

            get(PATH_STATIC_STYLE) {
                call.respondCss {
                    rule("main > .container") {
                        padding = "60px 15px 0"
                        minHeight = 600.px
                    }
                }
            }

            install(StatusPages) {
                exception<Exception> {
                    it.printStackTrace()
                    call.respondRedirect(PATH_ROOT)
                }
            }

            static("/static") {
                resources("static")
            }
        }

        install(Sessions) {
            cookie<CalConfigSession>(name = "CalConfigSession", storage = SessionStorageMemory()) {
                serializer = GsonSessionSerializer(CalConfigSession::class)
                cookie.extensions["CalConfigSession"] = "CalConfigSession"
            }
        }

        install(Webjars) { path = "/webjars" }

    }.start(wait = true)
}


const val PATH_ROOT: String = "/"
const val PATH_DOWNLOAD: String = "/download"
const val PATH_STATIC_STYLE: String = "/layout.css"
private const val MODEL_IMAGE_LOCATION = "imageLocation"
private const val VIEW_INDEX = "index"

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit): Unit =
    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)

operator fun ResourceBundle.get(key: String): String = if (containsKey(key)) getString(key) else key
