package www.digitalexperts.church_tracker.Adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import www.digitalexperts.church_tracker.models.DiffUtilRepositoriesModelItem
import www.digitalexperts.church_tracker.models.Myaudioitems
import www.digitalexperts.church_traker.databinding.ItemAudioBinding
import java.io.File


class Recordingsadapter(private val listener: OnitemclicKListenera) :
    ListAdapter<Myaudioitems, Recordingsadapter.itemholder>(
        DiffUtilRepositoriesModelItem()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itemholder {
        val binding = ItemAudioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return itemholder(binding)
    }

    override fun onBindViewHolder(holder: itemholder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class itemholder(val binding: ItemAudioBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        fun bind(aud: Myaudioitems) {
            binding.apply {
                audioName.text = aud.name
            }
            binding.deleteaudio.setOnClickListener {
                val uri: String = aud.path
                val file = File(uri)
                file.delete()
                listener.onDelete(position)
            }
            itemView.setOnClickListener {
                val uri: String = aud.path
                listener.playsong(uri)
            }
            binding.shareaudio.setOnClickListener {
                val uri: String = aud.path
                listener.shareaudio(uri)
            }
        }

        init {
            binding.root.setOnClickListener { this }
        }

        override fun onClick(v: View?) {
            val pos:Int=adapterPosition
            if(pos!=RecyclerView.NO_POSITION){
                listener.onDelete(pos)
            }
        }

    }

    interface OnitemclicKListenera {
        fun onDelete(position: Int)
        fun playsong( u:String)
        fun shareaudio(u:String)
    }


}