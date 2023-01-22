package com.app.alert_sheet

import android.os.Parcelable
import com.app.signal.utils.model.Text
import kotlinx.parcelize.Parcelize

enum class AlertActionStyle {
    Contained, Text
}

@Parcelize
data class AlertAction(
    val title: Text,
    val style: AlertActionStyle = AlertActionStyle.Contained,
    val receiver: String? = null
): Parcelable