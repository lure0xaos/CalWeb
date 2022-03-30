package gargoyle.calendar.util.resources

import java.net.URL
import kotlin.reflect.KClass

class ClasspathResource : ResourceBase {
    constructor(location: String, classLoader: ClassLoader)
            : super(Resources.getUrl(location, classLoader))

    constructor(location: String, type: KClass<*>) : super(Resources.getUrl(location, type))
    constructor(location: URL) : super(location)
}
