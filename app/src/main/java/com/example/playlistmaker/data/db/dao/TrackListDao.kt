package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.TrackListEntity

@Dao
interface TrackListDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTrack (trackListEntity: TrackListEntity)

    @Query("SELECT * FROM track_list_table WHERE :id")
    fun getTrack (id: Int): TrackListEntity
}