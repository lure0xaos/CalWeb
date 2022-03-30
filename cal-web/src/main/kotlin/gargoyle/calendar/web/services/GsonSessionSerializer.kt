package gargoyle.calendar.web.services

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import gargoyle.calendar.util.resources.Resource
import gargoyle.calendar.util.resources.Resources
import io.ktor.sessions.*
import java.awt.Color
import java.awt.Font
import java.net.URL
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Year
import kotlin.reflect.KClass

class GsonSessionSerializer<T : Any>(val type: KClass<T>, gson: Gson = Gson(), configure: Gson.() -> Unit = {}) :
    SessionSerializer<T> {
    val gson: Gson

    private fun config(gson: Gson): GsonBuilder =
        gson.newBuilder()
            .registerTypeAdapter(Year::class.java, object : TypeAdapter<Year>() {
                override fun write(out: JsonWriter, value: Year?) {
                    if (value == null) out.nullValue() else out.value(value.value.toString())
                }

                override fun read(input: JsonReader): Year? {
                    val nextString = input.nextString()
                    return if (nextString == null) null else Year.of(nextString.toInt())
                }
            })
            .registerTypeAdapter(Color::class.java, object : TypeAdapter<Color>() {
                override fun write(out: JsonWriter, value: Color?) {
                    if (value == null) out.nullValue() else out.value(value.rgb.toString())
                }

                override fun read(input: JsonReader): Color? {
                    val nextString = input.nextString()
                    return if (nextString == null) null else Color(nextString.toInt())
                }
            })
            .registerTypeAdapter(Font::class.java, object : TypeAdapter<Font>() {
                override fun write(out: JsonWriter, value: Font?) {
                    if (value == null) out.nullValue() else out.value(value.toString())
                }

                override fun read(input: JsonReader): Font? {
                    val nextString = input.nextString()
                    return if (nextString == null) null else Font.decode(nextString)
                }
            })
            .registerTypeAdapter(Resource::class.java, object : TypeAdapter<Resource>() {
                override fun write(out: JsonWriter, value: Resource?) {
                    if (value == null) out.nullValue() else out.value(value.url.toExternalForm())
                }

                override fun read(input: JsonReader): Resource? {
                    val nextString = input.nextString()
                    return if (nextString == null) null else Resources.getResource(URL(nextString))
                }
            })
            .registerTypeAdapter(Path::class.java, object : TypeAdapter<Path>() {
                override fun write(out: JsonWriter, value: Path?) {
                    if (value == null) out.nullValue() else out.value(value.toString())
                }

                override fun read(input: JsonReader): Path? {
                    val nextString = input.nextString()
                    return if (nextString == null) null else Paths.get(nextString)
                }
            })
            .registerTypeHierarchyAdapter(Path::class.java, object : TypeAdapter<Path>() {
                override fun write(out: JsonWriter, value: Path?) {
                    if (value == null) out.nullValue() else out.value(value.toString())
                }

                override fun read(input: JsonReader): Path? {
                    val nextString = input.nextString()
                    return if (nextString == null) null else Paths.get(nextString)
                }
            })

    override fun serialize(session: T): String = gson.toJson(session)
    override fun deserialize(text: String): T = gson.fromJson(text, type.java) as T

    init {
        this.gson = config(gson).create().also(configure)
    }
}
