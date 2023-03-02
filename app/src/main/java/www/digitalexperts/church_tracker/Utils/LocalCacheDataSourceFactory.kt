package www.digitalexperts.church_tracker.Utils

import android.content.Context
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.upstream.cache.CacheDataSink
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File


class LocalCacheDataSourceFactory(private val context: Context) : DataSource.Factory {
   val  MAX_CACHE_SIZE=1024 * 1024 * 1024
    val MAX_FILE_SIZE=1024 * 1024 * 1024
    private val defaultDataSourceFactory: DefaultDataSourceFactory
    private val simpleCache: SimpleCache = SimpleCache(
        File(context.cacheDir, "media"),
        LeastRecentlyUsedCacheEvictor(MAX_CACHE_SIZE.toLong())
    )
    private val cacheDataSink: CacheDataSink = CacheDataSink(simpleCache, MAX_FILE_SIZE.toLong())
    private val fileDataSource: FileDataSource = FileDataSource()

    init {
        val userAgent = "Demo"
        val bandwidthMeter = DefaultBandwidthMeter()
        defaultDataSourceFactory = DefaultDataSourceFactory(
            this.context,
            bandwidthMeter,
            DefaultHttpDataSourceFactory(userAgent)
        )
    }

    override fun createDataSource(): DataSource {
        return CacheDataSource(
            simpleCache, defaultDataSourceFactory.createDataSource(),
            fileDataSource, cacheDataSink,
            CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR, null
        )
    }
}