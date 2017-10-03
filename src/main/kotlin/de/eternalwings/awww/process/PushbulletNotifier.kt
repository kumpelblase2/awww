package de.eternalwings.awww.process

import com.github.sheigutn.pushbullet.Pushbullet
import org.springframework.stereotype.Component

@Component
class PushbulletNotifier(private val pushbullet: Pushbullet) {

    private val channel = pushbullet.getOwnChannel("hafu-updates")

    fun notifyStream(message: String) {
        this.channel.pushLink("Stream is live!", message, "https://twitch.tv/itshafu")
    }
}
