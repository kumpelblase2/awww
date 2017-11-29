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
        if (status.user.id != twitterSettings.userId) {
            LOGGER.trace { "Status from other user: ${status.user.name} - ${status.text}" }
            return
        }

        LOGGER.debug { "Received tweet: " + status.text }

        if (status.isAnnouncementTweet()) {
            LOGGER.debug { "Notifying subscribers, she's going live." }
            streamNotifier.notifyStreamLive(status.text)

            if (status.mediaEntities.isNotEmpty()) {
                LOGGER.debug { "Announcement tweet, but has media entries" }
                streamNotifier.notifyStatusUpdate(status.text, status.getSourceLink())
            }
        } else if (status.isNormalTweet()) {
            LOGGER.debug { "Just a normal tweet, broadcasting in chat." }
            streamNotifier.notifyStatusUpdate(status.text, status.getSourceLink())
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

// As far as I can tell, there's no nice way to check if a tweet would end up on the timeline of the followers.
// So this tries to filter all retweets and replys out.
fun Status.isNormalTweet() = !this.isRetweet && !this.isReply()

fun Status.getSourceLink() = "https://twitter.com/${this.user.screenName}/status/${this.id}"
