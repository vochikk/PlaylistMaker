package com.example.playlistmaker.ui.player.view_model

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
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

    fun updatePlayList(track: Track, playList: PlayList, context: Context) {
        var isInclude: Boolean = false

        viewModelScope.launch(Dispatchers.IO) {
            favoriteInteractor.getTrackList(playList).collect { list ->
                if (list.contains(track.trackId)) {
                    isInclude = true
                }
            }

            viewModelScope.launch(Dispatchers.Main) {
                val str = if (isInclude) {
                    "${
                        context.resources
                            .getString(
                                R.string.track_in_playlist
                            )
                    } ${playList.namePlaylist}"
                } else {
                    "${
                        context.resources
                            ?.getString(
                                R.string.track_add_in_playlist
                            )
                    } ${playList.namePlaylist}"
                }
                val toast: Toast = Toast.makeText(context, str, Toast.LENGTH_SHORT)
                toast.show()
            }

            favoriteInteractor.insertTrackInPlayList(playList, track)
            favoriteInteractor.getTrackList(playList).collect { list ->
                playList.sizePlaylist = list.size
            }
            plylistInteractor.updateTracksList(playList)
            getPlayList()


        }
    }

    companion object {
        private const val TIMER_DELAY_MILLIS = 300L
    }
}