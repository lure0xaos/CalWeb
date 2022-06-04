package gargoyle.calendar.util.audio

import java.net.URL
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip

internal class AudioClipImpl(val resource: URL) : AudioClip {

    private val clip: Clip = AudioSystem.getClip()

    init {
        clip.open(AudioSystem.getAudioInputStream(resource))
    }

    override fun play(): Unit = clip.loop(1)
    override fun loop(): Unit = clip.loop(Clip.LOOP_CONTINUOUSLY)
    override fun stop(): Unit = clip.stop()

    override fun close(): Unit = clip.close()
}
