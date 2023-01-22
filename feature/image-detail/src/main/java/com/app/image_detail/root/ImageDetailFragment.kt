package com.app.image_detail.root

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.app.signal.control_kit.IndicatorView
import com.app.signal.control_kit.ex.loadImage
import com.app.signal.control_kit.fragment.ActionBarFragment
import com.app.signal.control_kit.fragment.ex.consumeWindowInsets
import com.app.signal.design_system.R.*
import com.app.signal.image_detail.R
import com.google.android.material.imageview.ShapeableImageView

private object BundleKey {
    const val TITLE = "BUNDLE_KEY_TITLE"
    const val URI = "BUNDLE_KEY_URI"
}

open class ImageDetailFragment : ActionBarFragment(R.layout.fragment_image_detail) {
    private lateinit var imageView: ShapeableImageView
    private lateinit var indicator: IndicatorView
    private lateinit var title: TextView

    open fun load(uri: Uri) {
        imageView.loadImage(uri)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setStartToolbarIcon(drawable.ic_close)

        indicator = view.findViewById(R.id.indicator)
        title = view.findViewById(R.id.title_text)
        imageView = view.findViewById(R.id.image)

        consumeWindowInsets { _, insets ->
            toolbar.updatePadding(top = insets.top)
            WindowInsetsCompat.CONSUMED
        }

        val args = arguments ?: return

        setTitle(args.getString(BundleKey.TITLE))

        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            args.getParcelable(BundleKey.URI, Uri::class.java)
        } else {
            args.getParcelable(BundleKey.URI)
        }

        if (uri != null) {
            load(uri)
        }
    }

    companion object {
        fun instantiate(title: String? = null, uri: Uri): ImageDetailFragment {
            val fragment = ImageDetailFragment()

            fragment.arguments = bundleOf(
                BundleKey.TITLE to title,
                BundleKey.URI to uri
            )

            return fragment
        }
    }
}
