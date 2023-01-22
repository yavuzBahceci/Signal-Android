import android.content.Context
import android.content.res.Configuration
import android.content.res.TypedArray
import androidx.annotation.AttrRes
import androidx.annotation.DimenRes
import androidx.annotation.Dimension
import androidx.annotation.StyleRes

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
fun Context.resolveTextAppearanceResId(@AttrRes attrRes: Int): Int {
    val a: TypedArray = obtainStyledAttributes(intArrayOf(attrRes))
    return try {
        a.getResourceId(0, 0)
    } finally {
        a.recycle()
    }
}