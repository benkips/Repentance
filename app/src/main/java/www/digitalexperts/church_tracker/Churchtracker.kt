package www.digitalexperts.church_tracker

import android.app.Application
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import dagger.hilt.android.HiltAndroidApp
import java.io.File

@HiltAndroidApp
class Churchtracker: Application() {

    companion object {
        lateinit var simpleCache: SimpleCache
        lateinit var leastRecentlyUsedCacheEvictor: LeastRecentlyUsedCacheEvictor
        lateinit var standaloneDatabaseProvider: StandaloneDatabaseProvider
        private const val exoCacheSize: Long = 100 * 1024 * 1024 // Setting cache size to be ~ 100 MB
    }

    override fun onCreate() {
        super.onCreate()
        // Initialize the cache
        leastRecentlyUsedCacheEvictor = LeastRecentlyUsedCacheEvictor(exoCacheSize)
        standaloneDatabaseProvider = StandaloneDatabaseProvider(this)
        simpleCache = SimpleCache(
            File(this.cacheDir, "media"),
            leastRecentlyUsedCacheEvictor,
            standaloneDatabaseProvider
        )
    }
}