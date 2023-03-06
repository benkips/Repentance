package www.digitalexperts.church_tracker.Adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import www.digitalexperts.church_tracker.Utils.PlayerStateCallback
import www.digitalexperts.church_tracker.Utils.PlayerViewAdapter
import www.digitalexperts.church_tracker.Utils.PlayerViewAdapter.Companion.playersMap


import www.digitalexperts.church_tracker.models.Healings
import www.digitalexperts.church_traker.databinding.VideostuffBinding

class Healadapter (private val listener: OnItemClickListner) :
    PagingDataAdapter<Healings, Healadapter.HealViewHolder>(
        HEAL_COMPARATOR
    ) ,
    PlayerStateCallback {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HealViewHolder {
        val binding=VideostuffBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return HealViewHolder(binding)
    }
    override fun onBindViewHolder(holder: HealViewHolder, position: Int) {
        val currentitem=getItem(position);
        if(currentitem!=null){
            holder.bind(currentitem)
        }
    }

    inner class HealViewHolder(private val binding: VideostuffBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }


            }
        }

        fun bind(heals: Healings) {
            binding.tvvmsgs.text = heals.message

            val sdf = java.text.SimpleDateFormat("dd/MM/yyyy")
            val date = java.util.Date(heals.time.toLong() * 1000)
            binding.tme.text=sdf.format(date)


            /*val  path ="http://mobile.repentanceandholinessinfo.com/static/uploads/"+heals.video*/
            val  path =heals.vidlink

            var Player= ExoPlayer.Builder(itemView.context).build()
            binding.itemVideoExoplayer.loadVideo(
                url = path,
                callback = this@Healadapter,
                progressbar = binding.progressBar,
                item_index = adapterPosition,
                autoPlay = false,
                player=Player

            )




        }


    }

    interface OnItemClickListner {
        fun onItemClick(heals: Healings) {
        }
    }

    companion object {
        private val HEAL_COMPARATOR = object : DiffUtil.ItemCallback<Healings>() {
            override fun areItemsTheSame(oldItem: Healings, newItem: Healings) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Healings, newItem: Healings
            ) = oldItem == newItem

        }
    }

    fun StyledPlayerView.loadVideo(url: String, callback: PlayerStateCallback, progressbar: ProgressBar, item_index: Int? = null, autoPlay: Boolean = false,player: ExoPlayer) {

        if (url == null) return


        player.playWhenReady = autoPlay
        player.repeatMode = Player.REPEAT_MODE_ALL
        // When changing track, retain the latest frame instead of showing a black screen
        setKeepContentOnPlayerReset(true)
        // We'll show the controller, change to true if want controllers as pause and start
        this.useController = false
        // Provide url to load the video from here
        val dataSourceFactory = DefaultHttpDataSource.Factory()
            .setDefaultRequestProperties(hashMapOf("Header" to "Value"))
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(
            MediaItem.fromUri(Uri.parse(url)))
        player.prepare(mediaSource)

        this.player = player

        // add player with its index to map

        if (playersMap.containsKey(item_index))
            playersMap.remove(item_index)
        if (item_index != null)
            playersMap[item_index] = player

        this.player!!.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == ExoPlayer.STATE_BUFFERING) {
                    callback.onVideoBuffering(player)
                    // Buffering..
                    // set progress bar visible here
                    // set thumbnail visible
                    progressbar.visibility = View.VISIBLE
                }
                if (playbackState == ExoPlayer.STATE_READY) {
                    // [PlayerView] has fetched the video duration so this is the block to hide the buffering progress bar
                    progressbar.visibility = View.GONE
                    // set thumbnail gone
                    //humbnail.visibility = View.GONE
                    callback.onVideoDurationRetrieved(this@loadVideo.player!!.duration, player)
                }
                if (playbackState == Player.STATE_READY && player.playWhenReady){
                    // [PlayerView] has started playing/resumed the video
                    callback.onStartedPlaying(player)
                }
            }
        })
    }

    override fun onVideoDurationRetrieved(duration: Long, player: Player) {
    }

    override fun onVideoBuffering(player: Player) {
    }

    override fun onStartedPlaying(player: Player) {
    }

    override fun onFinishedPlaying(player: Player) {
    }
}


