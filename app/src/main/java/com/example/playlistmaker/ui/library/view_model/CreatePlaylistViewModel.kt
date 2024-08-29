package com.example.playlistmaker.ui.library.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.library.PlayListInteractor
import com.example.playlistmaker.domain.library.model.PlayList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(
    private val interactor: PlayListInteractor
): ViewModel() {

    private var _playlistLiveData = MutableLiveData<PlayList>()
    val playListLiveData: LiveData<PlayList> = _playlistLiveData

    fun savePlaylist(playList: PlayList) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.insertPlaylist(playList)
        }
    }

    fun getPlaylist(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val playlist = interactor.getPlaylist(id)
            _playlistLiveData.postValue(playlist)
        }
    }

    fun updatePlaylist (playList: PlayList){
        viewModelScope.launch(Dispatchers.IO) {
            interactor.updatePlaylist(playList)
        }
    }
}