package de.eternalwings.awww.ext

import java.util.Random

fun <T> randomOrder(): Comparator<T> {
    val random = Random()

    return Comparator { _, _ -> random.nextInt(10).compareTo(random.nextInt(10)) }
}
