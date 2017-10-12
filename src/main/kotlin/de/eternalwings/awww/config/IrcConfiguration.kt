package de.eternalwings.awww.config

import de.eternalwings.awww.TwitchSettings
import org.kitteh.irc.client.library.Client
import org.kitteh.irc.client.library.feature.twitch.TwitchDelaySender
import org.kitteh.irc.client.library.feature.twitch.TwitchListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import java.util.function.Consumer

@Configuration
@EnableScheduling
@EnableConfigurationProperties(TwitchSettings::class)
class IrcConfiguration {

    @Autowired lateinit var twitchSettings: TwitchSettings

    @Bean(destroyMethod = "shutdown")
    fun ircClient(): Client {
        val serverPassword = toTwitchServerPassword(twitchSettings.appOauthToken)
        val registerCustomEventListener = Consumer<Client> {
            it.eventManager.registerEventListener(createEventListener(it))
        }

        return Client.builder().nick(twitchSettings.appUsername).serverHost(TWITCH_SERVER).serverPort(TWITCH_PORT).serverPassword(
                serverPassword).messageSendingQueueSupplier(TwitchDelaySender.getSupplier(false)).afterBuildConsumer(
                registerCustomEventListener).build()
    }

    private fun createEventListener(client: Client) = TwitchListener(client)

    companion object {
        private val TWITCH_SERVER = "irc.chat.twitch.tv"
        private val TWITCH_PORT = 443

        fun toTwitchServerPassword(token: String) = "oauth:" + token
    }
}
