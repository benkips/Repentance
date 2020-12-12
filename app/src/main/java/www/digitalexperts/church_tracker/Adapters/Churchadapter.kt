package www.digitalexperts.church_tracker.Adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.facebook.ads.*
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import www.digitalexperts.church_tracker.models.ChurchesItem
import www.digitalexperts.church_traker.R
import www.digitalexperts.church_traker.databinding.ChurchinfBinding


class Churchadapter(private val churches: ArrayList<ChurchesItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val AD_TYPE = 1
    private val DEFAULT_VIEW_TYPE = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == AD_TYPE) {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.templatefile2,
                parent,
                false
            )
            return adholderc(view)
        } else {
            val binding = ChurchinfBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return Churchholder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder !is Churchholder) {
            return
        }
        val itemPosition = position - position / 3
        val currentitem = churches[itemPosition];
        if (currentitem != null) {
            holder.bind(currentitem)
        }
    }


    override fun getItemCount(): Int {
        var itemCount: Int = churches.size
        itemCount += itemCount / 3
        return itemCount
    }

    override fun getItemViewType(position: Int): Int {
        return if (position > 1 && position % 3 == 0) {
            AD_TYPE
        } else DEFAULT_VIEW_TYPE
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

   inner class adholderc(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val template: TemplateView?

        init {
            template = itemView.findViewById(R.id.my_templatebc)
            val adRequest = AdRequest.Builder().build()
            val adLoader =
                AdLoader.Builder(itemView.context, "ca-app-pub-4814079884774543/2277771600")
                    .forUnifiedNativeAd { unifiedNativeAd ->
                        val styles: NativeTemplateStyle =
                            NativeTemplateStyle.Builder().withMainBackgroundColor(ColorDrawable(Color.WHITE))
                                .build()
                        template.setStyles(styles)
                        template.setNativeAd(unifiedNativeAd)
                    }
                    .withAdListener(object : AdListener() {
                        override fun onAdFailedToLoad(errorCode: Int) {
                            // Handle the failure by logging, altering the UI, and so on.
                        }

                        override fun onAdLoaded() {
                            super.onAdLoaded()
                            template.setVisibility(View.VISIBLE)
                        }
                    })
                    .build()
            if (adRequest != null && template != null) {
                adLoader.loadAd(adRequest)
            }
        }
    }


}