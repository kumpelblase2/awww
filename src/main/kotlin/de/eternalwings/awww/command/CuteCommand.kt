package de.eternalwings.awww.command

import de.eternalwings.awww.TwitchSettings
import de.eternalwings.awww.process.ImageQueue
import de.eternalwings.awww.process.ImageStorage
import org.kitteh.irc.client.library.element.User
import org.springframework.stereotype.Component

@Component
class CuteCommand(private val imageQueue: ImageQueue, private val imageStorage: ImageStorage,
                  private val twitchSettings: TwitchSettings) : SimpleCommand("cutes") {
    override fun respondTo(args: Sequence<String>, user: User): String {
        val firstArgument = args.firstOrNull()
        if (firstArgument == null) {
            return this.imageQueue.next().orElse(
                    "Um... seems like something is pooped. omgFine Please let my creator know by shouting my name. thx.")
        } else {
            if (!this.twitchSettings.trustedUsers.contains(user.nick)) {
                return "Who dis?"
            }

            this.imageStorage.addImage(firstArgument)
            return "Added to list. Will show up sooner or later. omgHype"
        }
    }
}
