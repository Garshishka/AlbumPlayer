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
            _albumData.postValue(AlbumFeedModel(state = LoadState.LOADING))
            try {
                val album = repository.getAlbum().let { albumObject ->
                    albumObject.copy(
                        tracks = albumObject.tracks.map {
                            it.copy(album = albumObject.title)
                        },
                    )
                }
                AlbumFeedModel(
                    album = album,
                    state = if (album.tracks.isEmpty()) LoadState.EMPTY else LoadState.DONE
                )
            } catch (e: Exception) {
                println(e.message)
                AlbumFeedModel(state = LoadState.ERROR)
            }.also(_albumData::postValue)
        }
    }
}

