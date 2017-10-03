package de.eternalwings.awww.process

import com.google.gson.Gson
import de.eternalwings.awww.ImgurSettings
import de.eternalwings.awww.ext.createWithParentsIfNotExist
import de.eternalwings.awww.ext.debug
import de.eternalwings.awww.ext.fromJson
import de.eternalwings.awww.ext.randomOrder
import de.eternalwings.awww.ext.toJson
import de.eternalwings.awww.http.ImgurApi
import de.eternalwings.awww.http.SubredditApiResponse
import de.eternalwings.awww.http.SubredditImageData
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import retrofit2.Call
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.FileWriter
import java.nio.file.Files
import java.time.LocalDateTime
import java.util.Optional
import java.util.PriorityQueue
import java.util.Queue
import javax.annotation.PostConstruct

@Component
class ImageQueue(private val imgurApi: ImgurApi, private val imgurSettings: ImgurSettings, private val gson: Gson, private val imageBlacklist: ImageBlacklist) {

    private var usages: Map<String, LocalDateTime> = HashMap()
    private val queue: Queue<String> = PriorityQueue(this::compare)
    private var lastStockup: LocalDateTime = LocalDateTime.MIN
    private val cacheFile = File(imgurSettings.cacheLocation, CACHEFILE_NAME)

    init {
        try {
            val fromJson = this.gson.fromJson<Array<ImageUsage>?>(this.createAndGetCacheFile()) ?: emptyArray()
            usages = fromJson.associate { it.link to it.time }
            queue.addAll(usages.keys)
        } catch (e: FileNotFoundException) {
            LOGGER.info("No images used yet.")
        }
    }

    private fun createAndGetCacheFile(): File {
        if(!this.cacheFile.createWithParentsIfNotExist()) {
            LOGGER.warn("Cannot create cache dir/file. Using defaults.")
        }
        return this.cacheFile
    }

    private fun persist() {
        val jsonList = this.usages.map { ImageUsage(it.key, it.value) }
        this.gson.toJson(jsonList, this.createAndGetCacheFile())
        LOGGER.debug { "Saved link usages to cache." }
    }

    fun next(): Optional<String> {
        if (queue.isEmpty() || this.shouldStockUp()) {
            this.stockUp()
            if (queue.isEmpty()) {
                LOGGER.error("No new entries after polling, something is wrong.")
                return Optional.empty()
            }
        }

        var link: String
        do {
            link = queue.poll()
            LOGGER.debug { "Using next item in queue: $link" }
        } while (imageBlacklist.isInBlacklist(link))

        usages += link to LocalDateTime.now()
        queue.add(link)
        this.persist()
        return Optional.of(link)
    }

    private fun shouldStockUp(): Boolean {
        return this.queue.size < this.usages.size || this.lastStockup.isAWeekOld()
    }

    private fun stockUp() {
        val images = (0..2).flatMap { imgurApi.getTopImagesFromSubreddit(SUBREDDIT_NAME, it).getAllFiltered() }
        LOGGER.info("Stocked up on new images from last week.")
        this.lastStockup = LocalDateTime.now()
        this.queue.addAll(images.map(SubredditImageData::getWorkingLink).filterNot(this.usages::containsKey))
    }

    private fun compare(link1: String, link2: String): Int {
        val firstUsage = usages.getOrDefault(link1, UNUSED_TIME)
        val secondUsage = usages.getOrDefault(link2, UNUSED_TIME)

        return firstUsage.compareTo(secondUsage)
    }

    fun last() = this.queue.lastOrNull()

    companion object {
        private val LOGGER = LoggerFactory.getLogger(ImageQueue::class.java)
        private val UNUSED_TIME = LocalDateTime.MIN
        private val SUBREDDIT_NAME = "aww"
        private val CACHEFILE_NAME = "imageCache.json"
    }
}

data class ImageUsage(var link: String, var time: LocalDateTime)

fun Call<SubredditApiResponse>.getAllFiltered(): Collection<SubredditImageData> {
    val response = this.execute()
    val elements = response.body()?.data ?: throw IllegalStateException("Could not extract imgur response")
    return elements.filter(SubredditImageData::isAcceptable).sortedWith(randomOrder())
}

fun LocalDateTime.isAWeekOld(): Boolean {
    return this.isBefore(LocalDateTime.now().minusWeeks(1))
}
