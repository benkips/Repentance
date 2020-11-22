package www.digitalexperts.church_tracker.Adapters

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import www.digitalexperts.church_tracker.fragments.Radiostream
import www.digitalexperts.church_tracker.fragments.wvinfo

class vpageradapter(fragmentActivity: Fragment) :
    FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {Radiostream()}
            else -> {
                val c = "https://repentanceandholinessinfo.com/livevent.php"
                val fragmentmain: Fragment = wvinfo()
                fragmentmain.arguments = bundleOf("web" to c)
                fragmentmain
            }
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}