package com.juegosdemesa.next.util

import android.content.Context
import android.util.Log
import android.widget.Toast

//Log
fun Any.info(message: String) {
    Log.i(this::class.java.simpleName.take(23), message)
}

fun Context.toast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.longToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}