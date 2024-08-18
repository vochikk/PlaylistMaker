package com.example.playlistmaker.ui.library.view_holder

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.library.model.PlayList

class PlayListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val image: ImageView = itemView.findViewById(R.id.imagePlaylist)
    private val name: TextView = itemView.findViewById(R.id.namePlaylist)
    private val size: TextView = itemView.findViewById(R.id.sizePlaylist)

    fun bind(playList: PlayList, context: Context) {
        name.text = playList.namePlaylist
        size.text = context.resources.getQuantityString(
            R.plurals.plurals,
            playList.sizePlaylist,
            playList.sizePlaylist
        )

        if (!playList.imageUri.isNullOrEmpty()) {
            image.setImageURI(playList.imageUri.toUri())
        }
    }
}