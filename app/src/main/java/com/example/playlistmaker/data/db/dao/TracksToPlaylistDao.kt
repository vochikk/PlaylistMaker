package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.PlayListEntity
import com.example.playlistmaker.data.db.entity.TrackListEntity
import com.example.playlistmaker.data.db.entity.TracksToPlaylistEntity

@Dao
interface TracksToPlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert (playlistEntity: TracksToPlaylistEntity)

    @Query("SELECT trackId FROM tracks_to_playlist_table WHERE idPlaylist = :id")
    fun getTrackList (id: Int): List<Int>

    @Query("DELETE FROM tracks_to_playlist_table WHERE idPlaylist = :id")
    fun delete (id: Int)

    @Delete
    fun deleteTrack (tracksToPlaylistEntity: TracksToPlaylistEntity)

    @Query("SELECT idPlaylist FROM tracks_to_playlist_table WHERE trackId = :id")
    fun getPlaylistByIdTrack (id: Int) : List<Int>
}