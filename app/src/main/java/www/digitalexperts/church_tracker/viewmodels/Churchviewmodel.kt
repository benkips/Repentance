package www.digitalexperts.church_tracker.viewmodels

//import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import www.digitalexperts.church_tracker.Network.Resource
import www.digitalexperts.church_tracker.Repo.Repostuff
import www.digitalexperts.church_tracker.models.Churches
import javax.inject.Inject

@HiltViewModel
class Churchviewmodel @Inject constructor(private  val repostuff: Repostuff) :ViewModel(){

    private val searchstring = MutableLiveData<String>()
    val result = searchstring.switchMap {
        liveData {
            emit(Resource.Loading)
            emit(repostuff.getSearchresults(it)) }
    }
    fun searchphotos(query:String){
        searchstring.value=query
    }


    /*private val _churchResponse: MutableLiveData<Resource<Churches>> = MutableLiveData()
    val churchResponse:LiveData<Resource<Churches>>
        get() = _churchResponse
    fun search(
        query: String
    ) = viewModelScope.launch {
        _churchResponse.value = Resource.Loading
        _churchResponse.value=repostuff.getSearchresults(query)
    }*/

}