package de.eternalwings.awww.command

import org.springframework.stereotype.Component

@Component
class YuukiCommand : SimpleCommand("yuuki") {
    override fun respondTo(args: Sequence<String>): String {
        return "BabyRage"
    }

}
