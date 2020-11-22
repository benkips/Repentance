package www.digitalexperts.church_tracker.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import www.digitalexperts.church_tracker.Network.Resource
import www.digitalexperts.church_tracker.Repo.Repostuff
import www.digitalexperts.church_tracker.models.Folderz

class Pdfviewmodel @ViewModelInject constructor(private  val repostuff: Repostuff):ViewModel() {

     private val _pdffolderResponse: MutableLiveData<Resource<Folderz>> = MutableLiveData()
    val pdffolderResponse: LiveData<Resource<Folderz>>
      get() = _pdffolderResponse

  fun getpdfTeachings() = viewModelScope.launch {
      _pdffolderResponse.value = Resource.Loading
      _pdffolderResponse.value=repostuff.getteachingpdfs()
  }
}