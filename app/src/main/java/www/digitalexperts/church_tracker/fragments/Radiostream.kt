package www.digitalexperts.church_tracker.fragments

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.graphics.Bitmap
import android.media.MediaRecorder
import android.net.Uri
import android.os.*
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import www.digitalexperts.church_tracker.Utils.*
import www.digitalexperts.church_tracker.index
import www.digitalexperts.church_traker.BuildConfig
import www.digitalexperts.church_traker.R
import www.digitalexperts.church_traker.databinding.FragmentRadiostreamBinding
import java.io.File
import java.io.IOException
import java.util.*

class Radiostream : Fragment(R.layout.fragment_radiostream) {
    private var playbackStateListener: PlaybackStateListener? = null
    private var exoPlayer: SimpleExoPlayer?=null

    private var handler: Handler? = null
    private var isPlaying = false

    private var _binding: FragmentRadiostreamBinding? = null
    private val binding get() = _binding!!

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private var fileName: String? = null
    private var recorder: MediaRecorder? = null

    private var datez:String?=null
    val BITS_PER_SAMPLE = 16 // 16-bit data

    val NUMBER_CHANNELS = 1 // Mono

    val COMPRESSION_AMOUNT = 8

    /*0775967323*/
    private var filenamea: String? = null
    private lateinit var adView: AdView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentRadiostreamBinding.bind(view)
        recorder = MediaRecorder()
        checkpermission()
        requestrecodingPermission()

        val url = "https://s3.radio.co/s97f38db97/listen"
        prepareExoPlayerFromURL(Uri.parse(url))

