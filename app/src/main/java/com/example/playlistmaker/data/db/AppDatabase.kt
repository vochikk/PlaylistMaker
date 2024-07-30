package com.example.playlistmaker.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.db.dao.TrackDao
import com.example.playlistmaker.data.db.entity.TrackEntity
import org.koin.android.ext.koin.androidContext

@Database(version = 1, entities = [TrackEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao

}