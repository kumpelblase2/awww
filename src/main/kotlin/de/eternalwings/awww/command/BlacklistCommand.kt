package de.eternalwings.awww.command

import de.eternalwings.awww.process.ImageBlacklist
import de.eternalwings.awww.process.ImageQueue
import org.springframework.stereotype.Component

@Component
class BlacklistCommand(private val imageQueue: ImageQueue, private val imageBlacklist: ImageBlacklist) :
        SimpleCommand("blacklist") {
    override fun respondTo(args: Sequence<String>): String {
        val argList = args.toList()
        val link = if (argList.isNotEmpty()) {
            argList.first()
        } else {
            this.imageQueue.last()
        }
        return link?.let {
            this.imageBlacklist.addToBlacklist(it)
            "Added to blacklist VoHiYo"
        } ?: "No link found FeelsBadMan"
    }

}
