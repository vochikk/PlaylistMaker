package com.example.playlistmaker.ui.library.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.ui.library.view_model.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistFragment: Fragment() {

    companion object {
        private const val LIBRARY = "library"

        fun newInstance(isVisible: Boolean) = PlaylistFragment().apply {
            arguments = Bundle().apply {
                putBoolean(LIBRARY, isVisible)
            }
        }
    }

    private lateinit var binding: FragmentPlaylistBinding

    private val playlistViewModel: PlaylistViewModel by viewModel {
        parametersOf(requireArguments().getBoolean(LIBRARY))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        render()
    }

    private fun render() {
        binding.apply {
            buttonNewPlaylist.visibility = View.VISIBLE
            placeholderImage.visibility = View.VISIBLE
            placeholderText.visibility = View.VISIBLE
        }
    }
}