package com.example.playlistmaker.data.storage

import android.content.Context
import com.example.playlistmaker.data.search.dto.TrackDto
import com.google.gson.Gson

const val TRACK_LIST_KEY = "track_list_key"

class SheredPrefStorageClient (val context: Context) : StorageClient {
    private val sheredPref = context.getSharedPreferences(TRACK_LIST_KEY, Context.MODE_PRIVATE)

    override fun save(trackList: List<TrackDto>): Boolean {
        val gson = Gson().toJson(trackList)
        sheredPref.edit().putString(TRACK_LIST_KEY, gson).apply()
        return true
    }

    override fun get(): List<TrackDto> {
        val result = sheredPref.getString(TRACK_LIST_KEY, null)
        if (result != null) {
            val array = Gson().fromJson(result, Array<TrackDto>::class.java)
            return array.toList()
        } else {
            return emptyList()
        }
    }
}