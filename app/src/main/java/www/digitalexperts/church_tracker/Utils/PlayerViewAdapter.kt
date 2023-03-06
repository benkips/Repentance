package www.digitalexperts.church_tracker.Utils

import com.google.android.exoplayer2.ExoPlayer

class PlayerViewAdapter {
    companion object{
        // for hold all players generated
        var playersMap: MutableMap<Int, ExoPlayer>  = mutableMapOf()
        // for hold current player
        private var currentPlayingVideo: Pair<Int, ExoPlayer>? = null
        fun releaseAllPlayers(){
            playersMap.map {
                it.value.release()
            }
        }

        // call when item recycled to improve performance
        fun releaseRecycledPlayers(index: Int){
            playersMap[index]?.release()
        }

        // call when scroll to pause any playing player
        private fun pauseCurrentPlayingVideo(){
            if (currentPlayingVideo != null){
                currentPlayingVideo?.second?.playWhenReady = false
            }
        }

        fun playIndexThenPausePreviousPlayer(index: Int){
            if (playersMap[index]?.playWhenReady == false) {
                pauseCurrentPlayingVideo()
                playersMap[index]?.playWhenReady = true
                currentPlayingVideo = Pair(index, playersMap[index]!!)
            }

        }



    }
}