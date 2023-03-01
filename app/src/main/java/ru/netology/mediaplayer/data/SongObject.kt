package ru.netology.mediaplayer.data

data class SongObject(
    val id: Long,
    val file: String,
    val title: String?,
    val album: String?,
    val timeLength: Long?,
)