package de.eternalwings.awww.process

import org.kitteh.irc.client.library.Client
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class TwitchManager(irc: Client, eventListener: TwitchEventListener) {
    init {
        irc.setExceptionListener {
            LOGGER.error("Error occurred in IRC.", it)
        }

        LOGGER.info("Setting up twitch handling...")
        irc.eventManager.registerEventListener(eventListener)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(TwitchManager::class.java)
    }
}
