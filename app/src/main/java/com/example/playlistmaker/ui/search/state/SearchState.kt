package com.example.playlistmaker.ui.search.state

import com.example.playlistmaker.domain.player.models.Track

sealed interface SearchState {
    object isLoading: SearchState
    object Error: SearchState
    object NonFound: SearchState
    class Content(var data: List<Track>): SearchState
}