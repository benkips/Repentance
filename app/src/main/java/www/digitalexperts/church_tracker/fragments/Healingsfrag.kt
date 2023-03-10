package www.digitalexperts.church_tracker.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import dagger.hilt.android.AndroidEntryPoint
import www.digitalexperts.church_tracker.Adapters.Healadapter
import www.digitalexperts.church_tracker.Adapters.HealingLoadStateAdapter
import www.digitalexperts.church_tracker.Utils.PlayerViewAdapter
import www.digitalexperts.church_tracker.Utils.PlayerViewAdapter.Companion.playIndexThenPausePreviousPlayer
import www.digitalexperts.church_tracker.Utils.RecyclerViewScrollListener
import www.digitalexperts.church_tracker.models.Healings
import www.digitalexperts.church_tracker.viewmodels.Healingviewmodel
import www.digitalexperts.church_traker.R
import www.digitalexperts.church_traker.databinding.FragmentHealingsfragBinding
@AndroidEntryPoint
class Healingsfrag : Fragment(R.layout.fragment_healingsfrag) ,Healadapter.OnItemClickListner{
    private  val viewmodel by viewModels<Healingviewmodel>()
    private  var _binding :FragmentHealingsfragBinding?=null
    private val binding get() = _binding!!

    private lateinit var mHttpDataSourceFactory: HttpDataSource.Factory
    private lateinit var mDefaultDataSourceFactory: DefaultDataSourceFactory
    private lateinit var mCacheDataSource: CacheDataSource

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding=FragmentHealingsfragBinding.bind(view)
        lateinit var scrollListener: RecyclerViewScrollListener

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

        scrollListener = object : RecyclerViewScrollListener() {
            override fun onItemIsFirstVisibleItem(index: Int) {
                Log.d("visible item index", index.toString())
                // play just visible item
                if (index != -1)
                    playIndexThenPausePreviousPlayer(index)
            }

        }
        binding.rvheals.addOnScrollListener(scrollListener)

            viewmodel.Healed.observe(viewLifecycleOwner){
            adapter.submitData(viewLifecycleOwner.lifecycle,it)
               // adapter.snapshot().items.

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
        PlayerViewAdapter.releaseAllPlayers()
        super.onDestroyView()
        _binding = null
    }
    }
