package com.app.alert_sheet

import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import com.app.signal.control_kit.ex.current
import com.app.signal.utils.model.Text

fun FragmentManager.presentAlert(
    throwable: Throwable?,
    onDismissRequestKey: String? = null
) {
    current?.presentAlert(throwable, onDismissRequestKey)
}

fun FragmentManager.presentAlert(
    @StringRes titleResId: Int? = null,
    @StringRes msgResId: Int,
    onDismissReceiverKey: String? = null
) {
    current?.presentAlert(titleResId, msgResId, onDismissReceiverKey)
}

fun FragmentManager.presentAlert(
    title: Text? = null,
    msg: Text? = null,
    onDismissReceiverKey: String? = null
) {
    current?.presentAlert(title, msg, onDismissReceiverKey)
}