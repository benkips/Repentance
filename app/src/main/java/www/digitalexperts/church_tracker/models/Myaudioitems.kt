package www.digitalexperts.church_tracker.models

import androidx.recyclerview.widget.DiffUtil

data class Myaudioitems (
    val modified:Long,
    val name:String,
    val path:String
)

class DiffUtilRepositoriesModelItem: DiffUtil.ItemCallback<Myaudioitems>() {
    override fun areItemsTheSame(
        oldItem: Myaudioitems,
        newItem: Myaudioitems
    ): Boolean {
        return newItem.modified == oldItem.modified
    }

    override fun areContentsTheSame(
        oldItem: Myaudioitems,
        newItem: Myaudioitems
    ): Boolean {
        return newItem == oldItem
    }
}