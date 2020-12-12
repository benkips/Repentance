package www.digitalexperts.church_tracker.models

import android.net.Uri
import androidx.recyclerview.widget.DiffUtil

data class Myaudioitems (
    val uri: Uri,
    val name: String,
    val duration: Int,
    val size: Int
)

class DiffUtilRepositoriesModelItem: DiffUtil.ItemCallback<Myaudioitems>() {
    override fun areItemsTheSame(
        oldItem: Myaudioitems,
        newItem: Myaudioitems
    ): Boolean {
        return newItem.size == oldItem.size
    }

    override fun areContentsTheSame(
        oldItem: Myaudioitems,
        newItem: Myaudioitems
    ): Boolean {
        return newItem == oldItem
    }
}