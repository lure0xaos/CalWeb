package gargoyle.calendar.web.data

internal class ThrowableIterable(private var exception: Throwable?) : Iterable<Throwable> {
    override fun iterator(): Iterator<Throwable> {
        return object : Iterator<Throwable> {
            override fun hasNext(): Boolean {
                return exception != null
            }

            override fun next(): Throwable {
                val current = exception
                exception = exception?.cause
                return current!!
            }
        }
    }
}
