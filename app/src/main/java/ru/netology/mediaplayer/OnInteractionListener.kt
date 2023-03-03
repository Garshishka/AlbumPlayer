package ru.netology.mediaplayer

import ru.netology.mediaplayer.data.SongObject

interface OnInteractionListener {
    fun playOrPause(song: SongObject){}
}