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
import www.digitalexperts.church_tracker.Network.Resource
import www.digitalexperts.church_tracker.Utils.handleApiError
import www.digitalexperts.church_tracker.Utils.visible
import www.digitalexperts.church_tracker.viewmodels.Folderviewmodel
import www.digitalexperts.church_traker.R
import www.digitalexperts.church_traker.databinding.FragmentHomeBinding
import www.digitalexperts.church_traker.databinding.FragmentTeachingsBinding

@AndroidEntryPoint
class Teachings : Fragment(R.layout.fragment_teachings) {
    private  var _binding : FragmentTeachingsBinding?=null
    private val binding get() = _binding!!
   private  val viewmodel by viewModels<Folderviewmodel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding=FragmentTeachingsBinding.bind(view)

        binding.pgbar.visible(false)

        viewmodel.getTeachings()
        viewmodel.folderResponse.observe(viewLifecycleOwner, Observer {
            binding.pgbar.visible(it is Resource.Loading)
            when (it) {
                is Resource.Success -> {
                    binding.rvteachings.also { rv ->
                        rv.layoutManager = LinearLayoutManager(requireContext())
                        rv.setHasFixedSize(true)
                        rv.adapter=Folderadapter(it.value)
                    }
                }
                is Resource.Failure -> handleApiError(it) { teaching() }
            }

        })
    }
    private fun teaching(){
        viewmodel.getTeachings()
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}