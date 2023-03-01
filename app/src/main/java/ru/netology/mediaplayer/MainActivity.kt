package ru.netology.mediaplayer

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.mediaplayer.databinding.ActivityMainBinding
import ru.netology.mediaplayer.mvvm.AlbumViewModel

class MainActivity : AppCompatActivity() {
    private val mediaObserver = MediaLifecycleObserver()
    val viewModel: AlbumViewModel by viewModels()
    lateinit var binding: ActivityMainBinding
    private val BASE_URL =
        "https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/"


    private val onInteractionListener = object : OnInteractionListener{
        override fun play(file: String) {
            mediaObserver.apply {
                mediaPlayer?.setDataSource(BASE_URL+file)
            }.play()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycle.addObserver(mediaObserver)

        subscribe()
    }

    private fun subscribe(){
        val adapter = SongAdapter(onInteractionListener)
        binding.apply {
            songList.adapter = adapter
        }
        viewModel.albumData.observe(this){
            val album = it.album
            adapter.submitList(album?.tracks)
            binding.apply {
                albumNameText.text = album?.title
                artistText.text = album?.artist
                publishedText.text = album?.published
                genreText.text = album?.genre
            }
        }
    }

}

