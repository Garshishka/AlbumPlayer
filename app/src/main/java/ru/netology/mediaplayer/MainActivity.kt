package ru.netology.mediaplayer

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import ru.netology.mediaplayer.data.AlbumObject
import ru.netology.mediaplayer.data.SongObject
import ru.netology.mediaplayer.mvvm.AlbumViewModel
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private val mediaObserver = MediaLifecycleObserver()
    val viewModel: AlbumViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycle.addObserver(mediaObserver)

        val url: Uri = Uri.parse("https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/1.mp3")

//        findViewById<View>(R.id.play_button).setOnClickListener {
//            mediaObserver.apply {
//               mediaPlayer?.setDataSource("https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/1.mp3")
//           }.play()
//        }
        subscribe()
    }

    private fun subscribe(){
        viewModel.loadAlbum()
        val adapter = SongAdapter()
        findViewById<RecyclerView>(R.id.song_list).adapter = adapter
        viewModel.data.observe(this){
            adapter.submitList(it.album?.tracks)
        }
    }

}

