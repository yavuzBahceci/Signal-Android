package com.app.signal.data.room.converters

import android.net.Uri
import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun stringToUri(value: String?): Uri? = value?.let { Uri.parse(value) }

    @TypeConverter
    fun uriToString(uri: Uri?) = uri?.toString()
}