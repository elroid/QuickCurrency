package com.elroid.currency.ui.common

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast

fun Context.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    onUiThread { Toast.makeText(this, message, length).show() }
}

fun handler(): Handler = Handler(Looper.getMainLooper())

fun onUiThread(action: () -> Unit) {
    if (Looper.myLooper() == null) {
        handler().post { action() }
    } else {
        action()
    }
}

const val MS_IN_SECOND = 1000
const val MS_IN_MINUTE = 60 * MS_IN_SECOND
const val MS_IN_HOUR = 60 * MS_IN_MINUTE
const val MS_IN_DAY = 24 * MS_IN_HOUR

fun now(): Long = System.currentTimeMillis()

//Todo: i18n of timeAgo strings
fun Long.timeAgo(now: Long = now()): String {
    if (this > now) return "in the future!" // shouldn't happen really...
    val diff = now - this
    when {
        // if under 1 minute, show "x seconds ago"
        diff < MS_IN_MINUTE -> {
            val numSeconds = Math.round(diff.toDouble() / MS_IN_SECOND).toInt()
            return "$numSeconds second(s) ago"
        }
        // if under 1 hour, show "x minutes ago"
        diff < MS_IN_HOUR -> {
            val numMinutes = Math.round(diff.toDouble() / MS_IN_MINUTE).toInt()
            return "$numMinutes minute(s) ago"
        }
        // if under 24 hours show "x hours ago"
        diff < MS_IN_DAY -> {
            val numHours = Math.round(diff.toDouble() / MS_IN_HOUR).toInt()
            return "$numHours hour(s) ago"
        }
        // else "x days ago"
        else -> {
            val numDays = Math.round(diff.toDouble() / MS_IN_DAY).toInt()
            return "$numDays day(s) ago"
        }
    }
}