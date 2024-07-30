package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.TrackEntity

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrack (trackEntity: TrackEntity)

    @Delete
    fun deleteTrack (trackEntity: TrackEntity)

    @Query("SELECT * FROM track_table")
    fun getListTrack() : List<TrackEntity>

    @Query("SELECT track_id FROM track_table")
    fun getIdListTrack() : List<Int>
}