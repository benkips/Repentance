package www.digitalexperts.church_tracker.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import www.digitalexperts.church_tracker.Adapters.vpageradapter
import www.digitalexperts.church_traker.R
import www.digitalexperts.church_traker.databinding.FragmentLiveBinding

class Live : Fragment(R.layout.fragment_live) {

    private  var _binding : FragmentLiveBinding?=null
    private val binding get() = _binding!!



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding=FragmentLiveBinding.bind(view)

        binding.viewPager.adapter = vpageradapter(requireParentFragment())
        TabLayoutMediator(binding.tablayout, binding.viewPager){ tab, position->
            when(position){
                0 -> {
                    tab.text = "Listen"
                    tab.setIcon(R.drawable.ic_baseline_wifi_tethering_24)
                }
                1 -> {
                    tab.text = "Live events"
                    tab.setIcon(R.drawable.ic_baseline_slow_motion_video_24)
                }


            }

        }.attach()




    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}