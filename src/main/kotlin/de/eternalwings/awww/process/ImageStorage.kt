package de.eternalwings.awww.process

import com.google.gson.Gson
import de.eternalwings.awww.ImgurSettings
import de.eternalwings.awww.ext.randomOrder
import de.eternalwings.awww.http.ImgurApi
import de.eternalwings.awww.http.SubredditApiResponse
import de.eternalwings.awww.http.SubredditImageData
import de.eternalwings.awww.util.SingleFileStorage
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import retrofit2.Call
import java.io.File
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Component
class ImageStorage(imgurSettings: ImgurSettings, gson: Gson, private val imgurApi: ImgurApi,
                   private val imageBlacklist: ImageBlacklist) : SingleFileStorage<Storage>(gson, Storage::class) {
    override val default = { Storage() }
    override val storageFile = File(imgurSettings.cacheLocation, FILE_NAME)

    val images
        get() = this.data.links
    val lastPollTime
        get() = this.data.lastPoll

    init {
        this.load()
    }

    fun addImage(link: String, save: Boolean = true) {
        if (!this.data.links.contains(link) && !this.imageBlacklist.isInBlacklist(link)) {
            this.data.links += link

            if (save) {
                this.save()
            }
        }
    }

    @Scheduled(initialDelay = 0, fixedRate = ONE_MINUTE)
    fun refreshImages() {
        LOGGER.trace("Running image refresh")
        val minLastPollTime = LocalDateTime.now().minus(POLL_DELAY)
        if (this.lastPollTime.isBefore(minLastPollTime)) {
            LOGGER.debug("Polling images...")
            this.pollImages()
        }
    }

    private fun pollImages() {
        val images = imgurApi.getTopImagesFromSubreddit(SUBREDDIT_NAME, 0, "day").getAllFiltered()
        LOGGER.info("Stocked up on new images from last day.")
        this.data.lastPoll = LocalDateTime.now()
        images.map(SubredditImageData::getWorkingLink).forEach { this.addImage(it, false) }
        this.save()
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(ImageStorage::class.java)
        private val POLL_DELAY = Duration.of(1, ChronoUnit.DAYS)
        private const val FILE_NAME = "storage.json"
        private const val SUBREDDIT_NAME = "awww"
        private const val ONE_MINUTE = 60000L
    }
}

data class Storage(var links: Set<String> = emptySet(), var lastPoll: LocalDateTime = LocalDateTime.MIN)

fun Call<SubredditApiResponse>.getAllFiltered(): Collection<SubredditImageData> {
    val response = this.execute()
    val elements = response.body()?.data ?: throw IllegalStateException("Could not extract imgur response")
    return elements.filter(SubredditImageData::isAcceptable).sortedWith(randomOrder())
}
