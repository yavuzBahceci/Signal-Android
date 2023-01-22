package com.app.signal.utils.model

import android.content.Context
import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

sealed class Text: Parcelable {
    @Parcelize
    object Empty: Text()

    @Parcelize
    data class Resource(@StringRes val resourceId: Int): Text()

    @Parcelize
    data class Chars(val sequence: CharSequence?): Text()

    @Parcelize
    data class WithContext(val lambda: (Context) -> CharSequence?): Text() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true

            if (javaClass != other?.javaClass) return false

            return true
        }

        override fun hashCode(): Int {
            return javaClass.hashCode()
        }
    }

    val isEmpty: Boolean get() = when (this) {
        is Resource -> resourceId <= 0
        is Chars -> sequence.isNullOrBlank()
        else -> false
    }

    fun resolve(ctx: Context): CharSequence? {
        return when (this) {
            is Resource -> if (resourceId == 0) null else ctx.getString(resourceId)
            is Chars -> sequence
            is WithContext -> lambda(ctx)
            else -> null
        }
    }
}