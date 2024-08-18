package com.example.playlistmaker.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tracks_to_playlist_table",
    primaryKeys = arrayOf("idPlaylist", "trackId"),
    foreignKeys = arrayOf(
        ForeignKey(
            entity = PlayListEntity::class,
            parentColumns = arrayOf("idPlaylist"),
            childColumns = arrayOf("idPlaylist")
        ),
        ForeignKey(
            entity = TrackListEntity::class,
            parentColumns = arrayOf("trackId"),
            childColumns = arrayOf("trackId")
        )
    )
)
class TracksToPlaylistEntity(
    val idPlaylist: Int,
    @ColumnInfo(index = true)
    val trackId: Int
)