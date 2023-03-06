package www.digitalexperts.church_tracker.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import www.digitalexperts.church_tracker.models.ChurchesItem
import www.digitalexperts.church_traker.R
import www.digitalexperts.church_traker.databinding.ChurchinfBinding


class Churchadapter(private val churches: ArrayList<ChurchesItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

            val binding = ChurchinfBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return Churchholder(binding)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder !is Churchholder) {
            return
        }

        val currentitem = churches[position];
        if (currentitem != null) {
            holder.bind(currentitem)
        }
    }


    override fun getItemCount(): Int {
        var itemCount: Int = churches.size
        return itemCount
    }



    inner class Churchholder(val binding: ChurchinfBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: ChurchesItem) {
            binding.apply {
                Glide.with(itemView)
                    .load("https://repentanceandholinessinfo.com/photos/" + photo.photo)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(ivchurch)

                tvcname.text = photo.name
                tvlocation.text = photo.location
                ivcv.setOnClickListener {
                    val x=bundleOf(
                        "id" to photo.id,
                        "name" to photo.name,
                        "location" to photo.location,
                        "photo" to photo.photo
                    )
                    Navigation.findNavController(it).navigate(
                        R.id.action_nav_home_to_singleviewchurch,
                        x
                    )
                }
            }

        }
    }




}