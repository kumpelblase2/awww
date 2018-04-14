package de.eternalwings.awww.process

import de.eternalwings.awww.TwitchSettings
import de.eternalwings.awww.ext.debug
import de.eternalwings.awww.ext.info
import net.engio.mbassy.listener.Handler
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent
import org.kitteh.irc.client.library.event.channel.RequestedChannelJoinCompleteEvent
import org.kitteh.irc.client.library.event.client.ClientConnectionEstablishedEvent
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Component
class TwitchEventListener(private val twitchSettings: TwitchSettings, private val commandRegistry: CommandRegistry) {
    private var durationStart = LocalDateTime.MIN
    private var commands = 0

    @Handler
    fun onChannelMessage(messageEvent: ChannelMessageEvent) {
        LOGGER.debug { "${messageEvent.channel.name}\t| Received message: (${messageEvent.actor.nick}) ${messageEvent.message}" }
        if (this.atLimit()) {
            LOGGER.info { "Not handling command because limited." }
        }

        val response = this.commandRegistry.handleMessage(messageEvent.message, messageEvent.actor)
        if (response != null) {
            this.increaseLimit()
            LOGGER.debug { "Sending(${messageEvent.channel.name}): $response" }
            if (messageEvent.actor.nick == twitchSettings.appUsername && messageEvent.channel.name != "#" + twitchSettings.appUsername) {
                LOGGER.debug { "Adding an additional sleep because the same user is used as response." }
                Thread.sleep(2 * 1000)
            }
            messageEvent.client.sendMessage(messageEvent.channel, response)
        }
    }

    private fun increaseLimit() {
        if (!this.isInsideLimitingDuration()) {
            LOGGER.debug { "Starting new limiting period." }
            this.durationStart = LocalDateTime.now()
            this.commands = 0
        }

        this.commands += 1
    }

    private fun isInsideLimitingDuration(): Boolean {
        val currentTime = LocalDateTime.now()
        return currentTime.minus(LIMIT_DURATION).isBefore(this.durationStart)
    }

    private fun atLimit(): Boolean {
        if (this.isInsideLimitingDuration()) {
            return this.commands >= this.twitchSettings.messageLimit
        } else {
            return false
        }
    }

    @Handler
    fun onConnected(connectedEvent: ClientConnectionEstablishedEvent) {
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
        private val LIMIT_DURATION = Duration.of(1, ChronoUnit.MINUTES)

        fun asChannelName(channelName: String) = "#" + channelName
    }
}
