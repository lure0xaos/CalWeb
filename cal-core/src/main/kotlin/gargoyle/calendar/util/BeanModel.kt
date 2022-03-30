package gargoyle.calendar.util

import gargoyle.calendar.util.convert.DefaultConverters
import gargoyle.calendar.util.load.MapLoader
import gargoyle.calendar.util.resources.Resource
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.jvmErasure

class BeanModel<B : Any> private constructor(private val type: KClass<B>) {
    private var beanInfo: List<KMutableProperty<*>> = type.memberProperties
        .filter { it is KMutableProperty<*> }.map { it as KMutableProperty<*> }

    fun load(resource: Resource): B = fill(Reflections.newInstance(type), MapLoader.loadMap(resource))

    fun load(map: Map<String, String>): B = fill(Reflections.newInstance(type), map)

    fun fill(bean: B, resource: Resource): B = fill(bean, MapLoader.loadMap(resource))

    fun fill(bean: B, map: Map<String, String>): B =
        bean.also { forEachProperty { readWrite(map, bean, it) } }

    private fun readWrite(map: Map<String, String>, bean: B, property: KMutableProperty<*>) =
        writeProperty(
            bean, property, DefaultConverters.convert(
                String::class,
                property.returnType.jvmErasure,
                map[property.name]
            )!!
        )

    private fun forEachProperty(action: (KMutableProperty<*>) -> Unit) =
        beanInfo.filter { "class" != it.name }.forEach(action)

    fun findProperty(name: String): KMutableProperty<*>? = findProperty { it.name == name }

    fun findProperty(predicate: (KMutableProperty<*>) -> Boolean): KMutableProperty<*>? =
        beanInfo.firstOrNull(predicate)

    fun <T : Any> readProperty(bean: B, property: KMutableProperty<T>): T = property.getter.call(bean)

    fun <T : Any> writeProperty(bean: B, property: KMutableProperty<*>, value: T): Unit =
        property.setter.call(bean, value)

    override fun toString(): String = "BeanModel{type=$type}"

    companion object {
        fun <B : Any> ofType(type: KClass<B>): BeanModel<B> = BeanModel(type)

        fun <B : Any> load(type: KClass<B>, resource: Resource): B =
            ofType(type).fill(Reflections.newInstance(type), MapLoader.loadMap(resource))

        fun <B : Any> load(type: KClass<B>, map: Map<String, String>): B =
            ofType(type).fill(Reflections.newInstance(type), map)
    }
}
