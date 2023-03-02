package ru.netology.mediaplayer

import android.media.AudioAttributes
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import ru.netology.mediaplayer.databinding.ActivityMainBinding
import ru.netology.mediaplayer.mvvm.AlbumViewModel
import ru.netology.mediaplayer.mvvm.LoadState
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private val mediaObserver = MediaLifecycleObserver()
    private val viewModel: AlbumViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private val baseUrl =
        //"https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/"
    "https://bigsoundbank.com/UPLOAD/mp3/0003.mp3"


    private val onInteractionListener = object : OnInteractionListener {
        var filePlaying = ""
        lateinit var lastPlayButton: MaterialButton

        override fun playOrPause(file: String, playButton: MaterialButton, id: Long) {
            val player = mediaObserver.mediaPlayer
            if (filePlaying != file) {
                if (filePlaying != "") {
                    player?.stop()
                    player?.reset()
                    lastPlayButton.icon = getDrawable(R.drawable.baseline_play_arrow_32)
                }
                player?.apply {
                    setDataSource(baseUrl)// + file)
                    setOnPreparedListener {
                        it.start()
                        filePlaying = file
                        binding.nowPlayingText.text = filePlaying
                        playButton.icon = getDrawable(R.drawable.baseline_pause_32)
                        lastPlayButton = playButton
                        val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(player.duration.toLong())%60
                        val minutes = TimeUnit.MILLISECONDS.toMinutes(player.duration.toLong())
                        binding.nowPlayingDuration.text = "${minutes}:${seconds}"
                    }
                    setOnCompletionListener {
                        val adapter= binding.songList.adapter as SongAdapter
                        val nextId = adapter.getNextId(id.toInt())
                        val holder = binding.songList.findViewHolderForItemId(nextId.toLong()) as SongsListViewHolder?
                        val nextSong = adapter.getNextSong(nextId)
                        playOrPause(nextSong.first, holder!!.playButton, nextId+1L)
                    }
                    prepareAsync()
                }

            } else {
                if (player?.isPlaying == true) {
                    player.pause()
                    playButton.icon = getDrawable(R.drawable.baseline_play_arrow_32)
                } else {
                    player?.start()
                    playButton.icon = getDrawable(R.drawable.baseline_pause_32)
                }
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
}

