package de.eternalwings.awww.process

import org.kitteh.irc.client.library.Client
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class TwitchManager(private val irc: Client, private val eventListener: TwitchEventListener) {
    init {
        this.irc.setExceptionListener {
            LOGGER.error("Error occurred in IRC.", it)
        }

        this.irc.eventManager.registerEventListener(eventListener)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(TwitchManager::class.java)
    }
}
