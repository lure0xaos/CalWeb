package gargoyle.calendar.web.routes

import gargoyle.calendar.core.CalUtil
import gargoyle.calendar.web.config.MODEL_YEAR
import gargoyle.calendar.web.config.PATH_DOWNLOAD
import gargoyle.calendar.web.config.PATH_LAYOUT_CSS
import gargoyle.calendar.web.config.PATH_ROOT
import gargoyle.calendar.web.config.bsButton
import gargoyle.calendar.web.config.bsInput
import gargoyle.calendar.web.config.bsLabel
import gargoyle.calendar.web.config.col
import gargoyle.calendar.web.config.get
import gargoyle.calendar.web.config.getSession
import gargoyle.calendar.web.config.row
import gargoyle.calendar.web.config.stylesheet
import kotlinx.html.ATarget
import kotlinx.html.ButtonType
import kotlinx.html.FormEncType
import kotlinx.html.FormMethod
import kotlinx.html.InputType
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
import kotlinx.html.li
import kotlinx.html.main
import kotlinx.html.meta
import kotlinx.html.nav
import kotlinx.html.script
import kotlinx.html.span
import kotlinx.html.strong
import kotlinx.html.title
import kotlinx.html.ul
import kotlinx.html.unsafe
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.util.pipeline.*
import java.util.ResourceBundle
import kotlin.collections.set

suspend fun PipelineContext<Unit, ApplicationCall>.getIndex() {
    val application = ResourceBundle.getBundle("application")
    val messages = ResourceBundle.getBundle("messages")
    val session = getSession()
    call.respondHtml {
        head {
            meta(charset = application["application.encoding"])
            meta(content = "width=device-width, initial-scale=1", name = "viewport")
            stylesheet(href = "webjars/bootstrap/css/bootstrap.min.css")
            stylesheet(href = "webjars/bootstrap-icons/font/bootstrap-icons.css")
            stylesheet(href = PATH_LAYOUT_CSS)
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
                    if (session.error != null) {
                        row {
                            col { }
                            col {
                                div(classes = "alert alert-danger") {
                                    i(classes = "bi bi-exclamation-triangle-fill")
                                    strong { +(session.error?.message ?: "") }
                                    div { +(session.error?.details ?: "") }
                                }
                            }
                        }
                    }
                    if (session.output != null) {
                        row {
                            col { }
                            col {
                                div(classes = "alert alert-success") {
                                    h4 { +messages["success"] }
                                    +messages["download"]
                                    a(href = PATH_DOWNLOAD, target = ATarget.blank) { +session.year.toString() }
                                }
                            }
                        }
                    }
                    form(classes = "form", encType = FormEncType.multipartFormData, method = FormMethod.post) {
                        row {
                            col {
                                bsLabel(messages["year"])
                            }
                            col {
                                bsInput(
                                    type = InputType.number,
                                    name = MODEL_YEAR,
                                    value = CalUtil.currentYear.toString()
                                )
                            }
                        }
                        row {
                            col {
                                bsLabel(messages["imageLocation"])
                            }
                            col {
                                bsInput(type = InputType.file, name = "imageLocation")
                            }
                        }
                        row {
                            col { }
                            col {
                                bsButton(type = ButtonType.submit, classes = "primary") {
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
