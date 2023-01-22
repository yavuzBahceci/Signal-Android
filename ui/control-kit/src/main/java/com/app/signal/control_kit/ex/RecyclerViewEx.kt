import android.view.View
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.ItemDecoration.getChildViewHolder(
    rv: RecyclerView,
    view: View
): RecyclerView.ViewHolder? {
    return rv.getChildViewHolder(view)
}

fun RecyclerView.ItemDecoration.getChildViewType(rv: RecyclerView, view: View): Int? {
    return getChildViewHolder(rv, view)?.itemViewType
}