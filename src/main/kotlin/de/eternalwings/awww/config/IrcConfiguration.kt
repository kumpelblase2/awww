package de.eternalwings.awww.config

import de.eternalwings.awww.TwitchSettings
import org.kitteh.irc.client.library.Client
import org.kitteh.irc.client.library.feature.twitch.TwitchSupport
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@EnableScheduling
@EnableConfigurationProperties(TwitchSettings::class)
class IrcConfiguration {

    @Autowired
    lateinit var twitchSettings: TwitchSettings

    @Bean(destroyMethod = "shutdown")
    fun ircClient(): Client {
        val serverPassword = toTwitchServerPassword(twitchSettings.appOauthToken)
        val builtClient = Client.builder().nick(twitchSettings.appUsername)
            .server().host(TWITCH_SERVER).port(TWITCH_PORT).password(serverPassword).secure(true).then()
            .build()
        TwitchSupport.addSupport(builtClient, false)

        return builtClient
    }

    companion object {
        private const val TWITCH_SERVER = "irc.chat.twitch.tv"
        private const val TWITCH_PORT = 443

        fun toTwitchServerPassword(token: String) = "oauth:$token"
    }
}
