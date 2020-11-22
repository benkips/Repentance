package www.digitalexperts.church_tracker.Utils

import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Albums.getContentUri
import android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
import android.provider.MediaStore.Downloads.EXTERNAL_CONTENT_URI
import android.provider.MediaStore.Files.getContentUri
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import www.digitalexperts.church_tracker.Network.Resource
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


fun View.snackbar(message: String, action: (() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
    action?.let {
        snackbar.setAction("Retry") {
            it()
        }
    }
    snackbar.show()

}

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun Fragment.handleApiError(
    failure: Resource.Failure,
    retry: (() -> Unit)? = null
) {
    when {
        failure.isNetworkError -> requireView().snackbar("please check your internet",retry)
        else -> {
            val error = failure.errorBody?.string().toString()
            requireView().snackbar(error)
        }

    }
}

fun Context.showPermissionRequestExplanation(
    permission: String,
    message: String,
    retry: (() -> Unit)? = null
) {
    AlertDialog.Builder(this).apply {
        setTitle("$permission Required")
        setMessage(message)
        setPositiveButton("Ok") { _, _ -> retry?.invoke() }
    }.show()
}

fun Context.permissionGranted(permission: String) =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

fun Context.createaudiofile(fname: String): ParcelFileDescriptor {
    var file: ParcelFileDescriptor?=null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        this.contentResolver?.also { resolver ->
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fname)
                put(MediaStore.MediaColumns.MIME_TYPE, "audio/m4a")
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    "Music/Recordings/"
                )
            }
            val uriSavedAudio: Uri? =
                resolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues)
            file = uriSavedAudio?.let { resolver.openFileDescriptor(it, "w") }!!
        }
    }
    return file!!

}

fun Context.getBitmapFromVectorDrawable(drawableId: Int): Bitmap? {
    var drawable = ContextCompat.getDrawable(this, drawableId) ?: return null

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        drawable = DrawableCompat.wrap(drawable).mutate()
    }

    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888) ?: return null
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)

    return bitmap
}


