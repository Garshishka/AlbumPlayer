package ru.netology.mediaplayer.data

data class SongObject(
    val id: Int,
    val file: String,
    val album: String?,
    var isPlayingNow: Boolean = false,
)