package gargoyle.calendar.util.resources

import java.io.OutputStream
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import kotlin.reflect.KClass

object Resources {
    const val PROTOCOL_FILE: String = "file"

    fun getResource(path: Path): Resource =
        FileSystemResource(path)

    fun getResource(location: String, type: KClass<*>): Resource =
        ClasspathResource(location, type)

    fun getResource(location: String, loader: ClassLoader): Resource =
        ClasspathResource(location, loader)

    fun getResource(url: URL): Resource =
        UrlResource(url)

    fun getURLStream(url: URL): OutputStream =
        url.openConnection().getOutputStream()

    fun getFileStream(url: URL): OutputStream =
        Files.newOutputStream(
            Paths.get(url.toExternalForm().substringAfter("$PROTOCOL_FILE:/")),
            StandardOpenOption.CREATE, StandardOpenOption.WRITE
        )

    fun getUrl(location: String, loader: ClassLoader): URL =
        requireNotNull(loader.getResource(location)) { "no resource $location" }

    fun getUrl(location: String, type: KClass<*>): URL =
        requireNotNull(type.java.getResource(location) ?: type.java.getResource("/$location"))
        { "no resource $location" }
}
