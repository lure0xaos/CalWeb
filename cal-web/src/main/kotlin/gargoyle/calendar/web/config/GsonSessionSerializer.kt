package gargoyle.calendar.web.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.ktor.server.sessions.*
import kotlin.reflect.KClass

class GsonSessionSerializer<T : Any>(val type: KClass<T>, gson: Gson = Gson(), configure: GsonBuilder.() -> Unit = {}) :
    SessionSerializer<T> {
    val gson: Gson

    override fun serialize(session: T): String = gson.toJson(session)
    override fun deserialize(text: String): T = gson.fromJson(text, type.java) as T

    init {
        this.gson = gson.newBuilder().also { configure(it) }.create()
    }
}
