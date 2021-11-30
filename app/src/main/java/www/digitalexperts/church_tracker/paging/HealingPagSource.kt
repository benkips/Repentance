package www.digitalexperts.church_tracker.paging

import androidx.paging.PagingSource
import retrofit2.HttpException
import www.digitalexperts.church_tracker.Network.ApiInterface
import www.digitalexperts.church_tracker.models.Healings
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1
class HealingPagSource( private val apiInterface: ApiInterface,
):PagingSource<Int, Healings>() {
    override suspend fun load(params:LoadParams<Int>):LoadResult<Int, Healings> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            val response = apiInterface.gethealings("http" +
                    "://mobile.repentanceandholinessinfo.com/mobiadmin/fullhealings",position, params.loadSize)
            val news = response.data
            LoadResult.Page(
                data = news,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (news.isEmpty()) null else position + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}