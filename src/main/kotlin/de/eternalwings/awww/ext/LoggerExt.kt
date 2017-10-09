package de.eternalwings.awww.ext

import org.slf4j.Logger

fun Logger.debug(messageProducer: () -> String) {
    if (this.isDebugEnabled) {
        this.debug(messageProducer())
    }
}

fun Logger.trace(messageProducer: () -> String) {
    if (this.isTraceEnabled) {
        this.trace(messageProducer())
    }
}

fun Logger.info(messageProducer: () -> String) {
    if (this.isInfoEnabled) {
        this.info(messageProducer())
    }
}
