package ru.netology.mediaplayer.mvvm

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import ru.netology.mediaplayer.data.AlbumObject
import java.util.concurrent.TimeUnit

class SongRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<AlbumObject>() {}
    private val albumUrl = "https://github.com/netology-code/andad-homeworks/raw/master/09_multimedia/data/album.json"

    companion object {
        val jsonType = "application/json".toMediaType()
    }

    fun getAlbum(): AlbumObject {
        val request: Request = Request.Builder()
            .url(albumUrl)
            .build()

        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("body is null") }
            .let {
                gson.fromJson(it, typeToken.type)
            } ?: AlbumObject(0, "No", "No", "No", "No", "No", emptyList())
    }
}

