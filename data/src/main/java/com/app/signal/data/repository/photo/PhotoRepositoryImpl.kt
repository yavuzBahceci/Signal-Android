package com.app.signal.data.repository.photo

import com.app.signal.data.repository.photo.store.PhotoLocalStore
import com.app.signal.data.repository.photo.store.PhotoRemoteStore
import com.app.signal.domain.form.photo.SearchQueryParams
import com.app.signal.domain.model.photo.Photo
import com.app.signal.domain.repository.PhotoRepository
import com.app.signal.domain.service.DataMediator
import javax.inject.Inject

data class PhotoRepositoryImpl @Inject constructor(
    private val mediator: DataMediator,
    private val local: PhotoLocalStore,
    private val remote: PhotoRemoteStore
) : PhotoRepository {

    override fun getSearchResults(params: SearchQueryParams) = mediator.exec(
        remote = { remote.getPhotos(params) }
    )

    override fun getSavedPhotos() = mediator.execCache(
        cache = { local.getSavedPhotos() }
    )

    override fun savePhoto(photo: Photo) = mediator.execSave(
        save = { local.savePhoto(photo) }
    )

    override fun removeSavedPhoto(id: String) = mediator.execSave(
        save = { local.removePhoto(id) }
    )
}