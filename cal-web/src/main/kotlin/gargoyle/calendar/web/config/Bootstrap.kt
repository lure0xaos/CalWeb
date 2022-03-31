package gargoyle.calendar.web.config

import kotlinx.html.BUTTON
import kotlinx.html.ButtonFormEncType
import kotlinx.html.ButtonFormMethod
import kotlinx.html.ButtonType
import kotlinx.html.DIV
import kotlinx.html.FlowContent
import kotlinx.html.FlowOrInteractiveOrPhrasingContent
import kotlinx.html.FlowOrMetaDataOrPhrasingContent
import kotlinx.html.HtmlTagMarker
import kotlinx.html.INPUT
import kotlinx.html.InputFormEncType
import kotlinx.html.InputFormMethod
import kotlinx.html.InputType
import kotlinx.html.LABEL
import kotlinx.html.LINK
import kotlinx.html.LinkRel
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.input
import kotlinx.html.label
import kotlinx.html.link
import io.ktor.http.*

@HtmlTagMarker
inline fun FlowOrMetaDataOrPhrasingContent.stylesheet(
    href: String? = null,
    rel: String? = LinkRel.stylesheet,
    type: String? = ContentType.Text.CSS.toString(),
    crossinline block: LINK.() -> Unit = {}
): Unit =
    link(href = href, rel = rel, type = type, block = block)

@HtmlTagMarker
inline fun FlowContent.col(classes: String? = null, crossinline block: DIV.() -> Unit = {}): Unit =
    div(classes = if (classes?.isNotEmpty() == true) "col-$classes" else "col", block = block)

@HtmlTagMarker
inline fun FlowContent.row(classes: String? = null, crossinline block: DIV.() -> Unit = {}): Unit =
    div(classes = if (classes?.isNotEmpty() == true) "row $classes" else "row", block = block)

@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.bsLabel(
    value: String = "",
    classes: String? = null,
    crossinline block: LABEL.() -> Unit = {}
): Unit =
    label(classes = if (classes?.isNotEmpty() == true) "form-label $classes" else "form-label") {
        text(value)
        block()
    }

@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.bsInput(
    type: InputType? = InputType.text,
    value: String = "",
    name: String? = null,
    formEncType: InputFormEncType? = null,
    formMethod: InputFormMethod? = null,
    classes: String? = null,
    crossinline block: INPUT.() -> Unit = {}
): Unit =
    input(
        classes = if (classes?.isNotEmpty() == true) "form-control $classes" else "form-control",
        type = type,
        name = name,
        formEncType = formEncType,
        formMethod = formMethod,
        block = {
            attributes["value"] = value
            block()
        }
    )

@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.bsButton(
    name: String? = null,
    type: ButtonType? = null,
    formEncType: ButtonFormEncType? = null,
    formMethod: ButtonFormMethod? = null,
    classes: String? = null,
    crossinline block: BUTTON.() -> Unit = {}
): Unit =
    button(
        name = name,
        type = type,
        formEncType = formEncType,
        formMethod = formMethod,
        classes = if (classes?.isNotEmpty() == true) "btn btn-$classes" else "btn",
        block = block
    )
