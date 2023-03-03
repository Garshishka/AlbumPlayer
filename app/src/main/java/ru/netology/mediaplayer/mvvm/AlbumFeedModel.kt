package ru.netology.mediaplayer.mvvm

import ru.netology.mediaplayer.data.AlbumObject

data class AlbumFeedModel(
    val album: AlbumObject? = null,
    val state: LoadState = LoadState.DONE
)

enum class LoadState() {
    LOADING,
    ERROR,
    EMPTY,
    DONE,
}