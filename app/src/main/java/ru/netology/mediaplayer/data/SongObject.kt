package ru.netology.mediaplayer.data

import com.google.android.material.button.MaterialButton

data class SongObject(
    val id: Long,
    val file: String,
    val album: String?,
    //That's a hack to get next song button
    var playButton: MaterialButton? = null
)