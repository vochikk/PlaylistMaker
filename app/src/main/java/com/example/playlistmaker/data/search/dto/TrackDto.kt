package com.example.playlistmaker.data.search.dto


class TrackDto (
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
    var isFavorite: Boolean = false,
    var timestamp: Long
)