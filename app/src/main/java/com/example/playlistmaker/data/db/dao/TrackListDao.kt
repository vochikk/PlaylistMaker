package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.TrackListEntity

@Dao
interface TrackListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrack (trackListEntity: TrackListEntity)

    @Query("SELECT * FROM track_list_table")
    fun getTrackList (): List<TrackListEntity>

    @Query("DELETE FROM track_list_table WHERE trackId = :id")
    fun delete (id: Int)
}