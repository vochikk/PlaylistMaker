package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val TRACK_LIST_KEY = "track_list_key"
const val MAX_COUNT = 10


class SearchHistory (val sheredPref: SharedPreferences) {

    fun saveTrack (track: Track, trackList: ArrayList<Track>) {
        when {
            trackList.isEmpty() -> trackList.add(0, track)
            trackList.size == MAX_COUNT -> {
                if (trackList.contains(track)){
                    trackList.remove(track)
                    trackList.add(0, track)
                } else {
                    trackList.removeAt(9)
                    trackList.add(0, track)
                }
            }
            trackList.size < MAX_COUNT -> {
                trackList.remove(track)
                trackList.add(0, track)
            }
        }
            updateHistory(trackList)
    }

    private fun read () : Array<Track> {
        val json = sheredPref.getString(TRACK_LIST_KEY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    fun getTrackList () : ArrayList<Track> {
        var trackList = arrayListOf<Track>()
        trackList.addAll(read())
        return trackList
    }

    fun removeTrackList () {
        var tracks = getTrackList()
        tracks.clear()
        updateHistory(tracks)
    }

    private fun updateHistory (trackList: ArrayList<Track>) {
        val json = Gson().toJson(trackList)
        sheredPref.edit()
            .putString(TRACK_LIST_KEY, json)
            .apply()
    }
}