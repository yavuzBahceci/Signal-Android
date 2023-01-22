package com.app.signal.control_kit.recycler_view

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.signal.control_kit.recycler_view.adapter.StatefulAdapter
import kotlinx.parcelize.Parcelize

class CollectionView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {
    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()

        val states = mutableMapOf<String, Parcelable?>()

        getStatefulAdapters().forEach {
            states[it.getAdapterKey] = it.getAdapterState()
        }

        return SavedState(superState, states)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }

        val ss = state
        val nestedStates = ss.nestedStates
        super.onRestoreInstanceState(ss.superState)

        getStatefulAdapters().forEach {
            it.restoreAdapterState(nestedStates[it.getAdapterKey])
        }
    }

    private fun getStatefulAdapters(): List<StatefulAdapter<*>> {
        return when (val currentAdapter = adapter) {
            is StatefulAdapter<*> -> listOf(currentAdapter)
            is ConcatAdapter -> currentAdapter.adapters.filterIsInstance(StatefulAdapter::class.java)
            else -> emptyList()
        }
    }

    @Parcelize
    class SavedState(
        var superSaveState: Parcelable?,
        val nestedStates: MutableMap<String, Parcelable?>
    ) : BaseSavedState(superSaveState), Parcelable
}