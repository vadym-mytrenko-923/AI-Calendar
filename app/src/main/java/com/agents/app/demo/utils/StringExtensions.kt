package com.agents.app.demo.utils

import android.text.Editable
import android.text.SpannableStringBuilder
import java.util.Locale
import java.util.StringJoiner

inline fun <R> String?.notNullOrEmpty(body: (string: String) -> R): R? {
    return if (!this.isNullOrEmpty() && this != "null") {
        body(this)
    } else {
        null
    }
}

fun String?.notNullOrEmpty(): String? {
    return if (!this.isNullOrEmpty() && this != "null") {
        this
    } else {
        null
    }
}

fun CharSequence?.notNullOrEmpty(): CharSequence? {
    return if (!this.isNullOrEmpty() && this != "null") {
        this
    } else {
        null
    }
}

fun Editable?.notNullOrEmpty(): String? {
    return if (!this.isNullOrEmpty()) {
        this.toString()
    } else {
        null
    }
}

fun String.isNotBlankAndNotEmpty(): Boolean = isNotBlank() && isNotEmpty()

fun String.capitalizeFirstLetter(): String = replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
}

fun StringBuilder.appendIf(check: Boolean, stringToAppend: String): StringBuilder {
    if (check) this.append(stringToAppend)
    return this
}

fun StringBuilder.appendIfNotEmpty(stringToAppend: String?): StringBuilder {
    if (!stringToAppend.isNullOrEmpty()) this.append(stringToAppend)
    return this
}

fun StringJoiner.addIfNotEmpty(stringToAppend: String?): StringJoiner {
    if (!stringToAppend.isNullOrEmpty()) this.add(stringToAppend)
    return this
}

fun SpannableStringBuilder.appendIf(check: Boolean, stringToAppend: String): SpannableStringBuilder {
    if (check) this.append(stringToAppend)
    return this
}
