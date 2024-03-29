package com.example.playlistmaker.ui.settings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment: Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get(): FragmentSettingsBinding = _binding!!
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            buttonShare.setOnClickListener{ viewModel.shareApp() }
            buttonSupport.setOnClickListener{ viewModel.openSupport() }
            buttonTerms.setOnClickListener{ viewModel.openTerms() }
            themeSwitcher.setOnCheckedChangeListener { _, checked ->
                viewModel.updateThemeSettings(checked)
            }
        }

        viewModel.themeLiveData.observe(viewLifecycleOwner){state ->
            binding.themeSwitcher.isChecked = state
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}