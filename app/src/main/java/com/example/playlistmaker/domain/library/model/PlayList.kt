package com.example.playlistmaker.domain.library.model

class PlayList (
    val idPlaylist: Int,
    var namePlaylist: String,
    var about: String,
    var imageUri: String,
    var tracksList: String = "",
    var sizePlaylist: Int = 0
)