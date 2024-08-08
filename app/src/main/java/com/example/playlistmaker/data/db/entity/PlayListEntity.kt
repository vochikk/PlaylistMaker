package com.example.playlistmaker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
class PlayListEntity (

    @PrimaryKey(autoGenerate = true)
    val idPlaylist: Int,

    var namePlaylist: String,
    var about: String,
    var imageUri: String,
    var tracksList: String = "",
    var sizePlaylist: Int = 0
)