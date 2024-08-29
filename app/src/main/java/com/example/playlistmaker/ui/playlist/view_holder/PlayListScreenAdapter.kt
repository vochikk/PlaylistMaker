package com.example.playlistmaker.ui.playlist.view_holder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.player.models.Track

class PlayListScreenAdapter (): RecyclerView.Adapter<PlayListScreenViewHolder> () {
    var tracks : MutableList<Track> = mutableListOf()
    private var onClickListener: OnClickListener? = null
    private var onLongClickListener: OnLongClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListScreenViewHolder = PlayListScreenViewHolder(parent)

    override fun onBindViewHolder(holder: PlayListScreenViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(tracks[position])
            }
        }

        holder.itemView.setOnLongClickListener {
            if (onLongClickListener != null) {
                onLongClickListener!!.onLongClick(tracks[position])
            }
            true
        }
    }

    override fun getItemCount(): Int = tracks.size

    fun setOnClickListener (onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    fun setOnLongClickListener (onLongClickListener: OnLongClickListener) {
        this.onLongClickListener = onLongClickListener
    }

    interface OnClickListener {
        fun onClick (track: Track)
    }

    interface OnLongClickListener {
        fun onLongClick (track: Track) : Boolean
    }

}