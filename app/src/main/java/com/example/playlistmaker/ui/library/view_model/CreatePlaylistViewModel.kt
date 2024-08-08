package com.example.playlistmaker.ui.library.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.db.entity.PlayListEntity
import com.example.playlistmaker.domain.db.PlayListInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(
    private val interactor: PlayListInteractor
): ViewModel() {

    fun savePlaylist(playList: PlayListEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.insertPlaylist(playList)
        }
    }
}