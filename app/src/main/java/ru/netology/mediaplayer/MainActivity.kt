package ru.netology.mediaplayer

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import ru.netology.mediaplayer.data.AlbumObject
import ru.netology.mediaplayer.data.SongObject
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val mediaObserver = MediaLifecycleObserver()

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

        val adapter = subscribe()
        var album : AlbumObject = AlbumObject(0,"No","No","No","No","No", songList)
        thread {
            album = getSongsFromInternet()

        }
        adapter.submitList(album.tracks)
    }

    private fun subscribe() : SongAdapter {
        val adapter = SongAdapter()
        findViewById<RecyclerView>(R.id.song_list).adapter = adapter
        return adapter
    }

    private fun getSongsFromInternet():AlbumObject{
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
        val gson = Gson()
//        val typeToken = object : TypeToken<List<SongObject>>() {}

//        companion object
            val BASE_URL = "https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/"
            val jsonType = "application/json".toMediaType()
//    }
//
//    private fun getAlbum(client: OkHttpClient, gson: Gson): AlbumObject{
        val request: Request = Request.Builder()
            .url("https://github.com/netology-code/andad-homeworks/raw/master/09_multimedia/data/album.json")
            .build()

        val typeToken = object : TypeToken<AlbumObject>() {}

        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("body is null") }
            .let {
                gson.fromJson(it, typeToken.type)
            } ?: AlbumObject(0,"No","No","No","No","No", songList)
    }
}

val songList : List<SongObject> = listOf(
    SongObject(1,"peepa","dooda",1234),
    SongObject(2,"peepa","ddsda",132),
    SongObject(3,"keka","kekaruj",323123)
)