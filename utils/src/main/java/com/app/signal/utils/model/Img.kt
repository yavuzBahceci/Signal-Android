package com.app.signal.utils.model

import android.content.Context
import android.net.Uri
import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

sealed class Img: Parcelable {
    @Parcelize
    data class Link(val uri: Uri?): Img()

    @Parcelize
    data class Resource(@DrawableRes val resourceId: Int): Img()

    @Parcelize
    data class WithContext(val lambda: (Context) -> Uri?): Img() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            return true
        }

        override fun hashCode(): Int {
            return javaClass.hashCode()
        }
    }

    @Parcelize
    object None: Img()
}