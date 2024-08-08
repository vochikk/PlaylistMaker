package com.example.playlistmaker.ui.library.view_holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.data.db.entity.PlayListEntity

class PlayListViewHolder (view: View): RecyclerView.ViewHolder(view) {

    private val image: ImageView = itemView.findViewById(R.id.imagePlaylist)
    private val name: TextView = itemView.findViewById(R.id.namePlaylist)
    private val size: TextView = itemView.findViewById(R.id.sizePlaylist)

    fun bind(playListEntity: PlayListEntity) {
        name.text = playListEntity.namePlaylist
        size.text = when (playListEntity.sizePlaylist) {
            1 -> { playListEntity.sizePlaylist.toString() + " трек"}
            2 -> {playListEntity.sizePlaylist.toString() + " трека"}
            3 -> {playListEntity.sizePlaylist.toString() + " трека"}
            4 -> {playListEntity.sizePlaylist.toString() + " трека"}
            else -> {playListEntity.sizePlaylist.toString() + " треков"}
        }

        if (!playListEntity.imageUri.isNullOrEmpty()) {
            image.setImageURI(playListEntity.imageUri.toUri())
        }
    }
}