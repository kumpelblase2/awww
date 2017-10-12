package de.eternalwings.awww.command

import de.eternalwings.awww.TwitchSettings
import org.kitteh.irc.client.library.element.User
import org.springframework.stereotype.Component

@Component
class ShutdownCommand(private val twitchSettings: TwitchSettings) : SimpleCommand("denycutes") {
    override fun respondTo(args: Sequence<String>, user: User): String {
        if (twitchSettings.admins.contains(user.nick)) {
            System.exit(0)
        }

        return ""
    }
}
