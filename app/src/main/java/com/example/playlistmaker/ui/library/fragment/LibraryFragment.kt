package com.example.playlistmaker.ui.library.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator

class LibraryFragment: Fragment() {

    private var _binding: FragmentLibraryBinding? = null
    private val binding get(): FragmentLibraryBinding = _binding!!

    private var tapMediator: TabLayoutMediator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = LibraryViewPagerAdapter(childFragmentManager, lifecycle)

        tapMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) {tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tapMediator?.attach()

    }

    override fun onDestroy() {
        super.onDestroy()
        tapMediator?.detach()
        _binding = null
    }
}