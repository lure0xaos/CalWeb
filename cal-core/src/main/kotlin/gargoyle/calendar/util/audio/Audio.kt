package gargoyle.calendar.util.audio

import java.net.URL

object Audio {

    fun newAudioClip(resource: URL): AudioClip = AudioClipImpl(resource)
}
