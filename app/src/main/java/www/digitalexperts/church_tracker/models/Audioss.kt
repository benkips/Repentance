package www.digitalexperts.church_tracker.models

import android.net.Uri

data class Audioss(val uri: Uri,
                   val name: String,
                   val duration: Int,
                   val size: Int
)