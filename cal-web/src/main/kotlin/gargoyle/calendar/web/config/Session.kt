package gargoyle.calendar.web.config

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import gargoyle.calendar.util.resources.Resource
import gargoyle.calendar.util.resources.Resources
import gargoyle.calendar.web.data.CalConfigSession
import io.ktor.application.*
import io.ktor.sessions.*
import io.ktor.util.pipeline.*
import java.awt.Color
import java.awt.Font
import java.net.URL
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Year
import kotlin.reflect.KClass

fun Application.configureSession() {
    install(Sessions) {
        cookie<CalConfigSession>(name = CalConfigSession::class.simpleName!!, storage = SessionStorageMemory()) {
            serializer = GsonSessionSerializer(CalConfigSession::class) {
                registerTypeAdapter(YearTypeAdapter.TYPE.java, YearTypeAdapter)
                registerTypeAdapter(ColorTypeAdapter.TYPE.java, ColorTypeAdapter)
                registerTypeAdapter(FontTypeAdapter.TYPE.java, FontTypeAdapter)
                registerTypeAdapter(ResourceTypeAdapter.TYPE.java, ResourceTypeAdapter)
                registerTypeAdapter(PathTypeAdapter.TYPE.java, PathTypeAdapter)
                registerTypeHierarchyAdapter(PathTypeAdapter.TYPE.java, PathTypeAdapter)
            }
        }
    }
}

fun PipelineContext<Unit, ApplicationCall>.getSession(): CalConfigSession =
    call.sessions.get<CalConfigSession>() ?: CalConfigSession().also { setSession(it) }

fun PipelineContext<Unit, ApplicationCall>.setSession(session: CalConfigSession): Unit =
    call.sessions.set(session)

object YearTypeAdapter : TypeAdapter<Year>() {
    val TYPE: KClass<Year> = Year::class
    override fun write(out: JsonWriter, value: Year?): Unit =
        (value?.let { out.value(value.value.toString()) } ?: out.nullValue()).let { }

    override fun read(input: JsonReader): Year? = input.nextString()?.let { Year.of(it.toInt()) }
}

object ColorTypeAdapter : TypeAdapter<Color>() {
    val TYPE: KClass<Color> = Color::class
    override fun write(out: JsonWriter, value: Color?): Unit =
        (value?.let { out.value(value.rgb.toString()) } ?: out.nullValue()).let { }

    override fun read(input: JsonReader): Color? = input.nextString()?.let { Color(it.toInt()) }
}

object FontTypeAdapter : TypeAdapter<Font>() {
    val TYPE: KClass<Font> = Font::class
    override fun write(out: JsonWriter, value: Font?): Unit =
        (value?.let { out.value(value.toString()) } ?: out.nullValue()).let { }

    override fun read(input: JsonReader): Font? = input.nextString()?.let { Font.decode(it) }
}

object ResourceTypeAdapter : TypeAdapter<Resource>() {
    val TYPE: KClass<Resource> = Resource::class
    override fun write(out: JsonWriter, value: Resource?): Unit =
        (value?.let { out.value(value.url.toExternalForm()) } ?: out.nullValue()).let { }

    override fun read(input: JsonReader): Resource? = input.nextString()?.let { Resources.getResource(URL(it)) }
}

object PathTypeAdapter : TypeAdapter<Path>() {
    val TYPE: KClass<Path> = Path::class
    override fun write(out: JsonWriter, value: Path?): Unit =
        (value?.let { out.value(value.toString()) } ?: out.nullValue()).let { }

    override fun read(input: JsonReader): Path? = input.nextString()?.let { Paths.get(it) }
}
