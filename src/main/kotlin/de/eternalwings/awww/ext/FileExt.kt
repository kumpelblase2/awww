package de.eternalwings.awww.ext

import java.io.File

fun File.createWithParentsIfNotExist(initialText: String = ""): Boolean {
    if (!this.exists()) {
        val parentDir = this.parentFile
        if (!parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                return false
            }
        }

        if (this.createNewFile()) {
            if (initialText.isNotEmpty()) {
                this.writeText(initialText)
            }
        } else {
            return false
        }
    }

    return true
}
