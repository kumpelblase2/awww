package de.eternalwings.awww.process

import com.github.sheigutn.pushbullet.Pushbullet
import com.github.sheigutn.pushbullet.items.channel.OwnChannel
import de.eternalwings.awww.StreamUpdateSettings
import org.kitteh.irc.client.library.Client
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class StreamNotifier(private val pushbullet: Pushbullet, private val ircClient: Client,
                     private val streamUpdateSettings: StreamUpdateSettings) {

    lateinit var pushbulletChannel: OwnChannel

    @PostConstruct
    fun init() {
        this.pushbulletChannel = pushbullet.getOwnChannel(streamUpdateSettings.pushbulletChannel)
    }

    fun notifyStreamLive(message: String) {
        this.pushbulletChannel.pushLink("Stream is live!", message,
                "https://twitch.tv/" + streamUpdateSettings.twitchChannel)
    }

    fun notifyStatusUpdate(message: String) {
        ircClient.sendMessage("#" + streamUpdateSettings.twitchChannel, "Tweet: " + message)
    }
}
