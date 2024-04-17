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