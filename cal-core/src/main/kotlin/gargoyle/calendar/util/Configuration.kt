package gargoyle.calendar.util

class Configuration internal constructor(private val map: Map<String, String>) {

    operator fun get(key: String): String? = map[key]

    fun getOrDefault(key: String, defaultValue: String): String = get(key) ?: defaultValue

    override fun toString(): String = "Configuration{linkedHashMap=$map}"

}
