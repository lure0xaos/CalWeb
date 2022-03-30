package gargoyle.calendar.util

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.memberFunctions

object Reflections {
    fun <B : Any> newInstance(configClass: KClass<B>): B =
        configClass.constructors.first { it.parameters.isEmpty() }.call()

    fun getMethod(type: KClass<*>, methodName: String): KFunction<*> =
        type.memberFunctions.first { it.name == methodName }
}
