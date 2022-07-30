package www.digitalexperts.church_tracker.fragments

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import www.digitalexperts.church_tracker.Adapters.Churchadapter
import www.digitalexperts.church_tracker.Network.Resource
import www.digitalexperts.church_tracker.Utils.handleApiError
import www.digitalexperts.church_tracker.Utils.visible
import www.digitalexperts.church_tracker.viewmodels.Churchviewmodel
import www.digitalexperts.church_traker.R
import www.digitalexperts.church_traker.databinding.FragmentHomeBinding

@AndroidEntryPoint
class Home : Fragment(R.layout.fragment_home) {
    private  var _binding : FragmentHomeBinding?=null
    private val binding get() = _binding!!
    private  val viewmodel by viewModels<Churchviewmodel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding=FragmentHomeBinding.bind(view)

        binding.pgbar.visible(false)
        binding.rvchurches.setNestedScrollingEnabled(false);

        srchnow("nairobi")
        viewmodel.result.observe(viewLifecycleOwner, Observer {
            binding.pgbar.visible(it is Resource.Loading)
            when (it) {
                is Resource.Success -> {
                    binding.rvchurches.also {rv->
                        rv.layoutManager=LinearLayoutManager(requireContext())
                        rv.setHasFixedSize(true)
                        rv.adapter=Churchadapter(it.value)
                    }
                }
                is Resource.Failure -> handleApiError(it) { srchnow("nairobi") }
            }
        })

        binding.srchbtn.setOnClickListener {
            val qry:String=binding.srchquery.text.toString().trim()
            if(!qry.isEmpty()) {
                srchnow(qry)
            }
        }
        binding.srchquery.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val qry:String=binding.srchquery.text.toString().trim()
                if(!qry.isEmpty()) {
                    srchnow(qry)
                }
            }
            true
        }


    }
    private  fun srchnow(qury:String){
        viewmodel.searchphotos(qury)
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}