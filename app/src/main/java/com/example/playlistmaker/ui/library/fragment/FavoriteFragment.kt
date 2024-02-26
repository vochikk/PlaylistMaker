package com.example.playlistmaker.ui.library.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavoriteBinding
import com.example.playlistmaker.ui.library.view_model.FavoriteViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FavoriteFragment: Fragment() {

    companion object {
        private const val FAVORITE = "favorite"

        fun newInstance(isVisible: Boolean) = FavoriteFragment().apply {
            arguments = Bundle().apply {
                putBoolean(FAVORITE, isVisible)
            }
        }
    }

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get(): FragmentFavoriteBinding = _binding!!

    private val favoriteViewModel: FavoriteViewModel by viewModel {
        parametersOf(requireArguments().getBoolean(FAVORITE))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteViewModel.observeState().observe(viewLifecycleOwner) {
            render()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun render() {
        binding.apply {
            placeholderImage.visibility = View.VISIBLE
            placeholderText.visibility = View.VISIBLE
        }
    }
}