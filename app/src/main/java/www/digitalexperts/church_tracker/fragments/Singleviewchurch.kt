package www.digitalexperts.church_tracker.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dagger.hilt.android.AndroidEntryPoint
import www.digitalexperts.church_tracker.Network.Resource
import www.digitalexperts.church_tracker.Utils.handleApiError
import www.digitalexperts.church_tracker.viewmodels.Pastorsviewmodel
import www.digitalexperts.church_traker.R
import www.digitalexperts.church_traker.databinding.FragmentSingleviewchurchBinding
import java.util.*

@AndroidEntryPoint
class Singleviewchurch : Fragment(R.layout.fragment_singleviewchurch) {
    private  var _binding : FragmentSingleviewchurchBinding?=null
    private val binding get() = _binding!!
    private  val viewmodel by viewModels<Pastorsviewmodel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding=FragmentSingleviewchurchBinding.bind(view)


        val id=arguments?.getString("id")
        val name=arguments?.getString("name")
        val location=arguments?.getString("location")
        val photo=arguments?.getString("photo")

        getpastorx(id!!)
        Glide.with(binding.chimagez)
            .load("https://repentanceandholinessinfo.com/photos/" + photo)
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.ic_error)
            .into(binding.chimagez)
        binding.chnamez.setText(name)

        viewmodel.pastorResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    binding.callbtn.visibility = View.VISIBLE
                    val title = ArrayList<String>()
                    for (value in it.value) {
                        title.add(value.event)
                        title.add(value.name)
                        title.add(value.phone)
                    }
                    binding.chpastorz.text = title[1]
                    val phn = title[2]
                    binding.callbtn.setOnClickListener {
                        if (!phn.isEmpty()) {
                            val intent = Intent(Intent.ACTION_DIAL)
                            intent.data = Uri.parse("tel:+254$phn")
                            startActivity(intent)
                        }
                    }
                }
                is Resource.Failure -> handleApiError(it) {
                    getpastorx(id!!)
                }
            }
        })


    }
    private  fun getpastorx(x: String){
        viewmodel.getpastorz(x)
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}