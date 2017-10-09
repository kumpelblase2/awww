package de.eternalwings.awww.command

import org.kitteh.irc.client.library.element.User

interface Command {
    val priority: Int

    fun appliesTo(message: String): Boolean

    fun responseTo(message: String, user: User): String
}

abstract class SimpleCommand(private val commandName: String, override val priority: Int = 1) : Command {

    override fun appliesTo(message: String): Boolean {
        if (message.isEmpty()) {
            return false
        }

        val args = toArgumentArray(message)
        val command = args.first().substring(1)

        return command.equals(this.commandName, true)
    }

    override fun responseTo(message: String, user: User): String {
        val args = toArgumentArray(message)
        return this.respondTo(args.drop(1), user)
    }

    abstract fun respondTo(args: Sequence<String>, user: User): String

    companion object {
        fun toArgumentArray(message: String) = message.splitToSequence(" ")
    }
}
