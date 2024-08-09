package com.example.playlistmaker.ui.player.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.player.FavoriteInteractor
import com.example.playlistmaker.domain.library.PlayListInteractor
import com.example.playlistmaker.domain.library.model.PlayList
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

    private val _playlistLiveData = MutableLiveData<List<PlayList>>()
    val playlistLiveData: LiveData<List<PlayList>> = _playlistLiveData

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
                favoriteInteractor.deleteTrack(track)
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

    fun updatePlayList(track: Track, playList: PlayList): Boolean {
        var str: String = ""
        var isInclude = false
        var trackList: MutableList<Int> = mutableListOf()

        if (playList.sizePlaylist != 0) {
            trackList.addAll(
                Gson().fromJson(
                    playList.tracksList,
                    object : TypeToken<ArrayList<Int>>() {}.type
                )
            )

            trackList.forEach {
                isInclude = (track.trackId == it)
            }
        }

        if (!isInclude) {
            trackList.add(track.trackId)
            Log.d("log", trackList.toString())
        }

        str = Gson().toJson(trackList)

        playList.tracksList = str
        playList.sizePlaylist = trackList.size

        viewModelScope.launch(Dispatchers.IO) {
            favoriteInteractor.insertTrackInPlayList(track)
            plylistInteractor.updateTracksList(playList)
        }
        return isInclude
    }

    companion object {
        private const val TIMER_DELAY_MILLIS = 300L
    }
}