package com.example.playlistmaker.ui.library.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.library.PlayListInteractor
import com.example.playlistmaker.domain.library.model.PlayList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(
    private val interactor: PlayListInteractor
): ViewModel() {

    fun savePlaylist(playList: PlayList) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.insertPlaylist(playList)
        }
    }
}