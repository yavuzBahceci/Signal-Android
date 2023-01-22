package com.app.alert_sheet

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.os.bundleOf
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isGone
import androidx.core.view.updatePadding
import com.app.signal.alert_sheet.R
import com.app.signal.control_kit.ex.pop
import com.app.signal.control_kit.fragment.ex.consumeWindowInsets
import com.app.signal.control_kit.fragment.ex.requireRouterFragmentManager
import com.app.signal.control_kit.fragment.sheet.SheetFragment
import com.app.signal.utils.model.Text
import com.app.signal.utils.parcelable
import com.app.signal.utils.parcelableArrayList
import com.google.android.material.button.MaterialButton

internal object BundleKey {
    const val ALERT_TITLE = "ALERT_TITLE"
    const val ALERT_MSG = "ALERT_MSG"
    const val ALERT_ACTIONS = "ALERT_ACTIONS"
}

class AlertSheetFragment: SheetFragment(R.layout.alert_sheet_fragment) {
    private lateinit var txtTitle: TextView
    private lateinit var txtMsg: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtTitle = view.findViewById(R.id.txt_title)
        txtMsg = view.findViewById(R.id.txt_message)

        val container = view.findViewById<LinearLayoutCompat>(R.id.content_container)

        consumeWindowInsets { _, insets ->
            container.updatePadding(bottom = insets.bottom)

            WindowInsetsCompat.CONSUMED
        }

        val args = requireArguments()

        val titleModel = args.parcelable<Text>(BundleKey.ALERT_TITLE)
        val msgModel = args.parcelable<Text>(BundleKey.ALERT_MSG)
        val actions = args.parcelableArrayList<AlertAction>(BundleKey.ALERT_ACTIONS) ?: emptyList()

        val title = titleModel?.resolve(view.context)
        val msg = msgModel?.resolve(view.context)

        txtTitle.text = title
        txtTitle.isGone = title == null

        txtMsg.text = msg
        txtMsg.isGone = msg == null

        val actionsContainer = view.findViewById<LinearLayoutCompat>(R.id.sheet_actions)

        actions.forEach { action ->
            val style = when (action.style) {
                AlertActionStyle.Contained -> com.app.signal.design_system.R.attr.buttonStyleContained
                AlertActionStyle.Text -> com.app.signal.design_system.R.attr.buttonStyleText
            }

            val button = MaterialButton(view.context, null, style)

            button.text = action.title.resolve(view.context)

            button.setOnClickListener {
                handleSelection(action)
            }

            val params = LinearLayoutCompat.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            actionsContainer.addView(button, params)
        }
    }

    private fun handleSelection(action: AlertAction) {
        val fm = requireRouterFragmentManager()

        fm.pop {
            if (action.receiver != null) {
                fm.setFragmentResult(action.receiver, bundleOf())
            }
        }
    }

    companion object {
        fun instantiate(
            title: CharSequence?,
            msg: CharSequence,
            actions: List<AlertAction>? = null
        ): AlertSheetFragment {
            val titleText: Text? = if (title != null) {
                Text.Chars(title)
            } else {
                null
            }

            return instantiate(
                titleText,
                Text.Chars(msg),
                actions
            )
        }

        fun instantiate(
            @StringRes titleRes: Int? = null,
            @StringRes msg: Int? = null,
            actions: List<AlertAction>? = null
        ): AlertSheetFragment {
            val title: Text? = if (titleRes != null) {
                Text.Resource(titleRes)
            } else {
                null
            }

            return instantiate(
                title,
                msg?.let { Text.Resource(it) },
                actions
            )
        }

        fun instantiate(
            title: Text?,
            msg: Text? = null,
            actions: List<AlertAction>? = null
        ): AlertSheetFragment {
            val fragment = AlertSheetFragment()


            fragment.arguments = bundleOf(
                BundleKey.ALERT_TITLE to title,
                BundleKey.ALERT_MSG to msg,
                BundleKey.ALERT_ACTIONS to ArrayList(actions ?: emptyList())
            )

            return fragment
        }
    }
}