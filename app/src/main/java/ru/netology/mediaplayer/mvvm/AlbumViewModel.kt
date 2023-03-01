package ru.netology.mediaplayer.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.concurrent.thread

class AlbumViewModel : ViewModel() {
    private val repository = SongRepository()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data

    fun loadAlbum(){
        thread{
            _data.postValue(FeedModel(loading = true))
            try{
                val album = repository.getAlbum()
                FeedModel(album = album, empty = album.tracks.isEmpty())
            } catch (e:Exception){
                FeedModel(error = true)
            }.also { _data::postValue }
        }
    }
}