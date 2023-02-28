package ru.netology.mediaplayer

import ru.netology.mediaplayer.data.AlbumObject

data class FeedModel(
    val album: AlbumObject? = null,
    val loading : Boolean = false,
    val error : Boolean = false,
    val empty : Boolean = false
)