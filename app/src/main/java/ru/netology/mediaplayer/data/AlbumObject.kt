package ru.netology.mediaplayer.data

data class AlbumObject(
    val id: Long,
    val title: String,
    val subtitle: String,
    val artist: String,
    val published: String,
    val genre: String,
    val tracks: List<SongObject>
)