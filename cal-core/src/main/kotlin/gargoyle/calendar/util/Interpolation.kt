package gargoyle.calendar.util

object Interpolation {
    fun interpolate(text: String, params: Map<String, Any>): String {
        var ret = text
        params.forEach { (key, value) ->
            ret = ret.replace("{$key}", value.toString())
        }
        return ret
    }

    fun cutTail(text: String, tail: String): String =
        text.substringBeforeLast(tail)

    fun findFirstNotEmpty(vararg texts: String?): String =
        texts.firstOrNull { it != null && it.isNotBlank() } ?: ""

    fun <T> findFirstNotEmpty(iterable: Iterable<T>, formatter: (T) -> String): String =
        iterable.map { formatter(it) }.firstOrNull { it.isNotBlank() } ?: ""

    fun splitWords(text: String, options: SplitWordsOptions): String {
        val result = StringBuilder()
        var word: Boolean
        for (c in text.toCharArray()) {
            val begin = result.isEmpty()
            if (!Character.isLetterOrDigit(c)) {
                if (!begin) result.append(' ')
                continue
            } else {
                if (Character.isUpperCase(c)) {
                    word = true
                    if (!begin) result.append(' ')
                } else {
                    word = false
                }
            }
            when (options) {
                SplitWordsOptions.FIRST_UPPER_EVERY -> if (word) {
                    result.append(c.uppercaseChar())
                } else {
                    result.append(c.lowercaseChar())
                }
                SplitWordsOptions.FIRST_UPPER -> if (word && begin) {
                    result.append(c.uppercaseChar())
                } else {
                    result.append(c.lowercaseChar())
                }
                SplitWordsOptions.ALL_LOWER -> result.append(c.lowercaseChar())
                SplitWordsOptions.ALL_UPPER -> result.append(c.uppercaseChar())
            }
        }
        return result.toString()
    }

    enum class SplitWordsOptions {
        ALL_UPPER, ALL_LOWER, FIRST_UPPER, FIRST_UPPER_EVERY
    }
}
