package com.elroid.currency.data.exception

import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

/**
 * Returns true if the error is down to bad connectivity
 */
fun Throwable?.isConnectivityError(): Boolean {
    if (this == null) return false
    return matches { this is UnknownHostException } ||
            matches { this is SocketTimeoutException } ||
            matches { this is SocketException } ||
            matches { this is ConnectException } ||
            matches { this is TimeoutException }
}

/**
 * Returns true if this exception or any of its causes match the predicate (e.g. ignores wrappers)
 */
private fun Throwable.matches(predicate: Throwable.() -> Boolean): Boolean {
    var c: Throwable? = this
    while (c != null) {
        if (c.predicate()) return true
        c = c.cause
    }
    return false
}