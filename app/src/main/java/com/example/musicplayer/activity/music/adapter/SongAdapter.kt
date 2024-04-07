package com.example.musicplayer.activity.music.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayer.activity.music.model.MusicResultModel
import com.example.musicplayer.activity.music.presentation.SearchActivity
import com.example.musicplayer.activity.music.presentation.SearchOnClickListener
import com.example.musicplayer.databinding.ActivitySearchListItemBinding

class SongAdapter : RecyclerView.Adapter<SongAdapter.AddressHolder>() {

    private var listModel = mutableListOf<MusicResultModel>()
    private lateinit var listener: SearchOnClickListener<MusicResultModel>
    private lateinit var resultModel: MusicResultModel

    @SuppressLint("NotifyDataSetChanged")
    fun provided(
        model: List<MusicResultModel>,
        resultModel: MusicResultModel,
        context: SearchActivity,
    ) {
        this.listModel = model.toMutableList()
        this.listener = context
        this.resultModel = resultModel
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongAdapter.AddressHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = ActivitySearchListItemBinding.inflate(inflater, parent, false)
        return AddressHolder(binding)
    }

    override fun getItemCount(): Int {
        return this.listModel.size
    }

    override fun onBindViewHolder(holder: SongAdapter.AddressHolder, position: Int) {
        val model = this.listModel[position]
        holder.bind(position, model, listener)

    }

    inner class AddressHolder(binding: ActivitySearchListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        private val binding: ActivitySearchListItemBinding

        fun bind(position: Int, model: MusicResultModel, listener: SearchOnClickListener<MusicResultModel>) {

            Glide.with(binding.root).load(model.artworkUrl100).into(binding.icPhoto)

            binding.tvSongName.text = model.trackName
            binding.tvArtist.text = model.artistName
            binding.tvAlbum.text = model.collectionName

            if (resultModel.previewUrl != null) {
                if (!resultModel.previewUrl.equals(model.previewUrl)) {
                    binding.vmMusic.visibility = View.INVISIBLE
                } else {
                    binding.vmMusic.visibility = View.VISIBLE
                }
            }

            binding.layoutContent.setOnClickListener {
                listener.onItemClick(
                    position,
                    model
                )
            }
        }

        init {
            this.binding = binding
        }
    }
}