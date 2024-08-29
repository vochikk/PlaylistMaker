package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.data.db.entity.PlayListEntity

@Dao
interface PlayListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist(playListEntity: PlayListEntity)

    @Query("SELECT * FROM playlist_table")
    fun getAllPlaylists(): List<PlayListEntity>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTracksList(playListEntity: PlayListEntity)

    @Query("SELECT * FROM playlist_table WHERE idPlaylist = :id")
    fun getPlaylist (id: Int): PlayListEntity

    @Query("UPDATE playlist_table SET sizePlaylist = :size WHERE idPlaylist = :id")
    fun updatePlaylist (id: Int, size: Int)

    @Query("DELETE FROM playlist_table WHERE idPlaylist = :id")
    fun delete(id: Int)

    @Update
    fun updatePlaylist(playListEntity: PlayListEntity)
}