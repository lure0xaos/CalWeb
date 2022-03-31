package gargoyle.calendar.util.resources

import java.io.InputStream
import java.io.OutputStream
import java.net.URL

abstract class ResourceBase protected constructor(override val url: URL) : Resource {

    override fun exists(): Boolean =
        runCatching { url.openStream().use { return true } }.isSuccess

    override val inputStream: InputStream
        get() =
            url.openConnection().getInputStream()

    override val outputStream: OutputStream
        get() =
            when (this.url.protocol) {
                Resources.PROTOCOL_FILE -> Resources.getFileStream(this.url)
                else -> Resources.getURLStream(this.url)
            }


    override fun toString(): String = "${javaClass.name}{url=$url}"
}
