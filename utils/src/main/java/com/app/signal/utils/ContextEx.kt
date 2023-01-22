import android.content.Context
import android.content.res.Configuration
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap

val Context.isUsingNightMode: Boolean
    get() {
        val status = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return status == Configuration.UI_MODE_NIGHT_YES
    }

@Dimension
fun Context.loadAttrDimension(@DimenRes attrRes: Int): Int {
    return resources.getDimension(attrRes).toInt()
}

@StyleRes
fun Context.loadStyle(@StyleRes attrRes: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attrRes, typedValue, true)
    return typedValue.data
}

@StyleRes
fun Context.resolveTextAppearanceResId(@AttrRes attrRes: Int): Int {
    val a: TypedArray = obtainStyledAttributes(intArrayOf(attrRes))
    return try {
        a.getResourceId(0, 0)
    } finally {
        a.recycle()
    }
}

fun Context.loadDrawable(@AttrRes attrRes: Int): Drawable? {
    val a: TypedArray = obtainStyledAttributes(intArrayOf(attrRes))
    return try {
        a.getDrawable(0)
    } finally {
        a.recycle()
    }
}

fun Context.bitmapFromVector(@DrawableRes id: Int): Bitmap? {
    return AppCompatResources.getDrawable(this, id)?.toBitmap()
}

fun Context.resolveIntDim(@DimenRes id: Int): Int {
    return resources.getDimension(id).toInt()
}
