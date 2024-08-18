package com.example.playlistmaker.ui.playlist.view_model

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.library.PlayListInteractor
import com.example.playlistmaker.domain.library.model.PlayList
import com.example.playlistmaker.domain.player.models.Track
import com.example.playlistmaker.domain.sharing.SharingInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistScreenViewModel(
    private val interactor: PlayListInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private var _playlistLiveData = MutableLiveData<PlayList>()
    val playlistLiveData: LiveData<PlayList> = _playlistLiveData

    private var _trackListLiveData = MutableLiveData<List<Track>>()
    val tracklistLiveData: LiveData<List<Track>> = _trackListLiveData

    fun getPlayList(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val playlist = interactor.getPlaylist(id)
            _playlistLiveData.postValue(playlist)
        }
    }

    fun getTrackList(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val trackList = interactor.getTrackList(id)
            _trackListLiveData.postValue(trackList)
        }
    }

    fun deleteTrack(trackId: Int, idPlaylist: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.deleteTrack(trackId, idPlaylist)
            getTrackList(idPlaylist)
            getPlayList(idPlaylist)
        }
    }

    fun sharingTrack(context: Context) {
        val list = _trackListLiveData.value
        val playlist = _playlistLiveData.value

        if (list!!.isEmpty()) {
            Toast.makeText(
                context,
                context.resources.getString(R.string.playlist_empty),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            var str = "${playlist?.namePlaylist} \n ${playlist?.about} \n ${
                context.resources.getQuantityString(
                    R.plurals.plurals,
                    playlist!!.sizePlaylist,
                    playlist.sizePlaylist
                )
            }"
            var i = 1
            list?.forEach {
                str += "\n $i. ${it.artistName} - ${it.trackName} (${it.formatTime(it.trackTimeMillis)})"
                i++
            }

            sharingInteractor.shareTrack(str)
        }
    }

    fun deletePlaylist (idPlaylist: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.deletePlaylist(idPlaylist)
        }
    }
}