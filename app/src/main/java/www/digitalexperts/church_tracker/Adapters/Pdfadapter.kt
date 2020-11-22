package www.digitalexperts.church_tracker.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.facebook.ads.*
import www.digitalexperts.church_tracker.fragments.PdfsDirections
import www.digitalexperts.church_tracker.models.FolderzItem
import www.digitalexperts.church_traker.R
import www.digitalexperts.church_traker.databinding.FolderinfBinding

class Pdfadapter(val pdfz: ArrayList<FolderzItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val AD_TYPE = 1
    private val DEFAULT_VIEW_TYPE = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == AD_TYPE) {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.templatefile,
                parent,
                false
            )
            return adholderc(view)
        } else {
            val binding =
                FolderinfBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return PdfviewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder !is PdfviewHolder) {
            return
        }
        val itemPosition = position - position / 5
        val currentitem = pdfz[itemPosition];
        if (currentitem != null) {
            holder.bind(currentitem)
        }
    }

    override fun getItemCount(): Int {
        var itemCount: Int = pdfz.size
        itemCount += itemCount / 5
        return itemCount
    }

    override fun getItemViewType(position: Int): Int {
        return if (position > 1 && position % 5 == 0) {
            AD_TYPE
        } else DEFAULT_VIEW_TYPE
    }


    inner class PdfviewHolder(val binding: FolderinfBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(datas: FolderzItem) {
            binding.apply {
                fname.text = datas.folder
                fcount.text = "(" + datas.count.toString() + ")"
                binding.cvf.setOnClickListener {
                    val x = "magazines"
                    val y = datas.folder
                    val action = PdfsDirections.actionPdfsToContent(x, y)
                    Navigation.findNavController(it).navigate(action)
                }
            }
        }
    }

    inner class adholderc(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var nativeAdLayout: NativeAdLayout? = null
        private var adView: LinearLayout? = null
        private val nativeBannerAd: NativeBannerAd?
        private fun inflateAd(nativeBannerAd: NativeBannerAd) {
            // Unregister last ad
            nativeBannerAd.unregisterView()

            // Add the Ad view into the ad container.
            nativeAdLayout = itemView.findViewById(R.id.native_banner_ad_container)
            val inflater = LayoutInflater.from(itemView.context)
            // Inflate the Ad view.  The layout referenced is the one you created in the last step.
            adView = inflater.inflate(
                R.layout.native_banner_ad_container,
                nativeAdLayout,
                false
            ) as LinearLayout
            nativeAdLayout?.addView(adView)

            // Add the AdChoices icon
            val adChoicesContainer =
                adView!!.findViewById<RelativeLayout>(R.id.ad_choices_container)
            val adOptionsView = AdOptionsView(itemView.context, nativeBannerAd, nativeAdLayout)
            adChoicesContainer.removeAllViews()
            adChoicesContainer.addView(adOptionsView, 0)

            // Create native UI using the ad metadata.
            val nativeAdTitle = adView!!.findViewById<TextView>(R.id.native_ad_title)
            val nativeAdSocialContext =
                adView!!.findViewById<TextView>(R.id.native_ad_social_context)
            val sponsoredLabel = adView!!.findViewById<TextView>(R.id.native_ad_sponsored_label)
            val nativeAdIconView: AdIconView = adView!!.findViewById(R.id.native_icon_view)
            val nativeAdCallToAction = adView!!.findViewById<Button>(R.id.native_ad_call_to_action)

            // Set the Text.
            nativeAdCallToAction.text = nativeBannerAd.adCallToAction
            nativeAdCallToAction.visibility =
                if (nativeBannerAd.hasCallToAction()) View.VISIBLE else View.INVISIBLE
            nativeAdTitle.text = nativeBannerAd.advertiserName
            nativeAdSocialContext.text = nativeBannerAd.adSocialContext
            sponsoredLabel.text = nativeBannerAd.sponsoredTranslation

            // Register the Title and CTA button to listen for clicks.
            val clickableViews: MutableList<View> = java.util.ArrayList()
            clickableViews.add(nativeAdTitle)
            clickableViews.add(nativeAdCallToAction)
            nativeBannerAd.registerViewForInteraction(adView, nativeAdIconView, clickableViews)
        }

        init {
            AudienceNetworkAds.initialize(itemView.context)
            nativeBannerAd = NativeBannerAd(
                itemView.context,
                "376366029998847_376367446665372"
            )
            nativeBannerAd.setAdListener(object : NativeAdListener {
                override fun onMediaDownloaded(ad: Ad) {}
                override fun onError(ad: Ad, adError: AdError) {
                    // Toast.makeText(itemView.getContext(), adError.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }

                override fun onAdLoaded(ad: Ad) {
                    // Race condition, load() called again before last ad was displayed
                    if (nativeBannerAd == null || nativeBannerAd !== ad) {
                        return
                    }
                    // Inflate Native Banner Ad into Container
                    inflateAd(nativeBannerAd)
                }

                override fun onAdClicked(ad: Ad) {}
                override fun onLoggingImpression(ad: Ad) {}
            })
            // load the ad
            nativeBannerAd.loadAd()
        }

    }

}