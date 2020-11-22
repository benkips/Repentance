package www.digitalexperts.church_tracker.fragments

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils.substring
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import www.digitalexperts.church_tracker.Utils.permissionGranted
import www.digitalexperts.church_tracker.Utils.showPermissionRequestExplanation
import www.digitalexperts.church_traker.R
import www.digitalexperts.church_traker.databinding.FragmentContactzBinding


class contactz : Fragment(R.layout.fragment_contactz) {
    private  var _binding : FragmentContactzBinding?=null
    private val binding get() = _binding!!

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding= FragmentContactzBinding.bind(view)
        checkpermission()
        requestrecodingPermission()

        binding.p1.setOnClickListener {
            val ph = binding.p1.text.toString()
            comunicate("call", ph)
        }
        binding.p2.setOnClickListener {
            val ph = binding.p2.text.toString()
            comunicate("call", ph)
        }
        binding.p3.setOnClickListener {
            val ph = binding.p3.text.toString()
            comunicate("call", ph)
        }
        binding.p4.setOnClickListener {
            val ph = binding.p4.text.toString()
            comunicate("call", ph)
        }

        binding.r1.setOnClickListener {
            val ph = binding.r1.text.toString()
            comunicate("call", ph)
        }
        binding.r2.setOnClickListener {
            val ph = binding.r2.text.toString()
            comunicate("call", ph)
        }
        binding.s1.setOnClickListener {
            val ph = binding.s1.text.toString()
            comunicate("sms", ph)
        }

        binding.e1.setOnClickListener {
            val ph = binding.e1.text.toString()
            comunicate("email", ph)
        }
        binding.e2.setOnClickListener {
            val ph = binding.e2.text.toString()
            comunicate("email", ph)
        }
    }

    private fun comunicate(type: String, number: String) {
        val alert = AlertDialog.Builder(
            requireContext()
        )
        alert.setMessage("Do you wish to $type this number")
        alert.setNegativeButton(
            "no"
        ) { dialog, which -> }
        alert.setPositiveButton(
            "Yes"
        ) { dialog, which ->
            if (type == "call") {
                val str = number.substring(1, number.length);
                val phoneno = "tel:+254$str"
                val dailintent = Intent(Intent.ACTION_CALL)
                dailintent.data = Uri.parse(phoneno)
                try {
                    startActivity(dailintent)
                } catch (ex: ActivityNotFoundException) {
                    Toast.makeText(
                        context,
                        "phone app failed, please try again later.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else if (type == "sms") {
                val uri = Uri.parse("smsto:$number")
                val intent = Intent(Intent.ACTION_SENDTO, uri)
                intent.putExtra("sms_body", "coverse with us")
                startActivity(intent)
            } else if (type == "email") {
                val emailIntent = Intent(
                    Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", number, null
                    )
                )
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject")
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body")
                startActivity(Intent.createChooser(emailIntent, "Send email..."))
            }
        }
        alert.show()
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
            requireContext().permissionGranted(Manifest.permission.CALL_PHONE) -> {
                //to record
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE) -> {
                requireContext().showPermissionRequestExplanation(
                    getString(R.string.recordtwo),
                    getString(R.string.permission_requestwo)
                ) {
                    requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
                }
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}