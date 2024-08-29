package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.db.dao.PlayListDao
import com.example.playlistmaker.data.db.dao.TrackDao
import com.example.playlistmaker.data.db.dao.TrackListDao
import com.example.playlistmaker.data.db.dao.TracksToPlaylistDao
import com.example.playlistmaker.data.db.entity.PlayListEntity
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.data.db.entity.TrackListEntity
import com.example.playlistmaker.data.db.entity.TracksToPlaylistEntity

@Database(version = 1, entities = [TrackEntity::class, PlayListEntity::class, TrackListEntity::class, TracksToPlaylistEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao

    abstract fun playlistDao(): PlayListDao

    abstract fun trackListDao(): TrackListDao

    abstract fun tracksToPlaylistDao(): TracksToPlaylistDao
}