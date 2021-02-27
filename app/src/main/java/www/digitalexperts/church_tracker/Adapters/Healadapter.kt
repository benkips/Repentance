package www.digitalexperts.church_tracker.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import cn.onestravel.fivefiveplayer.interf.PlayerInterface
import cn.onestravel.fivefiveplayer.kernel.ExoPlayerKernel


import www.digitalexperts.church_tracker.models.Healings
import www.digitalexperts.church_traker.databinding.VideostuffBinding

class Healadapter (private val listener: OnItemClickListner) :
    PagingDataAdapter<Healings, Healadapter.HealViewHolder>(
        HEAL_COMPARATOR
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HealViewHolder {
        val binding=VideostuffBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return HealViewHolder(binding)
    }
    override fun onBindViewHolder(holder: HealViewHolder, position: Int) {
        val currentitem=getItem(position);
        if(currentitem!=null){
            holder.bind(currentitem)
        }
    }

    inner class HealViewHolder(private val binding: VideostuffBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }


            }
        }

        fun bind(heals: Healings) {
            binding.tvvmsgs.text = heals.message

            val sdf = java.text.SimpleDateFormat("dd/MM/yyyy")
            val date = java.util.Date(heals.time.toLong() * 1000)
            binding.tme.text=sdf.format(date)


            val  path ="http://repentanceandholinessinfo.com/static/uploads/"+heals.video
            //Set the current player core to ExoPlayer core
            binding.fiveVideoView.setMediaKernelClass ( ExoPlayerKernel::class.java)
            binding.fiveVideoView.setDataSource(path)
            binding.fiveVideoView.apply {
                doubleClickPlay=false
                clickPlay=true
                showLoadingViewEnable=true
                gestureControlEnable=true

            }

            binding.fiveVideoView.setOnPreparedListener {
                binding.fiveVideoView.setVideoDisplayType( PlayerInterface . VIDEO_DISPLAY_TYPE_FIT_CENTER )
            }

        }
    }

    interface OnItemClickListner {
        fun onItemClick(heals: Healings) {

        }
    }

    companion object {
        private val HEAL_COMPARATOR = object : DiffUtil.ItemCallback<Healings>() {
            override fun areItemsTheSame(oldItem: Healings, newItem: Healings) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Healings, newItem: Healings
            ) = oldItem == newItem

        }
    }


}