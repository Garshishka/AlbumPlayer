package ru.netology.mediaplayer

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.mediaplayer.data.SongObject
import ru.netology.mediaplayer.databinding.ActivityMainBinding
import ru.netology.mediaplayer.mvvm.AlbumViewModel
import ru.netology.mediaplayer.mvvm.LoadState
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private val mediaObserver = MediaLifecycleObserver()
    private val viewModel: AlbumViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private val baseUrl =
        "https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/"


    private val onInteractionListener = object : OnInteractionListener {
        var lastSong: SongObject = SongObject(-1,"","")

        override fun playOrPause(song: SongObject){
            //viewModel.songPlayingOrStopping(song.id)

            val player = mediaObserver.mediaPlayer
            if (lastSong.file != song.file) {
                //If new song
                if (lastSong.file != "") {
                    //When we change form already playing song
                    player?.stop()
                    player?.reset()
                    lastSong.isPlayingNow = false
                    binding.songList.adapter?.notifyItemChanged(lastSong.id-1)
                }
                player?.apply {
                    setDataSource(baseUrl + song.file)

                    setOnPreparedListener {
                        it.start()
                        song.isPlayingNow = true
                        lastSong = song
                        binding.nowPlayingText.text = song.file

                        binding.nowPlayingDuration.text = getDurationInString(player.duration.toLong())

                        binding.songList.adapter?.notifyItemChanged(song.id-1)
                    }

                    setOnCompletionListener {
                        val adapter = binding.songList.adapter as SongAdapter
                        val nextId = adapter.getNextId(song.id)
                        val nextSong = adapter.getNextSong(nextId)
                        playOrPause(nextSong)
                    }
                    prepareAsync()
                }
            } else {
                //If the same song
                if (player?.isPlaying == true) {
                    player.pause()
                } else {
                    player?.start()
                }
                song.isPlayingNow = !song.isPlayingNow
                binding.songList.adapter?.notifyItemChanged(song.id-1)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycle.addObserver(mediaObserver)

        subscribe()
    }

    private fun subscribe() {
        val adapter = SongAdapter(onInteractionListener)
        binding.apply {
            songList.adapter = adapter
        }
        viewModel.albumData.observe(this) {
            val album = it.album
            adapter.submitList(album?.tracks)
            binding.apply {
                albumNameText.text = album?.title
                artistText.text = album?.artist
                publishedText.text = album?.published
                genreText.text = album?.genre

                loadingStatus.text = when (it.state) {
                    LoadState.LOADING -> getString(R.string.loading)
                    LoadState.ERROR -> getString(R.string.error)
                    LoadState.EMPTY -> getString(R.string.empty)
                    LoadState.DONE -> ""
                }
            }
        }
    }

    fun getDurationInString(duration: Long): String{
        val seconds: Long =
            TimeUnit.MILLISECONDS.toSeconds(duration) % 60
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
        return "${minutes}:${seconds}"
    }
}

