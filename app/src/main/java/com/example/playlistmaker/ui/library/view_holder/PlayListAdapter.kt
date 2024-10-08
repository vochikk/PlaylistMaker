package com.example.playlistmaker.ui.library.view_holder

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.library.model.PlayList
import com.example.playlistmaker.domain.player.models.Track
import com.example.playlistmaker.ui.library.view_holder.LibraryAdapter.OnClickListener

class PlayListAdapter(private val context: Context) : RecyclerView.Adapter<PlayListViewHolder>() {

    var list = ArrayList<PlayList>()
    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist, parent, false)
        return PlayListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        holder.bind(list[position], context)
        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(list[position])
            }
        }
    }

    fun setOnClickListener (onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick (playList: PlayList)
    }
}