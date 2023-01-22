package com.app.signal.control_kit

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.media.Image
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isGone
import com.app.signal.control_kit.ex.*
import com.app.signal.design_system.R
import com.app.signal.design_system.TextAppearance
import com.app.signal.design_system.ThemeColor
import com.app.signal.design_system.Weight
import com.app.signal.utils.model.Img
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel

@RequiresApi(Build.VERSION_CODES.M)
class CircularImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    var hasBorder: Boolean = true
        set(value) {
            field = value

            val padding: Int
            val strokeWidth: Float

            if (value) {
                strokeWidth = resources.getDimension(R.dimen.stroke_thin)
                padding = strokeWidth.toInt() / 2
            } else {
                padding = 0
                strokeWidth = 0f
            }

            imageView.strokeWidth = strokeWidth
            imageView.setContentPaddingRelative(
                padding,
                padding,
                padding,
                padding
            )
        }

    var hasBackground: Boolean = true
        set(value) {
            field = value

            if (value) {
                imageView.setBackgroundColor(ThemeColor.Background.Primary)
            } else {
                imageView.setBackgroundColor(Color.TRANSPARENT)
            }
        }

    private val imageView: ShapeableImageView
    private val txtInitials: AppCompatTextView

    init {
        imageView = ShapeableImageView(context).also {
            it.scaleType = ImageView.ScaleType.FIT_CENTER
            it.strokeWidth = 0f

            val strokeColor = it.context.resolveColor(ThemeColor.Content.Opacity10)
            it.strokeColor = ColorStateList.valueOf(strokeColor)

            it.shapeAppearanceModel = ShapeAppearanceModel()
                .toBuilder()
                .setAllCornerSizes { rect ->
                    rect.width() / 2
                }
                .build()

            addView(
                it,
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
        }

        txtInitials = AppCompatTextView(context).also {
            it.gravity = Gravity.CENTER

            it.setTextColor(ThemeColor.Content.Normal)
            it.setTextAppearance(TextAppearance.H4(Weight.SemiBold))

            it.isGone = true

            addView(
                it,
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
        }

        hasBorder = true
        hasBackground = true
    }

    fun loadImage(img: Img) {
        imageView.loadImg(img)
    }

    fun loadImage(uri: Uri?, initials: String? = null, mode: FillMode = FillMode.CenterInside) {
        imageView.loadImage(uri, mode)

        if (uri != null) {
            txtInitials.isGone = true
        } else {
            txtInitials.isGone = false
            txtInitials.text = initials
        }
    }
}