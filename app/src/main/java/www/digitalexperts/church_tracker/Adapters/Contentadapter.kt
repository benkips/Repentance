package www.digitalexperts.church_tracker.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.ads.*
import com.rajat.pdfviewer.PdfViewerActivity
import www.digitalexperts.church_tracker.models.PdfdataItem
import www.digitalexperts.church_traker.R
import www.digitalexperts.church_traker.databinding.ListinfBinding

class Contentadapter(val contentdata:ArrayList<PdfdataItem>) :RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val AD_TYPE = 1
    private val DEFAULT_VIEW_TYPE = 2
   /* override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentHolder {
        val binding = ListinfBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContentHolder(binding)
    }

    override fun onBindViewHolder(holder: ContentHolder, position: Int) {
        val currentitem=contentdata[position];
        if(currentitem!=null){
            holder.bind(currentitem)
        }
    }*/
   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       if (viewType == AD_TYPE) {
           val view = LayoutInflater.from(parent.context).inflate(
               R.layout.templatefile,
               parent,
               false
           )
           return adholderc(view)
       } else {
           val binding = ListinfBinding.inflate(LayoutInflater.from(parent.context), parent, false)
           return ContentHolder(binding)
       }
   }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
         if (holder !is ContentHolder) {
            return
        }
        val itemPosition = position - position / 5
        val currentitem=contentdata[itemPosition];
        if(currentitem!=null){
            holder.bind(currentitem)
        }
    }
    override fun getItemCount(): Int {
          var itemCount: Int = contentdata.size
        itemCount += itemCount / 5
        return itemCount
    }


    override fun getItemViewType(position: Int): Int {
        return if (position > 1 && position % 5 == 0) {
            AD_TYPE
        } else DEFAULT_VIEW_TYPE
    }

    inner class ContentHolder(val binding:ListinfBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(contentdata: PdfdataItem){
            binding.ltitle.text=contentdata.title
            binding.ivl.setImageResource(R.drawable.pdficon)
            binding.cvl.setOnClickListener {
                itemView.context.startActivity(
                        PdfViewerActivity.buildIntent(
                                itemView.context,
                                "https://repentanceandholinessinfo.com/documents/"+contentdata.document,                                // PDF URL in String format
                                false,
                                contentdata.title,                        // PDF Name/Title in String format
                                "",    // If nothing specific, Put "" it will save to Downloads
                                enableDownload = true                    // This param is true by defualt.
                        )
                )
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