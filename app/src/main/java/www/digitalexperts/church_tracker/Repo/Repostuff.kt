package www.digitalexperts.church_tracker.Repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import www.digitalexperts.church_tracker.Network.ApiInterface
import www.digitalexperts.church_tracker.Network.Resource
import www.digitalexperts.church_tracker.models.Churches
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repostuff @Inject constructor (private val apiInterface: ApiInterface):Baserepository(){

    suspend fun getSearchresults(query: String) =
        safeApiCall{apiInterface.searchresults(query)}

    suspend fun  getteachingfolders()= safeApiCall {
        apiInterface.getfolders("teachings") }

    suspend fun  getteachingpdfs()= safeApiCall {
        apiInterface.getfolders("magazines") }

    suspend fun  getcontents(x:String,y:String)=safeApiCall {
        apiInterface.getpdfitems(x,y)
    }
    suspend fun getpastors(x:String)=safeApiCall {
        apiInterface.getingpastors(x)
    }
}