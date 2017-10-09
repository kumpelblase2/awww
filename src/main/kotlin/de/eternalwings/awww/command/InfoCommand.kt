package de.eternalwings.awww.command

import org.kitteh.irc.client.library.element.User
import org.springframework.stereotype.Component

@Component
class InfoCommand : SimpleCommand("kumpelbot") {
    override fun respondTo(args: Sequence<String>, user: User): String {
        return "VoHiYo I are ze bot of teh cutez omgAion 7"
    }
}
