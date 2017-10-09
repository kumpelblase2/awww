package de.eternalwings.awww.process

import de.eternalwings.awww.TwitterSettings
import de.eternalwings.awww.ext.debug
import de.eternalwings.awww.ext.trace
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import twitter4j.FilterQuery
import twitter4j.Status
import twitter4j.TwitterStream
import java.lang.Exception
import javax.annotation.PostConstruct

@Component
class TwitterListener(private val twitterStream: TwitterStream, private val streamNotifier: StreamNotifier,
                      private val twitterSettings: TwitterSettings) {

    @PostConstruct
    fun init() {
        twitterStream.onStatus(this::onStatus)
        twitterStream.onException(this::onException)
        LOGGER.debug("Registered twitter listeners")
        twitterStream.filter(FilterQuery(twitterSettings.userId))
    }

    fun onException(ex: Exception) {
        LOGGER.error("Error in twitter stream.", ex)
    }

    fun onStatus(status: Status) {
        LOGGER.debug { "Received tweet: " + status.text }

        if (status.user.id != twitterSettings.userId) {
            LOGGER.trace { "Status from other user: ${status.user.name} - ${status.text}" }
            return
        }

        if (status.isAnnouncementTweet()) {
            LOGGER.debug { "Notifying subscribers, she's going live." }
            //notifier.notifyStream(status.text)
            streamNotifier.notifyStreamLive(status.text)
        } else if (status.isNormalTweet()) {
            LOGGER.debug { "Just a normal tweet, broadcasting in chat." }
            // not sure, maybe do something ...
            //.sendMessage("", "Twitter - ${status.user.name}: ${status.text}")
            streamNotifier.notifyStatusUpdate(status.text)
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(TwitterListener::class.java)
    }
}

fun Status.isAnnouncementTweet(): Boolean {
    return this.isNormalTweet() && this.urlEntities.any {
        it.expandedURL.contains("twitch.tv/itshafu")
    } && this.quotedStatusId < 0
}

fun Status.isReply(): Boolean {
    return this.inReplyToUserId >= 0 || this.inReplyToStatusId >= 0
}

fun Status.isNormalTweet() = !this.isRetweet && !this.isReply()