        /*Environment.getExternalStorageDirectory().toString() + File.separator + "recordings/"*/
        fileName =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).toString() + File.separator + "Recordings/"


        //Create  folder if it does not exist
        val exportDir = File(fileName)

        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }

        binding.rec.setOnClickListener {
            val c = Calendar.getInstance()
            val sec = c[Calendar.MILLISECOND]
            val date = c[Calendar.DAY_OF_MONTH]
            val m = c[Calendar.MONTH]
            val y = c[Calendar.YEAR]
            val dates = date.toString() + "_" + (m + 1) + "_" + y + "_" + sec
            datez="JESUS_is_lORD_radio_$dates"

            val file= File(fileName, "JESUS_is_lORD_radio_$dates.m4a")
            filenamea = file.absolutePath
            binding.txtrecord.text = "recording..."
            startRecording()
            binding.spt.visibility = View.VISIBLE
            Toast.makeText(
                context,
                "recording has started: for the best experience please remove your headphones",
                Toast.LENGTH_LONG
            ).show()
        }

        binding.spt.setOnClickListener {
            binding.txtrecord.text = "record"
            stopRecording()
            Toast.makeText(
                context,
                "recording has stopped",
                Toast.LENGTH_SHORT
            ).show()
            binding.spt.visibility = View.GONE
        }
        binding.mrradio.setOnClickListener { view ->
            val c = "https://repentanceandholinessinfo.com/playradio.php"
            Navigation.findNavController(view).navigate(R.id.wvinfo, bundleOf("web" to c))
        }
        binding.playnow.setOnClickListener { v ->
            val externalStorageDirectory= File(fileName)
            val folder = File(externalStorageDirectory.absolutePath)
            val file = context?.readmyaudios()
            if (file!=null){
                Log.d(TAG, "the file is "+file[0])
            if (file.size != 0) {
                Navigation.findNavController(v).navigate(R.id.action_live_to_audiostuff)
            } else {
                //no file available
                Toast.makeText(
                    context,
                    "You have no recordings currently",
                    Toast.LENGTH_SHORT
                ).show()
            }
            }else{
                //no file available
                Toast.makeText(
                    context,
                    "You have no recordings currently",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        adView = AdView(context)
        binding.bannerContainertwo.addView(adView)
        adView.adUnitId = "ca-app-pub-4814079884774543/6358507489"

        adView.adSize = adSize
        val adRequest = AdRequest
            .Builder()
            .build()
        // Start loading the ad in the background.
        adView.loadAd(adRequest)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        exoPlayer?.release()
        exoPlayer=null
    }
    private val adSize: AdSize
        get() {
            val display =activity?.windowManager!!.defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)

            val density = outMetrics.density

            var adWidthPixels = binding.bannerContainertwo.width.toFloat()
            if (adWidthPixels == 0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
            }

            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
        }
    private fun prepareExoPlayerFromURL(uri: Uri) {
        val trackSelector = DefaultTrackSelector()
        val loadControl: LoadControl = DefaultLoadControl()

        exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl)
        val userAgent = Util.getUserAgent(context, BuildConfig.APPLICATION_ID)
        val mediaSource = ExtractorMediaSource(
            uri,
            DefaultDataSourceFactory(context, userAgent),
            DefaultExtractorsFactory(),
            null,
            null
        )
        playbackStateListener = PlaybackStateListener()
        exoPlayer?.addListener(playbackStateListener)
        exoPlayer?.prepare(mediaSource)

        initMediaControls()

    }

    private fun initMediaControls() {
        initPlayButton()
        initSeekBar()
    }

    private fun initPlayButton() {
        binding.btnPlay.requestFocus()
        binding.btnPlay.setOnClickListener { setPlayPause(!isPlaying) }
    }

    /**
     * Starts or stops playback. Also takes care of the Play/Pause button toggling
     *
     * @param play True if playback should be started
     */
    private fun setPlayPause(play: Boolean) {
        isPlaying = play
        exoPlayer?.playWhenReady = play
        if (!isPlaying) {
            binding.btnPlay.setImageResource(android.R.drawable.ic_media_play)
        } else {
            setProgress()
            binding.btnPlay.setImageResource(android.R.drawable.ic_media_pause)
        }
    }


    private fun stringForTime(timeMs: Int): String? {
        val mFormatBuilder: StringBuilder
        val mFormatter: Formatter
        mFormatBuilder = StringBuilder()
        mFormatter = Formatter(mFormatBuilder, Locale.getDefault())
        val totalSeconds = timeMs / 1000
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600
        mFormatBuilder.setLength(0)
        return if (hours > 0) {
            mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
        } else {
            mFormatter.format("%02d:%02d", minutes, seconds).toString()
        }
    }

    private fun setProgress() {
        binding.mediacontrollerProgress.progress = 0
        binding.mediacontrollerProgress.max = exoPlayer?.duration!!.toInt() / 1000
        binding.timeCurrent.text = stringForTime(exoPlayer?.currentPosition!!.toInt())
        binding.playerEndTime.text = stringForTime(exoPlayer?.duration!!.toInt())
        if (handler == null) handler = Handler()
        //Make sure you update Seekbar on UI thread
        handler!!.post(object : Runnable {
            override fun run() {
                    if (exoPlayer != null && isPlaying) {
                        binding.mediacontrollerProgress.max = exoPlayer?.duration!!.toInt() / 1000
                        val mCurrentPosition = exoPlayer?.currentPosition!!.toInt() / 1000
                        binding.mediacontrollerProgress.progress = mCurrentPosition
                        binding.timeCurrent.text = stringForTime(exoPlayer?.currentPosition!!.toInt())
                        binding.playerEndTime.text = stringForTime(exoPlayer?.duration!!.toInt())
                        handler!!.postDelayed(this, 1000)
                    }


            }
        })
    }

    private fun initSeekBar() {
        binding.mediacontrollerProgress.requestFocus()
        binding.mediacontrollerProgress.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (!fromUser) {
                    // We're not interested in programmatically generated changes to
                    // the progress bar's position.
                    return
                }
                exoPlayer?.seekTo(progress * 1000.toLong())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        binding.mediacontrollerProgress.max = 0
        binding.mediacontrollerProgress.max = exoPlayer?.duration!!.toInt() / 1000
    }

    private inner class PlaybackStateListener : Player.EventListener {

        override fun onPlayerStateChanged(
            playWhenReady: Boolean,
            playbackState: Int
        ) {
            val stateString: String
            when (playbackState) {
                ExoPlayer.STATE_IDLE -> {
                    stateString = "ExoPlayer.STATE_IDLE      -"
                }
                ExoPlayer.STATE_BUFFERING -> {
                    stateString = "ExoPlayer.STATE_BUFFERING -"
                    binding.pgbar.visibility = View.VISIBLE
                }
                ExoPlayer.STATE_READY -> {
                    initListener()
                    stateString = "ExoPlayer.STATE_READY     -"
                    binding.pgbar.visibility = View.GONE
                    binding.rec.visibility = View.VISIBLE
                    binding.txtrecord.visibility = View.VISIBLE
                    Log.i(
                        TAG, "ExoPlayer ready! pos: " + exoPlayer?.currentPosition
                                + " max: " + stringForTime(exoPlayer?.duration!!.toInt())
                    )
                    setProgress()
                }
                ExoPlayer.STATE_ENDED -> {
                    stateString = "ExoPlayer.STATE_ENDED     -"
                    //Stop playback and return to start position
                    setPlayPause(false)
                    exoPlayer?.seekTo(0)
                }
                else -> stateString = "UNKNOWN_STATE             -"
            }
            Log.d(
                TAG, "changed state to " + stateString
                        + " playWhenReady: " + playWhenReady
            )
        }
    }

    companion object {
        private val TAG = index::class.java.name
    }

    private fun checkpermission() {
        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (!isGranted) {
                    requestrecodingPermission()
                }
            }
    }

    //asking for permission
    private fun requestrecodingPermission() {
        when {
            requireContext().permissionGranted(Manifest.permission.RECORD_AUDIO) -> {
                //to record
            }
            shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO) -> {
                requireContext().showPermissionRequestExplanation(
                    getString(R.string.recordone),
                    getString(R.string.permission_requestone)
                ) {
                    requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    private fun startRecording() {
        val uncompressedBitRate: Int = 8000 * BITS_PER_SAMPLE * NUMBER_CHANNELS
        val encodedBitRate: Int = uncompressedBitRate / COMPRESSION_AMOUNT

        recorder = MediaRecorder()

        recorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        recorder?.setAudioSamplingRate(8000)
        recorder?.setAudioEncodingBitRate(encodedBitRate)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val desc:ParcelFileDescriptor=requireContext().createaudiofile("$datez.m4a")
            recorder?.setOutputFile(desc.fileDescriptor)
        }else{
            recorder?.setOutputFile(filenamea)
        }

        recorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

        try {
            recorder?.prepare()
        } catch (e: IOException) {
            Log.e(TAG, "prepare() failed" + e.toString())
        }

        recorder?.start()
    }

    private fun stopRecording() {
        recorder?.stop()
        recorder?.release()
        recorder = null
    }

    override fun onStop() {
        super.onStop()
        if (recorder != null) {
            recorder!!.release()
            recorder = null
        }
    }

    private fun initListener() {
        val playerNotificationManager: PlayerNotificationManager
        val notificationId = 1234
        val mediaDescriptionAdapter = object : PlayerNotificationManager.MediaDescriptionAdapter {
            override fun getCurrentSubText(player: Player?): String {
                return "Live"
            }

            override fun getCurrentContentTitle(player: Player): String {
                return "JESUS is LORD RADIO"
            }

            override fun createCurrentContentIntent(player: Player): PendingIntent? {
                return null
            }

            override fun getCurrentContentText(player: Player): String {
                return "Live broadcasting"
            }

            override fun getCurrentLargeIcon(
                player: Player,
                callback: PlayerNotificationManager.BitmapCallback
            ): Bitmap? {
               /*return AppCompatResources.getDrawable(context!!, R.drawable.jslord)?.toBitmap()*/
                return requireContext().getBitmapFromVectorDrawable(R.drawable.jslord)
            }
        }

        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
            context,
            "My_channel_id",
            R.string.app_name,
            notificationId,
            mediaDescriptionAdapter,
            object : PlayerNotificationManager.NotificationListener {
                override fun onNotificationPosted(
                    notificationId: Int,
                    notification: Notification,
                    ongoing: Boolean
                ) {

                }

                override fun onNotificationCancelled(
                    notificationId: Int,
                    dismissedByUser: Boolean
                ) {
                }

            })

        playerNotificationManager.setUseNavigationActions(false)
        playerNotificationManager.setUseNavigationActionsInCompactView(false)
        playerNotificationManager.setRewindIncrementMs(0);
        playerNotificationManager.setFastForwardIncrementMs(0)
        playerNotificationManager.setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
        playerNotificationManager.setPlayer(exoPlayer)
    }




}