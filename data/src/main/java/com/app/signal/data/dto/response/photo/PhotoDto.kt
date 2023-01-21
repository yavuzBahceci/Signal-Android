package com.app.signal.data.dto.response.photo

import android.net.Uri
import com.app.signal.domain.model.photo.Image
import com.app.signal.domain.model.photo.Photo
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class PhotoListDto(
    val page: Long,
    val pages: Long,
    val perPage: Long,
    val total: Long,
    val photo: List<PhotoDto>
)

@Serializable
data class PhotoDto(
    override val id: String,
    override val title: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: String
) : Photo {
    override val img: Image
        get() = ImageDto(
            buildSmallUri(),
            buildLargeUri(),
            buildThumbnailUri()
        )

    private fun buildThumbnailUri(): Uri? {
        return buildImageUriFromSuffix(URI_PATH_IMAGE_SUFFIX_THUMBNAIL)
    }

    private fun buildSmallUri(): Uri? {
        return buildImageUriFromSuffix(URI_PATH_IMAGE_SUFFIX_SMALL)
    }

    private fun buildLargeUri(): Uri? {
        return buildImageUriFromSuffix(URI_PATH_IMAGE_SUFFIX_LARGE)
    }

    private fun buildBaseUri(): Uri {
        return Uri.Builder()
            .scheme("https")
            .authority(getUriAuthorityString())
            .appendPath(server)
            .build()
    }

    private fun buildImageUriFromSuffix(suffix: String): Uri? {
        return buildBaseUri()
            .buildUpon()
            .appendPath("" + id + "_" + secret + suffix)
            .build()
    }

    private fun getUriAuthorityString(): String {
        return URI_AUTHORITY_FARM_PREFIX + farm + URI_AUTHORITY_SUFFIX
    }

    companion object {
        private const val URI_AUTHORITY_FARM_PREFIX = "farm"
        private const val URI_AUTHORITY_SUFFIX = ".staticflickr.com"
        private const val URI_PATH_IMAGE_SUFFIX_THUMBNAIL = "_t.jpg"
        private const val URI_PATH_IMAGE_SUFFIX_SMALL = "_n.jpg"
        private const val URI_PATH_IMAGE_SUFFIX_LARGE = "_b.jpg"
    }

}

@Serializable
data class ImageDto(
    @Contextual
    override val smallImageUrl: Uri?,
    @Contextual
    override val largeImageUrl: Uri?,
    @Contextual
    override val thumbNailUrl: Uri?
) : Image
