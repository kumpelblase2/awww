package de.eternalwings.awww.ext

import com.google.gson.Gson
import java.io.File
import java.io.FileReader
import java.io.FileWriter

inline fun <reified T> Gson.fromJson(path: File): T {
    return FileReader(path).use { this.fromJson(it, T::class.java) }
}

fun Gson.toJson(obj: Any, path: File) {
    FileWriter(path).use {
        this.toJson(obj, it)
        it.flush()
    }
}
