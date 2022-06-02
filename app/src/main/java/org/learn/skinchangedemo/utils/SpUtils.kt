package org.learn.skinchangedemo.utils

import android.content.Context


fun putSp(context: Context, key: String, value: String) {
    context.getSharedPreferences("default", Context.MODE_PRIVATE).also {
        it.edit().putString(key, value).apply()
    }
}

fun getSp(context: Context, key: String) =
    context.getSharedPreferences("default", Context.MODE_PRIVATE).getString(key, "")