package www.digitalexperts.church_tracker.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import www.digitalexperts.church_tracker.Adapters.Healadapter
import www.digitalexperts.church_tracker.Adapters.HealingLoadStateAdapter
import www.digitalexperts.church_tracker.models.Healings
import www.digitalexperts.church_tracker.viewmodels.Healingviewmodel
import www.digitalexperts.church_traker.R
import www.digitalexperts.church_traker.databinding.FragmentHealingsfragBinding
@AndroidEntryPoint
class Healingsfrag : Fragment(R.layout.fragment_healingsfrag) ,Healadapter.OnItemClickListner{
    private  val viewmodel by viewModels<Healingviewmodel>()
    private  var _binding :FragmentHealingsfragBinding?=null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding=FragmentHealingsfragBinding.bind(view)
        val adapter= Healadapter(this)

        binding.apply {
            rvheals.setHasFixedSize(true)
            rvheals.adapter=adapter.withLoadStateHeaderAndFooter(
                header = HealingLoadStateAdapter{adapter.retry()},
                footer =HealingLoadStateAdapter{adapter.retry()}
            )
            btnretry.setOnClickListener {
                adapter.retry();
            }
        }

        viewmodel.Healed.observe(viewLifecycleOwner){
            adapter.submitData(viewLifecycleOwner.lifecycle,it)
        }

        adapter.addLoadStateListener {loadstate->
            binding.apply {
                pgbar.isVisible=loadstate.source.refresh is LoadState.Loading
                rvheals.isVisible=loadstate.source.refresh is LoadState.NotLoading
                btnretry.isVisible=loadstate.source.refresh is LoadState.Error
                tvError.isVisible=loadstate.source.refresh is LoadState.Error
                //empty view
                if(loadstate.source.refresh is LoadState.NotLoading &&
                    loadstate.append.endOfPaginationReached &&
                    adapter.itemCount<1){
                    rvheals.isVisible=false
                    tvViewEmpty.isVisible=true
                }else{
                    tvViewEmpty.isVisible =false
                }
            }
        }
    }

    override fun onItemClick(heal:Healings) {
        super.onItemClick(heal)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    }
