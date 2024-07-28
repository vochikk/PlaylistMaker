package com.example.playlistmaker.domain.player.models

import java.text.SimpleDateFormat
import java.util.Locale

data class Track (
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val collectionName: String?,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val previewUrl: String,
    var isFavorite: Boolean
    ) {

    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")

    fun formatTime(timeMillis: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(timeMillis)
    }

    fun getReleaseYear () = releaseDate.substring(0, 4)

    override fun equals(other: Any?): Boolean {
        if (other is Track) {
            return (trackId == other.trackId)
        } else {
            return false
        }
    }
}

