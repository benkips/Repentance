package www.digitalexperts.church_tracker.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import www.digitalexperts.church_tracker.Network.Resource
import www.digitalexperts.church_tracker.Repo.Repostuff
import www.digitalexperts.church_tracker.models.Pastors

class Pastorsviewmodel @ViewModelInject constructor(private  val repostuff: Repostuff) : ViewModel() {
    private val _pastorResponse: MutableLiveData<Resource<Pastors>> = MutableLiveData()
    val pastorResponse: LiveData<Resource<Pastors>>
      get() = _pastorResponse

  fun getpastorz(x:String) = viewModelScope.launch {
      _pastorResponse.value = Resource.Loading
      _pastorResponse.value=repostuff.getpastors(x)
  }
}