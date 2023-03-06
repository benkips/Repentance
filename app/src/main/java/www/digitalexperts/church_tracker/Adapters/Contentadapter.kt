package www.digitalexperts.church_tracker.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rajat.pdfviewer.PdfViewerActivity
import www.digitalexperts.church_tracker.models.PdfdataItem
import www.digitalexperts.church_traker.R
import www.digitalexperts.church_traker.databinding.ListinfBinding

class Contentadapter(val contentdata:ArrayList<PdfdataItem>) :RecyclerView.Adapter<RecyclerView.ViewHolder>() {


   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
           val binding = ListinfBinding.inflate(LayoutInflater.from(parent.context), parent, false)
           return ContentHolder(binding)

   }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
         if (holder !is ContentHolder) {
            return
        }

        val currentitem=contentdata[position];
        if(currentitem!=null){
            holder.bind(currentitem)
        }
    }
    override fun getItemCount(): Int {
          var itemCount: Int = contentdata.size
         return itemCount
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



}