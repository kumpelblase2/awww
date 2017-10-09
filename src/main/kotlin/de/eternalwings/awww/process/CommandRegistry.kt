package de.eternalwings.awww.process

import de.eternalwings.awww.command.Command
import de.eternalwings.awww.ext.debug
import org.kitteh.irc.client.library.element.User
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class CommandRegistry(private val commands: MutableList<Command>) {

    init {
        this.commands.sortedBy { it.priority }
    }

    fun handleMessage(message: String, user: User): String? {
        if (!message.startsWith(COMMAND_PREFIX)) {
            return null
        } else {
            val command = commands.firstOrNull { it.appliesTo(message) } ?: return null
            LOGGER.debug { "Let plugin ${command.javaClass.simpleName} handle message." }
            return command.responseTo(message, user)
        }
    }

    companion object {
        val LOGGER = LoggerFactory.getLogger(CommandRegistry::class.java)
        val COMMAND_PREFIX = "!"
    }
}
