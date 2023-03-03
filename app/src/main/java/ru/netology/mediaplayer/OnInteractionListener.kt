package ru.netology.mediaplayer

import com.google.android.material.button.MaterialButton
import ru.netology.mediaplayer.data.SongObject

interface OnInteractionListener {
    fun playOrPause(song: SongObject, playButton: MaterialButton){}

}