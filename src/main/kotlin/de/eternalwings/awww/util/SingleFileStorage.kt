package de.eternalwings.awww.util

import com.google.gson.Gson
import de.eternalwings.awww.ext.createWithParentsIfNotExist
import de.eternalwings.awww.ext.fromJsonType
import de.eternalwings.awww.ext.toJson
import java.io.File
import kotlin.reflect.KClass

abstract class SingleFileStorage<T : Any>(protected val gson: Gson, private val type: KClass<T>, private val initialContents: String = "") {
    abstract val storageFile: File

    abstract val default: () -> T
    lateinit protected var data: T

    private fun getAndCreateFile(): File {
        if (!this.storageFile.createWithParentsIfNotExist(this.initialContents)) {
            throw IllegalStateException("Unable to create file for storage.")
        }

        return this.storageFile
    }

    fun load() {
        this.data = this.gson.fromJsonType(this.getAndCreateFile(), this.type) ?: this.default()
    }

    fun save() {
        this.gson.toJson(this.data, this.storageFile)
    }
}
