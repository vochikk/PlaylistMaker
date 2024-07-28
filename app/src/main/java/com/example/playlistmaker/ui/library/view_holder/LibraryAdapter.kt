package com.example.playlistmaker.ui.library.view_holder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.player.models.Track

class LibraryAdapter() : RecyclerView.Adapter<LibraryViewHolder>() {
    var tracks = ArrayList<Track>()
    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryViewHolder = LibraryViewHolder(parent)

    override fun onBindViewHolder(holder: LibraryViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(tracks[position])
            }
        }
    }

    override fun getItemCount(): Int = tracks.size

    fun setOnClickListener (onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick (track: Track)
    }
}