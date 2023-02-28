package ru.netology.mediaplayer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.mediaplayer.data.SongObject
import ru.netology.mediaplayer.databinding.SongLayoutBinding

class SongAdapter() : ListAdapter<SongObject, SongsListViewHolder>(SongDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsListViewHolder {
        val binding = SongLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongsListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongsListViewHolder, position: Int) {
        val place = getItem(position)
        holder.bind(place)
    }
}

class SongsListViewHolder(
    private val binding: SongLayoutBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(song: SongObject) {
        binding.apply {
            songName.text = song.title
            albumName.text = "poka niche"
            songTime.text = "4:20 +${song.timeLength}"
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