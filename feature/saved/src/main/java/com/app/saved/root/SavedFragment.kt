package com.app.saved.root

import android.os.Bundle
import android.view.View
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.saved.root.adapter.SavedAdapter
import com.app.saved.root.model.SavedItem
import com.app.signal.control_kit.IndicatorView
import com.app.signal.control_kit.fragment.ActionBarToolbarFragment
import com.app.signal.control_kit.fragment.ScrollableFragment
import com.app.signal.control_kit.fragment.ex.consumeWindowInsets
import com.app.signal.control_kit.recycler_view.decorations.MarginDecoration
import com.app.signal.control_kit.recycler_view.decorations.SpacingDecoration
import com.app.signal.saved.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class SavedFragment : ActionBarToolbarFragment(R.layout.fragment_saved),
    ScrollableFragment {
    private val vm: SavedViewModel by viewModels()

    private lateinit var savedRv: RecyclerView

    private lateinit var _indicatorView: IndicatorView

    override fun resetScroll() {
        savedRv.smoothScrollToPosition(0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layout = LinearLayoutManager(view.context)

        savedRv = view.findViewById(R.id.saved_rv)

        savedRv.also {

            it.addItemDecoration(
                SpacingDecoration(
                    ctx = it.context,
                    spacingRes = com.app.signal.design_system.R.dimen.spacing_sm
                )
            )

            it.addItemDecoration(
                MarginDecoration(
                    ctx = it.context,
                    horizontalResId = com.app.signal.design_system.R.dimen.spacing_md
                )
            )

            it.adapter = SavedAdapter()
            it.layoutManager = layout

            _indicatorView = view.findViewById(R.id.indicator_view)

            consumeWindowInsets { _, insets ->
                toolbar.updatePadding(top = insets.top)

                savedRv.updatePadding(
                    top = it.height,
                    bottom = insets.bottom
                )

                WindowInsetsCompat.CONSUMED
            }

        }

        bindFlows()
    }

    private fun bindFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { bindItemsFlow() }
                launch { bindActionFlow() }
            }
        }
    }

    private suspend fun bindActionFlow() {
        vm.getActionFlow().collect(this::handleSelection)

    }

    private suspend fun bindItemsFlow() {
        val adapter = savedRv.adapter as SavedAdapter
        vm.savedPhotos.collect {
            it?.let { it1 -> adapter.submit(it1) }
        }
    }

    private fun handleSelection(item: SavedItem) {
        vm.deletePhoto(item.id) // collect
    }

}