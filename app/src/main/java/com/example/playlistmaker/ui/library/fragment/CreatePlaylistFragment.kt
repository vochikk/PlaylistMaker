package com.example.playlistmaker.ui.library.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.text.set
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker.domain.library.model.PlayList
import com.example.playlistmaker.ui.library.view_model.CreatePlaylistViewModel
import com.example.playlistmaker.ui.playlist.fragment.PlaylistScreenFragment.Companion.PLAYLIST_TO_EDIT
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class CreatePlaylistFragment : Fragment() {

    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get(): FragmentCreatePlaylistBinding = _binding!!
    private var imageUri: String = ""
    private var playListToEdit = 0
    private var size = 0
    private lateinit var confirmDialog: MaterialAlertDialogBuilder

    private val viewModel: CreatePlaylistViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var isEdit = false

        if (!requireArguments().isEmpty) {
            playListToEdit = requireArguments().getInt(PLAYLIST_TO_EDIT)
            viewModel.playListLiveData.observe(viewLifecycleOwner) { playlist ->
                viewPlaylistToEdit(playlist)
                imageUri = playlist.imageUri
                size = playlist.sizePlaylist
            }
            viewModel.getPlaylist(playListToEdit)
            isEdit = true
        }

        val pickPhoto =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.imagePlaylist.setImageURI(uri)
                    requireContext().contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    imageUri = uri.toString()
                }
            }

        binding.imagePlaylist.setOnClickListener {
            pickPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.buttonBack.setOnClickListener {
            if (isEdit) {
                findNavController().navigateUp()
            } else {
                cancel()
            }
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                binding.buttonCreate.isEnabled = (!s.isNullOrEmpty())
            }
        }

        textWatcher?.let { binding.namePlaylist.addTextChangedListener(it) }

        binding.buttonCreate.setOnClickListener {
            if (isEdit) {
                viewModel.updatePlaylist(
                    PlayList(
                        playListToEdit,
                        binding.namePlaylist.text.toString(),
                        binding.textAbout.text.toString(),
                        imageUri,
                        size
                    )
                )
            } else {
                val name = binding.namePlaylist.text.toString()
                val about = binding.textAbout.text.toString()
                val playList = PlayList(
                    namePlaylist = name,
                    about = about,
                    imageUri = imageUri,
                    idPlaylist = 0
                )
                if (imageUri.isNotEmpty()) {
                    savePhotoInStorage(imageUri.toUri(), name)
                }
                viewModel.savePlaylist(playList)
                Toast.makeText(
                    requireContext(),
                    view.resources.getString(
                        R.string.playlist_is_creation,
                        playList.namePlaylist
                    ),
                    Toast.LENGTH_LONG
                ).show()
            }
            findNavController().navigateUp()
        }

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.dialog_title)
            .setMessage(R.string.dialog_message)
            .setNeutralButton(R.string.dialog_neutral) { dialog, which -> }
            .setPositiveButton(R.string.dialog_positive) { dialog, which ->
                findNavController().navigateUp()
            }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (isEdit) {
                findNavController().navigateUp()
            } else {
                cancel()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun cancel() {
        if ((imageUri.isNotEmpty()) || (binding.namePlaylist.text.toString().isNotEmpty())
            || (binding.textAbout.text.toString().isNotEmpty())
        ) {
            confirmDialog.show()
        } else {
            findNavController().navigateUp()
        }
    }

    private fun savePhotoInStorage(uri: Uri, name: String): Uri {
        val filePath =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), LOGO_ALBUM)
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "${name}.jpg")
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return file.toUri()
    }

    fun viewPlaylistToEdit(playList: PlayList) {
        binding.title.text = view?.resources?.getString(R.string.edit_title)
        binding.buttonCreate.text = view?.resources?.getString(R.string.save_playlist)

        if (!playList.imageUri.isNullOrEmpty()) {
            binding.imagePlaylist.setImageURI(playList.imageUri.toUri())
        }

        binding.namePlaylist.setText(playList.namePlaylist)
        binding.textAbout.setText(playList.about)
    }

    companion object {
        private const val LOGO_ALBUM = "logoalbum"
    }

}