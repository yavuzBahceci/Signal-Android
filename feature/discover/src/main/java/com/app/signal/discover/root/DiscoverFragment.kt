package com.app.signal.discover.root

import android.os.Bundle
import android.view.View
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.navigation.router.ImageDetailRouter
import com.app.signal.control_kit.IndicatorView
import com.app.signal.control_kit.ex.updateMargins
import com.app.signal.control_kit.field.SearchField
import com.app.signal.control_kit.fragment.ActionBarFragment
import com.app.signal.control_kit.fragment.ex.consumeWindowInsets
import com.app.signal.control_kit.fragment.ex.focusKeyboard
import com.app.signal.control_kit.recycler_view.EndlessRecyclerViewScrollListener
import com.app.signal.control_kit.recycler_view.decorations.MarginDecoration
import com.app.signal.control_kit.recycler_view.decorations.SpacingDecoration
import com.app.signal.design_system.R.*
import com.app.signal.discover.R
import com.app.signal.discover.root.model.DiscoverAction
import com.app.signal.discover.root.model.DiscoverItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import loadAttrDimension
import javax.inject.Inject

@AndroidEntryPoint
internal class DiscoverFragment : ActionBarFragment(R.layout.fragment_discover) {
    private val vm: DiscoverViewModel by viewModels()

    private lateinit var _fieldSearch: SearchField
    private lateinit var _rv: RecyclerView
    private lateinit var _indicatorView: IndicatorView

    @Inject
    lateinit var _imageDetailRouter: ImageDetailRouter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _fieldSearch = view.findViewById(R.id.field_search)
        _fieldSearch.addTextChangedListener {
            if (it.toString().isNotEmpty()) {
                vm.triggerSearch(it.toString())
            }
        }

        val layout = LinearLayoutManager(view.context)

        _rv = view.findViewById(R.id.recycler_view)
        _rv.also {
            it.layoutManager = layout

            it.setHasFixedSize(true)
            it.setItemViewCacheSize(6)

            it.addItemDecoration(
                MarginDecoration(
                    ctx = it.context,
                    horizontalResId = dimen.spacing_md
                )
            )

            it.addItemDecoration(
                SpacingDecoration(
                    ctx = it.context,
                    spacingRes = dimen.spacing_sm
                )
            )

            it.adapter = DiscoverAdapter()

            val loadMoreListener = object : EndlessRecyclerViewScrollListener(layout) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    vm.triggerToLoadMore()
                    println("!!!!!!!! On Load More")
                }
            }

            it.addOnScrollListener(loadMoreListener)
        }

        _indicatorView = view.findViewById(R.id.indicator_view)

        val spacing = view.context
            .loadAttrDimension(dimen.spacing_md)

        consumeWindowInsets { _, insets ->
            toolbar.updatePadding(top = insets.top)

            _fieldSearch.doOnPreDraw {
                _rv.updateMargins(
                    top = it.height / 2
                )

                _rv.updatePadding(
                    top = it.height,
                    bottom = insets.bottom + spacing
                )
            }

            WindowInsetsCompat.CONSUMED
        }

        bindFlows()
    }

    override fun onResume() {
        super.onResume()

        if (_fieldSearch.text.isNullOrEmpty()) {
            focusKeyboard(_fieldSearch)
        }
    }

    private fun bindFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { bindItemsFlow() }
                launch { bindActionFlow() }
                launch { bindLastSearchesFlow() }
            }
        }
    }

    private fun bindLastSearchesFlow() {

    }

    private suspend fun bindItemsFlow() {
        val discoverAdapter = _rv.adapter as DiscoverAdapter
        vm.itemsFlow.collect {
            println("!!!!!!!! Items Flow !!!!!!! $it")
            it?.let { it1 -> discoverAdapter.submit(it1) }
        }
    }

    private suspend fun bindActionFlow() {
        vm.getActionFlow().collect(this::handleSelection)
    }

    private fun handleSelection(item: DiscoverAction) {
        when (item) {
            is DiscoverAction.Select -> toImageDetail(item.photo)
            is DiscoverAction.Save -> saveImage(item.photo)
        }
    }

    private fun saveImage(photo: DiscoverItem.Photo) {
        TODO("")
    }

    private fun toImageDetail(photo: DiscoverItem.Photo) {

        println("!!!!!!! To Image Detail $photo")
        //val fragment = _imageDetailRouter.getImageDetailFragment()
        // requireRouterFragmentManager().push(fragment = )
    }
}