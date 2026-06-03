package com.agents.app.demo.utils

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.PluralsRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import timber.log.Timber

/**
 * Useful class when showing a string with the correct locale is needed.
 * It can be constructed at any place and used with getString in the fragment/activity/dialog/composable.
 */
open class StringResource(val resId: Int, vararg val formatArgs: Any) : Parcelable {
    open fun getString(context: Context) = try {
        context.getString(resId, *formatArgs)
    } catch (throwable: Throwable) {
        Timber.e(throwable)
        ""
    }

    @Composable
    @ReadOnlyComposable
    open fun getString(): String {
        return stringResource(resId, *formatArgs)
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        when (this) {
            is NonTranslatableStringResource -> {
                parcel.writeInt(TYPE_NON_TRANSLATABLE)
                parcel.writeString(nonTranslatableString)
            }
            is PluralStringResource -> {
                parcel.writeInt(TYPE_PLURAL)
                parcel.writeInt(resId)
                parcel.writeInt(quantity)
                parcel.writeFormatArgs(formatArgs)
            }
            else -> {
                parcel.writeInt(TYPE_BASE)
                parcel.writeInt(resId)
                parcel.writeFormatArgs(formatArgs)
            }
        }
    }

    companion object CREATOR : Parcelable.Creator<StringResource> {
        private const val TYPE_BASE = 0
        private const val TYPE_NON_TRANSLATABLE = 1
        private const val TYPE_PLURAL = 2

        override fun createFromParcel(parcel: Parcel): StringResource = when (parcel.readInt()) {
            TYPE_NON_TRANSLATABLE -> NonTranslatableStringResource(parcel.readString().orEmpty())
            TYPE_PLURAL -> {
                val resId = parcel.readInt()
                val quantity = parcel.readInt()
                val args = parcel.readFormatArgs()
                PluralStringResource(resId, quantity, *args)
            }
            else -> {
                val resId = parcel.readInt()
                val args = parcel.readFormatArgs()
                StringResource(resId, *args)
            }
        }

        override fun newArray(size: Int): Array<StringResource?> = arrayOfNulls(size)
    }
}

class NonTranslatableStringResource(val nonTranslatableString: String) : StringResource(0) {
    override fun getString(context: Context): String = nonTranslatableString

    @Composable
    @ReadOnlyComposable
    override fun getString(): String = nonTranslatableString

    fun getRawString(): String = nonTranslatableString
}

class PluralStringResource(@PluralsRes resId: Int, val quantity: Int, vararg formatArgs: Any) : StringResource(
    resId,
    *formatArgs
) {
    override fun getString(context: Context): String =
        context.resources.getQuantityString(resId, quantity, *formatArgs)

    @Composable
    @ReadOnlyComposable
    override fun getString(): String = pluralStringResource(resId, quantity, *formatArgs)
}

private fun Parcel.writeFormatArgs(args: Array<out Any>) {
    writeInt(args.size)
    args.forEach { writeValue(it) }
}

private fun Parcel.readFormatArgs(): Array<Any> {
    val size = readInt()
    val loader = StringResource::class.java.classLoader
    return Array(size) { readValue(loader) ?: "" }
}
