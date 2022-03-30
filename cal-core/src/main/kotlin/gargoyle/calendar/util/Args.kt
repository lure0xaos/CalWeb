package gargoyle.calendar.util

import kotlin.reflect.KClass

object Args {
    fun <C : Any> parseArgs(configClass: KClass<C>, args: Array<String>): C =
        BeanModel.ofType(configClass).load(parseArgs(args))

    fun parseArgs(args: Array<String>): Map<String, String> =
        args
            .map { it.split("=".toRegex(), 2).toTypedArray() }
            .filter { it.size > 1 }
            .associateTo(LinkedHashMap(args.size)) { it[0].trim() to it[1].trim() }

    fun parseArgs(keys: Array<String>, args: Array<String>): Map<String, String> {
        require(keys.size >= args.size) { "no matched args name" }
        return (args.indices).associateTo(LinkedHashMap(keys.size)) { keys[it] to args[it] }
    }
}
