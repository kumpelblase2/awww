package de.eternalwings.awww.command

import de.eternalwings.awww.TwitchSettings
import de.eternalwings.awww.process.ImageBlacklist
import de.eternalwings.awww.process.ImageQueue
import org.kitteh.irc.client.library.element.User
import org.springframework.stereotype.Component

@Component
class BlacklistCommand(private val imageQueue: ImageQueue, private val imageBlacklist: ImageBlacklist,
                       private val twitchSettings: TwitchSettings) : SimpleCommand("blacklist") {
    override fun respondTo(args: Sequence<String>, user: User): String {
        if (!twitchSettings.trustedUsers.contains(user.nick)) {
            return "Who dis?"
        }

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
