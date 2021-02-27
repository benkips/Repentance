package www.digitalexperts.church_tracker.Repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import www.digitalexperts.church_tracker.Network.ApiInterface
import www.digitalexperts.church_tracker.Network.Resource
import www.digitalexperts.church_tracker.models.Churches
import www.digitalexperts.church_tracker.paging.HealingPagSource
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

    fun gethealingz() =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {HealingPagSource(apiInterface)}
        ).liveData
}