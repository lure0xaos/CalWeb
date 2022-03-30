package gargoyle.calendar.util

import gargoyle.calendar.util.convert.DefaultConverters
import kotlin.reflect.KClass

class Configuration internal constructor(private val map: Map<String, String>) {

    operator fun get(key: String): String? = map[key]

    fun <T : Any> getAs(key: String, type: KClass<T>): T? = get(key)?.let { DefaultConverters.convert(type, it) }

    fun getOrDefault(key: String, defaultValue: String): String = get(key) ?: defaultValue

    override fun toString(): String = "Configuration{linkedHashMap=$map}"

}
