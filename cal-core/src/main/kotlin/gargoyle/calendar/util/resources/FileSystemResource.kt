package gargoyle.calendar.util.resources

import java.net.URL
import java.nio.file.Path

class FileSystemResource(path: Path) : ResourceBase(getUrl(path)) {
    companion object {
        private fun getUrl(path: Path): URL = path.toAbsolutePath().toUri().toURL()
    }
}
