package de.eternalwings.awww.config

import org.kitteh.irc.client.library.Client
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component

@Component
class TwitchConnector(private val ircClient: Client) : ApplicationListener<ContextRefreshedEvent> {
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        ircClient.connect()
    }
}
