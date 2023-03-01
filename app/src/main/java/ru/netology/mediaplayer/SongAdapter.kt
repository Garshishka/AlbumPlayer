package ru.netology.mediaplayer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.mediaplayer.data.SongObject
import ru.netology.mediaplayer.databinding.SongLayoutBinding

class SongAdapter(
    private val onInteractionListener: OnInteractionListener,
) : ListAdapter<SongObject, SongsListViewHolder>(SongDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsListViewHolder {
        val binding = SongLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongsListViewHolder(binding,onInteractionListener)
    }

    override fun onBindViewHolder(holder: SongsListViewHolder, position: Int) {
        val place = getItem(position)
        holder.bind(place)
    }
}

class SongsListViewHolder(
    private val binding: SongLayoutBinding,
    private val onInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(song: SongObject) {
        binding.apply {
            songName.text = song.file
            albumName.text = song.album
            songTime.text = "${song.timeLength}"
            playButton.setOnClickListener {
                onInteractionListener.play(song.file)
            }
        }

    }
}

class SongDiffCallback : DiffUtil.ItemCallback<SongObject>() {
    override fun areItemsTheSame(oldItem: SongObject, newItem: SongObject): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SongObject, newItem: SongObject): Boolean {
        return oldItem == newItem
    }
}