package gargoyle.calendar.web.data

data class ExceptionInfo(val message: String, val details: String) {
    constructor(exception: Throwable) : this(
        message =
        splitWords(exception::class.simpleName?.substringBeforeLast(TAIL) ?: TAIL, SplitWordsOptions.FIRST_UPPER),
        details =
        generateSequence(exception) { it.cause }.map { it.localizedMessage ?: "" }.firstOrNull { it.isNotBlank() } ?: ""
    )

    companion object {
        private const val TAIL = "Exception"
        fun splitWords(text: String, options: SplitWordsOptions): String {
            var result = ""
            var word: Boolean
            text.forEachIndexed { i, c ->
                if (!c.isLetterOrDigit()) {
                    if (i != 0) result += ' '
                    return@forEachIndexed
                } else {
                    if (c.isUpperCase()) {
                        word = true
                        if (i != 0) result += ' '
                    } else {
                        word = false
                    }
                }
                result += when (options) {
                    SplitWordsOptions.FIRST_UPPER_EVERY -> if (word) c.uppercaseChar() else c.lowercaseChar()
                    SplitWordsOptions.FIRST_UPPER -> if (word && i == 0) c.uppercaseChar() else c.lowercaseChar()
                    SplitWordsOptions.ALL_LOWER -> c.lowercaseChar()
                    SplitWordsOptions.ALL_UPPER -> c.uppercaseChar()
                }
            }
            return result
        }

        enum class SplitWordsOptions {
            ALL_UPPER, ALL_LOWER, FIRST_UPPER, FIRST_UPPER_EVERY
        }
    }
}
