package gargoyle.calendar.util.convert

import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
open class Converters {
    private val converters: MutableMap<Pair<KClass<*>, KClass<*>>, (Any) -> Any>

    protected constructor() {
        converters = LinkedHashMap()
    }

    constructor(converters: Map<Pair<KClass<*>, KClass<*>>, (Any) -> Any>) {
        this.converters = LinkedHashMap(converters)
    }

    protected fun <S : Any, T : Any> declareConverter(
        sourceType: KClass<S>,
        targetType: KClass<T>,
        converter: (S) -> T
    ): Converters {
        require(!canConvert(sourceType, targetType)) { "converter ${sourceType}->${targetType} already exists" }
        converters[sourceType to targetType] = converter as (Any) -> Any
        return this
    }

    fun <S : Any, T : Any> canConvert(sourceType: KClass<S>, targetType: KClass<T>): Boolean {
        return (sourceType to targetType) in converters
    }

    @Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
    fun <S : Any, T : Any, V : S?> convert(targetType: KClass<T>, value: V?): T? {
        return if (value == null) null
        else convert<S, T, V>(value!!::class as KClass<S>, targetType, value)
    }

    fun <S : Any, T : Any, V : S?> convert(sourceType: KClass<S>, targetType: KClass<T>, value: V?): T? =
        when {
            value == null -> null
            sourceType == targetType -> value as T
            else -> (requireNotNull(converters[sourceType to targetType])
            { "converter ${sourceType}->${targetType} is not supported yet" } as (S) -> T)(value)
        }
}
