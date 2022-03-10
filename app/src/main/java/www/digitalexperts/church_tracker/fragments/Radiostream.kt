package www.digitalexperts.church_tracker.fragments

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
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
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.beraldo.playerlib.PlayerService
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
import org.jetbrains.anko.AnkoLogger
import www.digitalexperts.church_tracker.Utils.*
import www.digitalexperts.church_tracker.index
import www.digitalexperts.church_traker.R
import www.digitalexperts.church_traker.databinding.FragmentRadiostreamBinding
import java.io.File
import java.io.IOException
import java.util.*
@RequiresApi(Build.VERSION_CODES.FROYO)
class Radiostream : Fragment(R.layout.fragment_radiostream)  , AnkoLogger {

    private var isPlaying = false

    private var _binding: FragmentRadiostreamBinding? = null
    private val binding get() = _binding!!

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private var fileName: String? = null
    private var recorder: MediaRecorder? = null

    private var datez: String? = null
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
        //Toast.makeText(context, urls, Toast.LENGTH_SHORT).show()
        initMediaControls()
        //Start the service
        val intent = Intent(context, PlayerService::class.java).apply {
            putExtra(PlayerService.STREAM_URL, url)
        }
        activity?.applicationContext!!.bindService(intent, connection, Context.BIND_AUTO_CREATE)

        /*Environment.getExternalStorageDirectory().toString() + File.separator + "recordings/"*/
        fileName =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
                .toString() + File.separator + "Recordings/"


        //Create  folder if it does not exist
        val exportDir = File(fileName)
        //val exportDir = File(context?.getExternalFilesDir( Environment.DIRECTORY_MUSIC).toString())

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
            datez = "JESUS_is_lORD_radio_$dates"

            val file = File(fileName, "JESUS_is_lORD_radio_$dates.m4a")
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
        binding.endtimemsg.setOnClickListener { view ->
            val c = "http://node-15.zeno.fm/gmdx1sb97f8uv?rj-ttl=5&rj-tok=AAABfccRdpIA8mopC5CghSrEoA"
            Navigation.findNavController(view).navigate(R.id.wvinfo, bundleOf("web" to c))
        }
        binding.playnow.setOnClickListener { v ->
            val externalStorageDirectory = File(fileName)
            val folder = File(externalStorageDirectory.absolutePath)
            val file = context?.readmyaudios()
            if (file != null) {
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
            } else {
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
    }

    private val adSize: AdSize
        get() {
            val display = activity?.windowManager!!.defaultDisplay
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
            val desc: ParcelFileDescriptor = requireContext().createaudiofile("$datez.m4a")
            recorder?.setOutputFile(desc.fileDescriptor)
        } else {
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
            override fun getCurrentSubText(player: Player): CharSequence? {
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
            requireContext(),
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
       // playerNotificationManager.setPlayer(exoPlayer)
    }

    private fun initMediaControls() {
        initPlayButton()
    }

    private fun initPlayButton() {
        binding.btnPlay.requestFocus()
        binding.btnPlay.setOnClickListener { setPlayPause(!isPlaying) }
        binding.rec.visibility=View.VISIBLE
    }

    /**
     * Starts or stops playback. Also takes care of the Play/Pause button toggling
     *
     * @param play True if playback should be started
     */
    private fun setPlayPause(play: Boolean) {
        isPlaying = play
        if (!isPlaying) {
            binding.btnPlay.setImageResource(android.R.drawable.ic_media_play)
        } else {
            binding.btnPlay.setImageResource(android.R.drawable.ic_media_pause)
        }
    }
    private val connection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {}
        /*
         * Called after a successful bind with our PlayerService.
         */
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            if (service is PlayerService.PlayerServiceBinder) {
                //service.getPlayerHolderInstance() // use the player and call methods on it to start and stop
                binding.btnPlay.setOnClickListener {
                    if (!isPlaying) {
                        Toast.makeText(context, "Playing..", Toast.LENGTH_LONG).show()
                        service.getPlayerHolderInstance().start()
                        setPlayPause(true)
                    } else {
                        Toast.makeText(context, "Pausing..", Toast.LENGTH_LONG).show()
                        service.getPlayerHolderInstance().stop()
                        setPlayPause(false)
                    }
                }


            }else{
                setPlayPause(false)
            }
        }
    }
}