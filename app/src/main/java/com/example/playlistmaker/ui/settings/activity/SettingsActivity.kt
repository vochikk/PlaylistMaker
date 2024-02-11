package com.example.playlistmaker.ui.settings.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding =  ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this, SettingsViewModel.getSettingsViewModelFactory(this))[SettingsViewModel::class.java]


        binding.buttonBack.setOnClickListener { finish() }

        binding.buttonShare.setOnClickListener{ viewModel.shareApp() }

        binding.buttonSupport.setOnClickListener{ viewModel.openSupport() }

        binding.buttonTerms.setOnClickListener{ viewModel.openTerms() }

        viewModel.getLiveData().observe(this){state ->
            binding.themeSwitcher.isChecked = state
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.updateThemeSettings(checked)
        }



    }
}