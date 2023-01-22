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
import com.app.alert_sheet.presentAlert
import com.app.navigation.router.ImageDetailRouter
import com.app.navigation.router.SavedRouter
import com.app.signal.control_kit.IndicatorView
import com.app.signal.control_kit.ex.present
import com.app.signal.control_kit.ex.push
import com.app.signal.control_kit.field.SearchField
import com.app.signal.control_kit.fragment.ActionBarToolbarFragment
import com.app.signal.control_kit.fragment.ScrollableFragment
import com.app.signal.control_kit.fragment.ex.consumeWindowInsets
import com.app.signal.control_kit.fragment.ex.focusKeyboard
import com.app.signal.control_kit.fragment.ex.promptToast
import com.app.signal.control_kit.fragment.ex.requireRouterFragmentManager
import com.app.signal.control_kit.recycler_view.EndlessRecyclerViewScrollListener
import com.app.signal.control_kit.recycler_view.decorations.MarginDecoration
import com.app.signal.control_kit.recycler_view.decorations.SpacingDecoration
import com.app.signal.design_system.R.*
import com.app.signal.discover.R
import com.app.signal.discover.root.adapter.DiscoverAdapter
import com.app.signal.discover.root.adapter.SearchesAdapter
import com.app.signal.discover.root.model.DiscoverAction
import com.app.signal.discover.root.model.DiscoverItem
import com.app.signal.domain.model.State
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import loadAttrDimension
import javax.inject.Inject

@AndroidEntryPoint
internal class DiscoverFragment : ActionBarToolbarFragment(R.layout.fragment_discover),
    ScrollableFragment {
    private val vm: DiscoverViewModel by viewModels()

    private lateinit var _fieldSearch: SearchField
    private lateinit var discoverRv: RecyclerView
    private lateinit var searchRv: RecyclerView

    private lateinit var loadMoreListener: EndlessRecyclerViewScrollListener

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

        val layoutDiscover = LinearLayoutManager(view.context)
        val layoutSearch = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)


        discoverRv = view.findViewById(R.id.discover_rv)
        searchRv = view.findViewById(R.id.search_rv)

        discoverRv.also {

            it.addItemDecoration(
                SpacingDecoration(
                    ctx = it.context,
                    spacingRes = dimen.spacing_sm
                )
            )

            it.addItemDecoration(
                MarginDecoration(
                    ctx = it.context,
                    horizontalResId = dimen.spacing_md
                )
            )

            it.adapter = DiscoverAdapter()
            it.layoutManager = layoutDiscover
        }

        searchRv.also {

            it.setHasFixedSize(true)

            it.addItemDecoration(
                SpacingDecoration(
                    ctx = it.context,
                    spacingRes = dimen.spacing_xxs
                )
            )

            it.adapter = SearchesAdapter()
            it.layoutManager = layoutSearch
        }

        loadMoreListener = object : EndlessRecyclerViewScrollListener(layoutDiscover) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                vm.triggerToLoadMore()
            }

            override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
                scrolledHeight = view.computeVerticalScrollOffset()
                updateToolbar(scrolledHeight)
                super.onScrolled(view, dx, dy)
            }
        }

        discoverRv.addOnScrollListener(loadMoreListener)

        _indicatorView = view.findViewById(R.id.indicator_view)

        val spacing = view.context
            .loadAttrDimension(dimen.spacing_md)

        consumeWindowInsets { _, insets ->
            toolbar.updatePadding(top = insets.top)

            _fieldSearch.doOnPreDraw {

                searchRv.updatePadding(
                    top = it.height,
                    bottom = insets.bottom + spacing
                )
                discoverRv.updatePadding(
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

    private suspend fun bindLastSearchesFlow() {
        val adapter = searchRv.adapter as SearchesAdapter
        vm.recentSearches.collect {
            it?.let { it1 -> adapter.submit(it1) }
        }
    }

    private suspend fun bindItemsFlow() {
        val adapter = discoverRv.adapter as DiscoverAdapter
        vm.itemsFlow.collect {
            it?.let { it1 -> adapter.submit(it1) }
        }
    }

    private suspend fun bindActionFlow() {
        vm.getActionFlow().collect(this::handleSelection)
    }

    private fun handleSelection(item: DiscoverAction) {
        when (item) {
            is DiscoverAction.Select -> toImageDetail(item.photo)
            is DiscoverAction.Save -> saveImage(item.photo)
            is DiscoverAction.Search -> vm.triggerSearch(item.searchText)
        }
    }

    private fun saveImage(photo: DiscoverItem.Photo) {
        viewLifecycleOwner.lifecycleScope.launch {
            vm.savePhoto(photo).collect { state ->
                when (state) {
                    is State.Success -> onImageSaved()
                    is State.Error -> presentAlert(throwable = state.cause)

                    else -> {}
                }
            }
        }
    }

    private fun onImageSaved() {
        promptToast(getString(R.string.image_saved))
    }

    private fun updateToolbar(dy: Int) {
        val distance = toolbar.height.toFloat()
        toolbar.updateToolbarProgress(dy.toFloat() / distance)
    }

    override fun resetScroll() {
        discoverRv.smoothScrollToPosition(0)
    }

    private fun toImageDetail(photo: DiscoverItem.Photo) {

        photo.image?.largeImageUrl?.let {
            _imageDetailRouter.getImageDetailFragment(photo.title, it).let {
                requireRouterFragmentManager().present(fragment = it)
            }
        }
    }
}