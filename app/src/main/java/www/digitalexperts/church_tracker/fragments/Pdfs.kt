package www.digitalexperts.church_tracker.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import www.digitalexperts.church_tracker.Adapters.Folderadapter
import www.digitalexperts.church_tracker.Adapters.Pdfadapter
import www.digitalexperts.church_tracker.Network.Resource
import www.digitalexperts.church_tracker.Utils.handleApiError
import www.digitalexperts.church_tracker.Utils.visible
import www.digitalexperts.church_tracker.viewmodels.Pdfviewmodel
import www.digitalexperts.church_traker.R
import www.digitalexperts.church_traker.databinding.FragmentPdfsBinding
import www.digitalexperts.church_traker.databinding.FragmentTeachingsBinding
@AndroidEntryPoint
class Pdfs : Fragment(R.layout.fragment_pdfs) {
     private  var _binding : FragmentPdfsBinding?=null
    private val binding get() = _binding!!
   private  val viewmodel by viewModels<Pdfviewmodel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding= FragmentPdfsBinding.bind(view)

        binding.pgbar.visible(false)

        viewmodel.getpdfTeachings()
        viewmodel.pdffolderResponse.observe(viewLifecycleOwner, Observer {
            binding.pgbar.visible(it is Resource.Loading)
            when (it) {
                is Resource.Success -> {
                    binding.rvpdf.also {rv->
                        rv.layoutManager = LinearLayoutManager(requireContext())
                        rv.setHasFixedSize(true)
                        rv.adapter= Pdfadapter(it.value)
                    }

                }
                is Resource.Failure -> handleApiError(it) { pdfzteaching() }
            }
        })
    }
    private fun pdfzteaching(){
        viewmodel.getpdfTeachings()
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}