package gargoyle.calendar.util.load

import gargoyle.calendar.util.resources.Resource
import java.text.MessageFormat
import kotlin.reflect.KClass

open class Loaders {
    private val loaders: MutableMap<KClass<*>, (Resource) -> Any>

    protected constructor() {
        loaders = LinkedHashMap()
    }

    constructor(loaders: Map<KClass<*>, (Resource) -> Any>) {
        this.loaders = LinkedHashMap(loaders)
    }

    protected fun <T : Any> addLoader(targetType: KClass<T>, loader: (Resource) -> T): Loaders {
        require(!canLoad(targetType)) { MessageFormat.format("loader {0} already exists", targetType) }
        loaders[targetType] = loader
        return this
    }

    fun <T : Any> canLoad(targetType: KClass<T>): Boolean = targetType in loaders

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> load(targetType: KClass<T>, value: Resource): T =
        (requireNotNull(loaders[targetType]) { "loader $targetType is not supported yet" } as (Resource) -> T)(value)

    override fun toString(): String = "Loaders{loaders=$loaders}"
}
