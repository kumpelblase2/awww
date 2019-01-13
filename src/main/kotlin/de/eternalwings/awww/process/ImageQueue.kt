package de.eternalwings.awww.process

import com.google.gson.Gson
import de.eternalwings.awww.ImgurSettings
import de.eternalwings.awww.ext.createWithParentsIfNotExist
import de.eternalwings.awww.ext.debug
import de.eternalwings.awww.ext.fromJson
import de.eternalwings.awww.ext.toJson
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileNotFoundException
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.ArrayDeque
import java.util.Optional
import java.util.Queue

@Component
class ImageQueue(imgurSettings: ImgurSettings, private val gson: Gson, private val imageStorage: ImageStorage) {
    private var usages: Map<String, LocalDateTime> = HashMap()
    private val queue: Queue<String> = ArrayDeque(10)
    private val cacheFile = File(imgurSettings.cacheLocation, CACHEFILE_NAME)

    init {
        try {
            val fromJson = this.gson.fromJson<Array<ImageUsage>?>(this.createAndGetCacheFile()) ?: emptyArray()
            usages = fromJson.associate { it.link to it.time }
        } catch (e: FileNotFoundException) {
            LOGGER.info("No images used yet.")
        }
    }

    private fun createAndGetCacheFile(): File {
        if (!this.cacheFile.createWithParentsIfNotExist()) {
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
        if (queue.isEmpty()) {
            this.fillQueue()
            if (queue.isEmpty()) {
                LOGGER.error("No new entries after polling, something is wrong.")
                return Optional.empty()
            }
        }

        val link = queue.poll()
        LOGGER.debug { "Using next item in queue: $link" }

        usages += link to LocalDateTime.now()
        this.persist()
        return Optional.of(link)
    }

    private fun fillQueue() {
        LOGGER.info("Refilling queue because it's empty.")
        val sortedByTimeImages = this.imageStorage.images.sortedBy { usages.getOrDefault(it, UNUSED_TIME) }
        val possibleSize = Math.min(sortedByTimeImages.size, DEFAULT_QUEUE_SIZE)
        this.queue.offerAll(sortedByTimeImages.take(possibleSize))
        LOGGER.debug("New queue size is ${this.queue.size}.")
    }

    fun last() = this.usages.maxBy { it.value.toEpochSecond(ZoneOffset.UTC) }?.key

    companion object {
        private val LOGGER = LoggerFactory.getLogger(ImageQueue::class.java)
        private val UNUSED_TIME = LocalDateTime.MIN
        private const val CACHEFILE_NAME = "imageCache.json"
        private const val DEFAULT_QUEUE_SIZE = 10
    }
}

data class ImageUsage(var link: String, var time: LocalDateTime)

fun <T> Queue<T>.offerAll(items: Iterable<T>) {
    items.forEach { this.offer(it) }
}
