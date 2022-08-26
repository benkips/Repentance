package www.digitalexperts.church_tracker.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import www.digitalexperts.church_tracker.fragments.PdfsDirections
import www.digitalexperts.church_tracker.models.FolderzItem
import www.digitalexperts.church_traker.databinding.FolderinfBinding

class Pdfadapter(val pdfz: ArrayList<FolderzItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

            val binding =
                FolderinfBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return PdfviewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder !is PdfviewHolder) {
            return
        }
        val currentitem = pdfz[position];
        if (currentitem != null) {
            holder.bind(currentitem)
        }
    }

    override fun getItemCount(): Int {
        var itemCount: Int = pdfz.size
        return itemCount
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


}