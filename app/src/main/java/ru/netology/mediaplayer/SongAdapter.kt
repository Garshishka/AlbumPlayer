package ru.netology.mediaplayer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import ru.netology.mediaplayer.data.SongObject
import ru.netology.mediaplayer.databinding.SongLayoutBinding

class SongAdapter(
    private val onInteractionListener: OnInteractionListener,
) : ListAdapter<SongObject, SongsListViewHolder>(SongDiffCallback()) {

    fun getNextId(position: Int): Int{
        return if (position == currentList.size) 0 else position
    }
    fun getNextSong(position: Int) : Pair<String, MaterialButton> {
        return getItem(position).file to getItem(position).playButton!!
    }
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
    val playButton = binding.playButton
    fun bind(song: SongObject) {
        binding.apply {
            songName.text = song.file
            albumName.text = song.album
            playButton.setOnClickListener {
                onInteractionListener.playOrPause(song.file, it as MaterialButton,song.id)
            }
            //That's a hack to get next song button
            song.playButton = playButton
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