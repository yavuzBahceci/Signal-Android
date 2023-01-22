package com.app.signal.di.ui

import com.app.image_detail.ImageDetailRouterImpl
import com.app.navigation.router.DiscoverRouter
import com.app.navigation.router.ImageDetailRouter
import com.app.navigation.router.SavedRouter
import com.app.saved.SavedRouterImpl
import com.app.signal.discover.DiscoverRouterImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class RoutersModule {

    @Binds
    abstract fun bindDiscoverRouter(impl: DiscoverRouterImpl): DiscoverRouter

    @Binds
    abstract fun bindImageDetailRouter(impl: ImageDetailRouterImpl): ImageDetailRouter

    @Binds
    abstract fun bindSavedRouter(impl: SavedRouterImpl): SavedRouter

}