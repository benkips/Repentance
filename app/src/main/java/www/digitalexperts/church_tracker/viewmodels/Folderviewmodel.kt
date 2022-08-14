package www.digitalexperts.church_tracker.viewmodels

//import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import www.digitalexperts.church_tracker.Network.Resource
import www.digitalexperts.church_tracker.Repo.Repostuff
import www.digitalexperts.church_tracker.models.Folderz
import javax.inject.Inject

@HiltViewModel
class Folderviewmodel  @Inject constructor(private  val repostuff: Repostuff) : ViewModel() {

    private val _folderResponse: MutableLiveData<Resource<Folderz>> = MutableLiveData()
    val folderResponse: LiveData<Resource<Folderz>>
      get() = _folderResponse

  fun getTeachings() = viewModelScope.launch {
      _folderResponse.value = Resource.Loading
      _folderResponse.value=repostuff.getteachingfolders()
  }
}