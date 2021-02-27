package www.digitalexperts.church_tracker.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import www.digitalexperts.church_traker.databinding.HealLoadStateFooterBinding

class HealingLoadStateAdapter(private  val retry:()->Unit) : LoadStateAdapter<HealingLoadStateAdapter.LoadStateViewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewholder {
        val binding=HealLoadStateFooterBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return LoadStateViewholder(binding)
    }

    override fun onBindViewHolder(holder: LoadStateViewholder, loadState: LoadState) {

        holder.bind(loadState)
    }

    inner class LoadStateViewholder(private val binding: HealLoadStateFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.buttonretry.setOnClickListener{
                retry.invoke()
            }
        }
        fun bind(loadState: LoadState){
            binding.apply {
                pgbar.isVisible= loadState is LoadState.Loading
                buttonretry.isVisible= loadState !is LoadState.Loading
                textViewError.isVisible= loadState !is LoadState.Loading

            }

        }

    }
}