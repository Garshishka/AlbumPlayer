package ru.netology.mediaplayer

import com.google.android.material.button.MaterialButton

interface OnInteractionListener {
    fun playOrPause(file: String, playButton: MaterialButton, id: Long){}

}