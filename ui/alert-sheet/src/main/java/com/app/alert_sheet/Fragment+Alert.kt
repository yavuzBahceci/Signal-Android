package com.app.alert_sheet

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.app.signal.alert_sheet.R
import com.app.signal.control_kit.ex.present
import com.app.signal.control_kit.fragment.ex.requireRouterFragmentManager
import com.app.signal.utils.model.Text

fun Fragment.presentAlert(
    throwable: Throwable?,
    onDismissRequestKey: String? = null
) {
    presentAlert(
        msg = Text.Chars(throwable?.localizedMessage),
        onDismissReceiverKey = onDismissRequestKey
    )
}

fun Fragment.presentAlert(
    @StringRes titleResId: Int? = null,
    @StringRes msgResId: Int,
    onDismissReceiverKey: String? = null
) {
    val title: Text? = if (titleResId != null) {
        Text.Resource(titleResId)
    } else {
        null
    }

    presentAlert(
        title,
        Text.Resource(msgResId),
        onDismissReceiverKey
    )
}

fun Fragment.presentAlert(
    title: Text? = null,
    msg: Text? = null,
    onDismissReceiverKey: String? = null
) {
    val fragment = AlertSheetFragment.instantiate(
        title,
        msg,
        listOf(
            AlertAction(
                Text.Resource(R.string.action_continue),
                AlertActionStyle.Contained,
                onDismissReceiverKey
            )
        )
    )

    requireRouterFragmentManager().present(fragment)
}