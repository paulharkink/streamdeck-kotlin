package nl.paulharkink.streamdeckapi.streamdeckkotlinapi

import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun <reified T> T.logger(): Logger {
    return LoggerFactory.getLogger(T::class.java)
}

inline fun <reified T> T.trace(msg: String) {
    logger().trace(msg)
}

inline fun <reified T> T.debug(msg: String) {
    logger().debug(msg)
}

inline fun <reified T> T.info(msg: String) {
    logger().info(msg)
}

inline fun <reified T> T.warn(msg: String) {
    logger().warn(msg)
}

inline fun <reified T> T.warn(msg: String, e: Throwable) {
    logger().warn(msg, e)
}

inline fun <reified T> T.error(msg: String) {
    logger().error(msg)
}

inline fun <reified T> T.error(msg: String, e: Throwable) {
    logger().error(msg, e)
}