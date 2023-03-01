package ru.netology.mediaplayer.mvvm

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import ru.netology.mediaplayer.data.AlbumObject
import ru.netology.mediaplayer.data.SongObject
import java.util.concurrent.TimeUnit

class SongRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<AlbumObject>() {}

    companion object {
        val BASE_URL =
            "https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/"
        val jsonType = "application/json".toMediaType()
    }

    fun getAlbum(): AlbumObject {
        val request: Request = Request.Builder()
            .url("https://github.com/netology-code/andad-homeworks/raw/master/09_multimedia/data/album.json")
            .build()

        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("body is null") }
            .let {
                gson.fromJson(it, typeToken.type)
            } ?: AlbumObject(0, "No", "No", "No", "No", "No", songList)
    }

    private val songList: List<SongObject> = listOf(
        SongObject(1, "peepa", "dooda", 1234),
        SongObject(2, "peepa", "ddsda", 132),
        SongObject(3, "keka", "kekaruj", 323123)
    )
}

