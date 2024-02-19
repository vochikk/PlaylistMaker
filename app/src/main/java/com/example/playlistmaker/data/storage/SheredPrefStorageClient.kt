package com.example.playlistmaker.data.storage

import android.content.SharedPreferences
import com.example.playlistmaker.data.search.dto.TrackDto
import com.google.gson.Gson

const val TRACK_LIST_KEY = "track_list_key"

class SheredPrefStorageClient(
    private val sheredPref: SharedPreferences,
    private val gson: Gson
    ): StorageClient{

    override fun save(trackList: List<TrackDto>): Boolean {
        sheredPref.edit().putString(TRACK_LIST_KEY, gson.toJson(trackList)).apply()
        return true
    }

    override fun get(): List<TrackDto> {
        val result = sheredPref.getString(TRACK_LIST_KEY, null)
        if (result != null) {
            val array = gson.fromJson(result, Array<TrackDto>::class.java)
            return array.toList()
        } else {
            return emptyList()
        }
    }
}