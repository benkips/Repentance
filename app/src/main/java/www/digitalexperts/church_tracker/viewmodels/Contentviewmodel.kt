package www.digitalexperts.church_tracker.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import www.digitalexperts.church_tracker.Network.Resource
import www.digitalexperts.church_tracker.Repo.Repostuff
import www.digitalexperts.church_tracker.models.Pdfdata

class Contentviewmodel@ViewModelInject constructor(private  val repostuff: Repostuff) : ViewModel() {

     private val _contentResponse: MutableLiveData<Resource<Pdfdata>> = MutableLiveData()
    val contentResponse: LiveData<Resource<Pdfdata>>
      get() = _contentResponse

  fun getcontentz(x:String,y:String) = viewModelScope.launch {
      _contentResponse.value = Resource.Loading
      _contentResponse.value=repostuff.getcontents(x,y)
  }
}