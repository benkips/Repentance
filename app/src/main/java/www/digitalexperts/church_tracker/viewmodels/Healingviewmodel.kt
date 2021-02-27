package www.digitalexperts.church_tracker.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import www.digitalexperts.church_tracker.Repo.Repostuff

class Healingviewmodel @ViewModelInject constructor(private  val repostuff: Repostuff) : ViewModel() {

    val Healed=repostuff.gethealingz()

}