package gargoyle.calendar.web.data

import gargoyle.calendar.util.Interpolation
import gargoyle.calendar.util.Interpolation.cutTail
import gargoyle.calendar.util.Interpolation.findFirstNotEmpty
import gargoyle.calendar.util.Interpolation.splitWords

class ExceptionInfo(exception: Exception) {
    val message: String
    val details: String

    init {
        message = splitWords(
            cutTail(exception.javaClass.simpleName, TAIL),
            Interpolation.SplitWordsOptions.FIRST_UPPER
        )
        details = findFirstNotEmpty(ThrowableIterable(exception)) { obj: Throwable -> obj.localizedMessage ?: "" }
    }

    override fun equals(other: Any?): Boolean =
        when {
            other === this -> true
            other !is ExceptionInfo -> false
            message != other.message -> false
            else -> details == other.details
        }

    override fun hashCode(): Int = (1 * 59 + message.hashCode()) * 59 + details.hashCode()

    override fun toString(): String = "ExceptionInfo(message=$message, details=$details)"

    companion object {
        private const val TAIL = "Exception"
    }
}
