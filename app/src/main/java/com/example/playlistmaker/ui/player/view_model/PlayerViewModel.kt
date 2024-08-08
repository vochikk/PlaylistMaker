package com.example.playlistmaker.ui.player.view_model

import android.content.ClipData.Item
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.db.entity.PlayListEntity
import com.example.playlistmaker.domain.db.FavoriteInteractor
import com.example.playlistmaker.domain.db.PlayListInteractor
import com.example.playlistmaker.domain.player.OnStateChangeListener
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.models.Track
import com.example.playlistmaker.domain.player.state.PlayerState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val favoriteInteractor: FavoriteInteractor,
    private val plylistInteractor: PlayListInteractor
) : ViewModel() {

    private val _playerStateLiveDate = MutableLiveData<PlayerState>()
    val playerStateLiveData: LiveData<PlayerState> = _playerStateLiveDate

    private val _likeStateLiveData = MutableLiveData<Boolean>()
    val likeStateLiveData: LiveData<Boolean> = _likeStateLiveData

    private val _playlistLiveData = MutableLiveData<List<PlayListEntity>>()
    val playlistLiveData: LiveData<List<PlayListEntity>> = _playlistLiveData

    private var timerJob: Job? = null
    private var newState: PlayerState = PlayerState.PREPARED()


    private fun onChange() {
        playerInteractor.setListener(object : OnStateChangeListener {
            override fun onChange(state: PlayerState) {
                newState = state
            }
        })
    }

    fun prepare(track: Track) {
        playerInteractor.prepare(track)
        onChange()
        _playerStateLiveDate.postValue(newState)
    }

    fun play() {
        playerInteractor.play()
        startTimer()
    }

    fun pause() {
        timerJob?.cancel()
        playerInteractor.pause()
        _playerStateLiveDate.postValue(newState)
    }

    fun realese() {
        timerJob?.cancel()
        timerJob = null
        _playerStateLiveDate.postValue(PlayerState.ENDING())
        playerInteractor.realese()
    }

    fun getTimer(): String {
        return playerInteractor.getTimer()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (playerInteractor.isPlaying()) {
                delay(TIMER_DELAY_MILLIS)
                _playerStateLiveDate.postValue(PlayerState.PLAYING(getTimer()))
            }
            _playerStateLiveDate.postValue(PlayerState.PREPARED())
            timerJob?.cancel()
        }
    }

    fun updateFavorite(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            if (track.isFavorite) {
                favoriteInteractor.delteTrack(track)
                track.isFavorite = false
            } else {
                favoriteInteractor.insertTrack(track)
                track.isFavorite = true
            }
            _likeStateLiveData.postValue(track.isFavorite)
        }
    }

    fun getPlayList() {
        viewModelScope.launch(Dispatchers.IO) {
            plylistInteractor.getAllPlaylists().collect { list ->
                _playlistLiveData.postValue(list)
            }
        }
    }

    fun updatePlayList(track: Track, playListEntity: PlayListEntity): Boolean {
        var str: String = ""
        var isInclude = false
        var trackList: MutableList<Track> = mutableListOf()

        if (playListEntity.sizePlaylist != 0) {
            trackList.addAll(Gson().fromJson(playListEntity.tracksList, object : TypeToken<ArrayList<Track>>() {}.type))

            trackList.forEach {
                isInclude = track == it
            }

            if (!isInclude) {
                trackList.add(track)
            }

        } else {
            trackList.add(track)
        }

        str = Gson().toJson(trackList)
        playListEntity.tracksList = str
        playListEntity.sizePlaylist = trackList.size

        viewModelScope.launch(Dispatchers.IO) {
            plylistInteractor.updateTracksList(playListEntity)
        }
        return isInclude
    }

    companion object {
        private const val TIMER_DELAY_MILLIS = 300L
    }
}