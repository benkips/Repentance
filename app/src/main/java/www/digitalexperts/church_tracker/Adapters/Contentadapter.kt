package www.digitalexperts.church_tracker.Adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.ads.*
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
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
               R.layout.templatefile2,
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
        private val template: TemplateView?

        init {
            template = itemView.findViewById(R.id.my_templatebc)
            val adRequest = AdRequest.Builder().build()
            val adLoader =
                AdLoader.Builder(itemView.context, "ca-app-pub-4814079884774543/2277771600")
                    .forUnifiedNativeAd { unifiedNativeAd ->
                        val styles: NativeTemplateStyle =
                            NativeTemplateStyle.Builder().withMainBackgroundColor(
                                ColorDrawable(
                                    Color.WHITE)
                            )
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