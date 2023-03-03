package ru.netology.mediaplayer

import android.graphics.drawable.Drawable
import android.media.AudioAttributes
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.forEach
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
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
        //"https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/"
        "https://bigsoundbank.com/UPLOAD/mp3/0003.mp3"
    private var pauseIcon : Drawable? = null
    private var playIcon : Drawable? = null


    private val onInteractionListener = object : OnInteractionListener {
        var lastSong: SongObject = SongObject(-1,"","")

        override fun playOrPause(song: SongObject, playButton: MaterialButton) {
            val player = mediaObserver.mediaPlayer
            if (lastSong.file != song.file) {
                if (lastSong.file != "") {
                    //When we change form already playing song
                    player?.stop()
                    player?.reset()
                    lastSong.playButton?.icon = playIcon
                    lastSong.isPlayingNow = false
                }
                player?.apply {
                    setDataSource(baseUrl)// + file)

                    setOnPreparedListener {
                        it.start()
                        song.isPlayingNow = true
                        lastSong = song
                        binding.nowPlayingText.text = song.file
                        playButton.icon = pauseIcon

                        binding.nowPlayingDuration.text = getDurationInString(player.duration.toLong())
                    }

                    setOnCompletionListener {
                        val adapter = binding.songList.adapter as SongAdapter
                        val nextId = adapter.getNextId(song.id.toInt())
                        val holder =
                            binding.songList.findViewHolderForItemId(nextId.toLong()) as SongsListViewHolder?
                        val nextSong = adapter.getNextSong(nextId)
                        playOrPause(nextSong.first, nextSong.second)
                    }
                    prepareAsync()
                }

            } else {
                if (player?.isPlaying == true) {
                    player.pause()
                    song.isPlayingNow = false
                    playButton.icon = playIcon
                } else {
                    player?.start()
                    song.isPlayingNow = true
                    playButton.icon = pauseIcon
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pauseIcon = AppCompatResources.getDrawable(this, R.drawable.baseline_pause_32)
        playIcon = AppCompatResources.getDrawable(this, R.drawable.baseline_play_arrow_32)

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

