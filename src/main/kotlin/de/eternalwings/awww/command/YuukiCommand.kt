package de.eternalwings.awww.command

import org.kitteh.irc.client.library.element.User
import org.springframework.stereotype.Component

@Component
class YuukiCommand : SimpleCommand("yuuki") {
    override fun respondTo(args: Sequence<String>, user: User): String {
        return "BabyRage"
    }
}
