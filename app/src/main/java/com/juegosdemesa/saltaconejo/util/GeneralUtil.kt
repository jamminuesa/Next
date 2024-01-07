package com.juegosdemesa.saltaconejo.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.asynctaskcoffee.cardstack.BuildConfig

//Log
const val debugMode: Boolean = BuildConfig.DEBUG

fun Any.info(message: String) {
    if (debugMode) Log.i(this::class.java.simpleName.take(23), message)
}

fun Context.toast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.longToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}