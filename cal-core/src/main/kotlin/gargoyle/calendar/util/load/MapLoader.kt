package gargoyle.calendar.util.load

import gargoyle.calendar.util.resources.Resource
import java.util.Properties

object MapLoader {
    fun loadMap(resource: Resource): Map<String, String> =
        Properties().also { resource.inputStream.use { stream -> it.load(stream) } }
            .map { (key, value) -> key.toString() to value.toString() }
            .associateTo(LinkedHashMap()) { it }
}
