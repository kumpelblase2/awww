package de.eternalwings.awww.command

import de.eternalwings.awww.process.ImageQueue
import org.springframework.stereotype.Component

@Component
class CuteCommand(private val imageQueue: ImageQueue) : SimpleCommand("cutes") {
    override fun respondTo(args: Sequence<String>): String {
        return this.imageQueue.next().orElse("Um... seems like something is pooped. omgFine Please let my creator know by shouting my name. thx.")
    }
}
