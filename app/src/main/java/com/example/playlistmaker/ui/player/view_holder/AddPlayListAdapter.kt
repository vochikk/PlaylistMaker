package com.example.playlistmaker.ui.player.view_holder

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.library.model.PlayList

class AddPlayListAdapter(private val context: Context) : RecyclerView.Adapter<AddPlayListHolder>() {

    var playList = ArrayList<PlayList>()
    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddPlayListHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_small, parent, false)
        return AddPlayListHolder(view)
    }

    override fun onBindViewHolder(holder: AddPlayListHolder, position: Int) {
        holder.bind(playList[position], context)
        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener?.onClick(playList[position])
            }
        }
    }

    override fun getItemCount(): Int = playList.size

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(playList: PlayList)
    }
}