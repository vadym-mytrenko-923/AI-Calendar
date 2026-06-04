package com.ai.calendar.demo.data.common

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import timber.log.Timber
import java.lang.reflect.Type

fun <T> T.convertToString(gson: Gson): String = gson.toJson(this)

val unitType: Type = object : TypeToken<Unit>() {}.type

inline fun <reified T> String.toObject(gson: Gson): T? {
    val type = object : TypeToken<T>() {}.type
    return try {
        if (type == unitType) {
            Unit as T
        } else {
            gson.fromJson(this, type)
        }
    } catch (t: Throwable) {
        Timber.e(t)
        null
    }
}
