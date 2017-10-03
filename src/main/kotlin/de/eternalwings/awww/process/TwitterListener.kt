package de.eternalwings.awww.process

import de.eternalwings.awww.ext.debug
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import twitter4j.Status
import twitter4j.TwitterStream
import java.lang.Exception
import javax.annotation.PostConstruct

@Component
class TwitterListener(private val twitterStream: TwitterStream, private val notifier: PushbulletNotifier) {

    @PostConstruct
    fun init() {
        twitterStream.onStatus(this::onStatus)
        twitterStream.onException(this::onException)
    }

    fun onException(ex: Exception) {
        LOGGER.error("Error in twitter stream.", ex)
    }

    fun onStatus(status: Status) {
        LOGGER.debug { "Received tweet: " + status.text }
        if (status.isAnnouncementTweet()) {
            LOGGER.debug { "Notifying subscribers, she's going live." }
            notifier.notifyStream(status.text)
        } else if (status.isNormalTweet()) {
            LOGGER.debug { "Just a normal tweet, broadcasting in chat." }
            // not sure, maybe do something ...
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(TwitterListener::class.java)
    }
}

fun Status.isAnnouncementTweet(): Boolean {
    return this.isNormalTweet() && this.urlEntities.any { it.url.contains("twitch.tv/itshafu") }
}

fun Status.isNormalTweet() = !this.isRetweet
