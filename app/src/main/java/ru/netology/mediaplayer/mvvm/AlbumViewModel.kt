package ru.netology.mediaplayer.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.concurrent.thread

class AlbumViewModel : ViewModel() {
    private val repository = SongRepository()
    private val _albumData = MutableLiveData(AlbumFeedModel())
    val albumData: LiveData<AlbumFeedModel>
        get() = _albumData

    init {
        loadAlbum()
    }

    private fun loadAlbum() {
        thread {
            _albumData.postValue(AlbumFeedModel(loading = true))
            try {
                val album = repository.getAlbum().let { albumObject ->
                    albumObject.copy(tracks = albumObject.tracks.map {
                        it.copy(album = albumObject.title) })
                }
                AlbumFeedModel(album = album, empty = album.tracks.isEmpty())
            } catch (e: Exception) {
                println(e.message)
                AlbumFeedModel(error = true)
            }.also(_albumData::postValue)
        }
    }

}

