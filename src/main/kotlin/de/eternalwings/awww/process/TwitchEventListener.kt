package de.eternalwings.awww.process

import de.eternalwings.awww.TwitchSettings
import de.eternalwings.awww.ext.debug
import de.eternalwings.awww.ext.info
import net.engio.mbassy.listener.Handler
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent
import org.kitteh.irc.client.library.event.channel.RequestedChannelJoinCompleteEvent
import org.kitteh.irc.client.library.event.client.ClientConnectedEvent
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class TwitchEventListener(private val twitchSettings: TwitchSettings, private val commandRegistry: CommandRegistry) {
    @Handler
    fun onChannelMessage(messageEvent: ChannelMessageEvent) {
        LOGGER.debug { "${messageEvent.channel.name}\t| Received message: (${messageEvent.actor.nick}) ${messageEvent.message}" }
        val response = this.commandRegistry.handleMessage(messageEvent.message, messageEvent.actor)
        if (response != null) {
            LOGGER.debug { "Sending(${messageEvent.channel.name}): $response" }
            if (messageEvent.actor.nick == twitchSettings.appUsername && messageEvent.channel.name != "#" + twitchSettings.appUsername) {
                LOGGER.debug { "Adding an additional sleep because the same user is used as response." }
                Thread.sleep(2 * 1000)
            }
            messageEvent.client.sendMessage(messageEvent.channel, response)
        }
    }

    @Handler
    fun onConnected(connectedEvent: ClientConnectedEvent) {
        Thread.currentThread().name = THREAD_NAME

        LOGGER.debug { "Connected to Twitch." }
        LOGGER.info { "Joining initial channels: ${twitchSettings.initialChannels.joinToString(",")}" }
        twitchSettings.initialChannels.map { asChannelName(it) }.forEach { connectedEvent.client.addChannel(it) }
    }

    @Handler
    fun onChannelJoined(channelJoinCompleteEvent: RequestedChannelJoinCompleteEvent) {
        LOGGER.debug { "Joined channel ${channelJoinCompleteEvent.channel.name}" }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(TwitchEventListener::class.java)
        private val THREAD_NAME = "IRC Events"

        fun asChannelName(channelName: String) = "#" + channelName
    }
}
