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

@Component
class ImageBlacklist(private val gson: Gson, private val imgurSettings: ImgurSettings) {

    private var blacklist: Set<String> = HashSet()
    private val blacklistFile = File(imgurSettings.cacheLocation, "blacklist.json")

    init {
        this.blacklist = this.gson.fromJson(this.createAndGetCacheFile()) ?: emptySet()
        LOGGER.info("Loaded ${blacklist.size} items from blacklist.")
    }

    private fun createAndGetCacheFile(): File {
        if (!this.blacklistFile.createWithParentsIfNotExist()) {
            LOGGER.warn("Cannot create cache dir/file. Using defaults.")
        }
        return this.blacklistFile
    }

    fun addToBlacklist(link: String) {
        blacklist += link
        this.persist()
    }

    private fun persist() {
        this.gson.toJson(this.blacklist, this.createAndGetCacheFile())
        LOGGER.debug { "Saved blacklist to cache." }
    }

    fun isInBlacklist(link: String): Boolean = blacklist.contains(link)

    companion object {
        private val LOGGER = LoggerFactory.getLogger(ImageBlacklist::class.java)
    }
}
