package gargoyle.calendar.util.resources

import java.io.InputStream
import java.io.OutputStream
import java.net.URL

interface Resource {
    fun exists(): Boolean
    val inputStream: InputStream
    val outputStream: OutputStream
    val url: URL
}
