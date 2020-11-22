package www.digitalexperts.church_tracker.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import www.digitalexperts.church_tracker.Adapters.Contentadapter
import www.digitalexperts.church_tracker.Adapters.Pdfadapter
import www.digitalexperts.church_tracker.Network.Resource
import www.digitalexperts.church_tracker.Utils.handleApiError
import www.digitalexperts.church_tracker.Utils.visible
import www.digitalexperts.church_tracker.viewmodels.Contentviewmodel
import www.digitalexperts.church_traker.R
import www.digitalexperts.church_traker.databinding.FragmentContentBinding
import www.digitalexperts.church_traker.databinding.FragmentPdfsBinding
@AndroidEntryPoint
class Content : Fragment(R.layout.fragment_content) {
  private  var _binding : FragmentContentBinding?=null
    private val binding get() = _binding!!
   private  val viewmodel by viewModels<Contentviewmodel>()
    private val args:ContentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding= FragmentContentBinding.bind(view)
        val x=args.tsend
        val y=args.f
        binding.pgbar.visible(false)

        getcontents(x,y)
        viewmodel.contentResponse.observe(viewLifecycleOwner, Observer {
            binding.pgbar.visible(it is Resource.Loading)
            when (it) {
                is Resource.Success -> {
                    binding.rvcontent.also {rv->
                        rv.layoutManager = LinearLayoutManager(requireContext())
                        rv.setHasFixedSize(true)
                        rv.adapter= Contentadapter(it.value)
                    }

                }
                is Resource.Failure -> handleApiError(it) {  getcontents(x,y)}
            }
        })
    }
    private  fun getcontents(x:String,y:String){
        viewmodel.getcontentz(x,y)
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}