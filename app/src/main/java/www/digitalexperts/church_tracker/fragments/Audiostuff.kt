package www.digitalexperts.church_tracker.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import www.digitalexperts.church_tracker.Adapters.Recordingsadapter
import www.digitalexperts.church_tracker.models.Myaudioitems
import www.digitalexperts.church_traker.R
import www.digitalexperts.church_traker.databinding.FragmentAudiostuffBinding
import java.io.File
import java.util.*

class Audiostuff : Fragment(R.layout.fragment_audiostuff),Recordingsadapter.OnitemclicKListenera {

    private var _binding: FragmentAudiostuffBinding? = null
    private val binding get() = _binding!!

    private lateinit var recordadapter: Recordingsadapter

    private var filename: String? = null
    var audioArrayList: ArrayList<Myaudioitems> = ArrayList<Myaudioitems>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentAudiostuffBinding.bind(view)

        Loadsongs()
    }
    private fun Loadsongs() {
        filename =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).toString() + File.separator + "Recordings/"
        val externalStorageDirectory = File(filename)
        val folder = File(externalStorageDirectory.absolutePath)
        val file = folder.listFiles()
        if (file.isNotEmpty()) {
            for (i in file.indices) {
                audioArrayList.add(
                    Myaudioitems(
                        file[i].lastModified(),
                        file[i].name,
                        file[i].absolutePath
                    )
                )
                recordadapter= Recordingsadapter(this)
                binding.rvaudio.also {
                    it.layoutManager = LinearLayoutManager(requireContext())
                    it.setHasFixedSize(true)
                    it.adapter=recordadapter

                }
                recordadapter.submitList(audioArrayList)
            }
        } else {
            Toast.makeText(context, "No Music downloaded yet", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onDelete(position: Int) {
        audioArrayList.removeAt(position)
        recordadapter.notifyItemRemoved(position)
        recordadapter.submitList(audioArrayList)
    }

    override fun playsong(u: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        val file = File(u)
        val apkURI: Uri = FileProvider.getUriForFile(
            requireContext(), requireContext().applicationContext
                .packageName.toString() + ".fileprovider", file
        )
        intent.setDataAndType(apkURI, "audio/mp3")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val activities: List<ResolveInfo> = activity?.packageManager!!.queryIntentActivities(
            intent,
            PackageManager.MATCH_DEFAULT_ONLY
        )
        val isIntentSafe: Boolean = activities.isNotEmpty()
        if(isIntentSafe) {
            try {
                activity?.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    override fun shareaudio(u: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        val file = File(u)
        val shareuri: Uri = FileProvider.getUriForFile(
            requireContext(), requireContext().applicationContext
                .packageName.toString() + ".fileprovider", file
        )
        intent.setType("audio/mp3")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.putExtra(Intent.EXTRA_STREAM, shareuri)

        try {
            val chooser = Intent.createChooser(intent, "Send To")
            activity?.startActivity(chooser)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }


}