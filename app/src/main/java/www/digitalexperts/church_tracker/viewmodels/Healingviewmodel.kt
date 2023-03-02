package www.digitalexperts.church_tracker.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import www.digitalexperts.church_tracker.Repo.Repostuff
import javax.inject.Inject

@HiltViewModel
class Healingviewmodel @Inject constructor(private  val repostuff: Repostuff) : ViewModel() {

    val Healed=repostuff.gethealingz()

}