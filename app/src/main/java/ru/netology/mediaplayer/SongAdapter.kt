package ru.netology.mediaplayer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.mediaplayer.data.SongObject
import ru.netology.mediaplayer.databinding.SongLayoutBinding

class SongAdapter(
    private val onInteractionListener: OnInteractionListener,
) : ListAdapter<SongObject, SongsListViewHolder>(SongDiffCallback()) {

    fun getNextId(position: Int): Int {
        return if (position == currentList.size) 0 else position
    }

    fun getNextSong(position: Int): SongObject {
        return getItem(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsListViewHolder {
        val binding = SongLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongsListViewHolder(binding, onInteractionListener)
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
            playButton.setOnClickListener {
                onInteractionListener.playOrPause(song)
            }
            playButton.icon = if (song.isPlayingNow) getDrawable(
                playButton.context,
                R.drawable.baseline_pause_32
            ) else
                getDrawable(playButton.context, R.drawable.baseline_play_arrow_32)
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