package de.eternalwings.awww.process

import com.google.gson.Gson
import de.eternalwings.awww.ImgurSettings
import de.eternalwings.awww.util.SingleFileStorage
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.File

@Component
class ImageBlacklist(gson: Gson, imgurSettings: ImgurSettings) : SingleFileStorage<Blacklist>(gson, Blacklist::class, "[]") {
    override val default = { Blacklist() }
    override val storageFile = File(imgurSettings.cacheLocation, "blacklist.json")

    init {
        this.load()
        LOGGER.info("Loaded ${data.size} items from blacklist.")
    }

    fun addToBlacklist(link: String) {
        this.data.add(link)
        this.save()
    }

    fun isInBlacklist(link: String): Boolean = this.data.contains(link)

    companion object {
        private val LOGGER = LoggerFactory.getLogger(ImageBlacklist::class.java)
    }
}

class Blacklist : HashSet<String>()
