package de.eternalwings.awww.command

interface Command {
    val priority: Int

    fun appliesTo(message: String): Boolean

    fun responseTo(message: String): String
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

    override fun responseTo(message: String): String {
        val args = toArgumentArray(message)
        return this.respondTo(args.drop(1))
    }

    abstract fun respondTo(args: Sequence<String>): String

    companion object {
        fun toArgumentArray(message: String) = message.splitToSequence(" ")
    }
}
