package de.eternalwings.awww.ext

import com.google.gson.Gson
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import kotlin.reflect.KClass

inline fun <reified T> Gson.fromJson(path: File): T {
    return FileReader(path).use { this.fromJson(it, T::class.java) }
}

fun <T: Any> Gson.fromJsonType(path: File, type: KClass<T>): T? {
    return FileReader(path).use { this.fromJson(it, type.java) }
}

fun Gson.toJson(obj: Any, path: File) {
    FileWriter(path).use {
        this.toJson(obj, it)
        it.flush()
    }
}
