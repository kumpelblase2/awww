package de.eternalwings.awww.ext

import java.io.File

fun File.createWithParentsIfNotExist(): Boolean {
    if (!this.exists()) {
        val parentDir = this.parentFile
        if (!parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                return false
            }
        }

        if (!this.createNewFile()) {
            return false
        }
    }

    return true
}
