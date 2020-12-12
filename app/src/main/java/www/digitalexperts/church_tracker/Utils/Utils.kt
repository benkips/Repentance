package www.digitalexperts.church_tracker.Utils

import android.content.ContentUris
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import www.digitalexperts.church_tracker.Network.Resource
import www.digitalexperts.church_tracker.fragments.Radiostream
import www.digitalexperts.church_tracker.models.Audioss
import www.digitalexperts.church_tracker.models.Myaudioitems
import java.io.IOException


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
        failure.isNetworkError -> requireView().snackbar("please check your internet", retry)
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
        Bitmap.Config.ARGB_8888
    ) ?: return null
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)

    return bitmap
}

fun Context.readmyaudios():List<Audioss>{

    val audioList = mutableListOf<Audioss>()

    val collection =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL
            )
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }
    val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.DISPLAY_NAME,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.SIZE
    )
    val  directory = "Recordings"
    val audiodir = "%" + directory + "%";

    val selection = "${MediaStore.Audio.Media.RELATIVE_PATH}   like ?"
    val selectionArgs = arrayOf(
        audiodir
    )
    val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"

    val query = this.contentResolver.query(
        collection,
        projection,
        selection,
        selectionArgs,
        sortOrder
    )
    query?.use { cursor ->
        // Cache column indices.
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
        val nameColumn =
            cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
        val durationColumn =
            cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
        val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)

        while (cursor.moveToNext()) {
            // Get values of columns for a given video.
            val id = cursor.getLong(idColumn)
            val name = cursor.getString(nameColumn)
            val duration = cursor.getInt(durationColumn)
            val size = cursor.getInt(sizeColumn)

            val contentUri: Uri = ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                id
            )

            // Stores column values and the contentUri in a local object
            // that represents the media file.
            audioList += Audioss(contentUri, name, duration, size)
        }
    }
    return  audioList
}

fun Context.readaudios():List<Myaudioitems>{

    val audioList = mutableListOf<Myaudioitems>()

    val collection =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL
            )
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }
    val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.DISPLAY_NAME,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.SIZE
    )
    val  directory = "Recordings"
    val audiodir = "%" + directory + "%";

    val selection = "${MediaStore.Audio.Media.RELATIVE_PATH}   like ?"
    val selectionArgs = arrayOf(
        audiodir
    )
    val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"

    val query = this.contentResolver.query(
        collection,
        projection,
        selection,
        selectionArgs,
        sortOrder
    )
    query?.use { cursor ->
        // Cache column indices.
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
        val nameColumn =
            cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
        val durationColumn =
            cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
        val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)

        while (cursor.moveToNext()) {
            // Get values of columns for a given video.
            val id = cursor.getLong(idColumn)
            val name = cursor.getString(nameColumn)
            val duration = cursor.getInt(durationColumn)
            val size = cursor.getInt(sizeColumn)

            val contentUri: Uri = ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                id
            )

            // Stores column values and the contentUri in a local object
            // that represents the media file.
            audioList += Myaudioitems(contentUri, name, duration, size)
        }
    }
    return  audioList
}

fun  Context.getNameFromContentUri(context: Context, contentUri: Uri?): String? {
    val returnCursor: Cursor? = this.contentResolver.query(contentUri!!, null, null, null, null)
    val nameColumnIndex: Int = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    returnCursor.moveToFirst()
    return returnCursor.getString(nameColumnIndex)
}

fun Context.deleteaudio(uri:Uri){

   try {
       val deleted = this.contentResolver.delete(uri, null, null)

       if (deleted >= 0) {
           Log.d("Tag", "File deleted");
       }
   } catch (e: Exception) {
       Log.e(TAG, "failed to delete" + e.toString())
     /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
           val recoverableSecurityException =
               securityException as? RecoverableSecurityException
                   ?: throw SecurityException()

           val intentSender = recoverableSecurityException.userAction.actionIntent.intentSender

           intentSender?.let {
               mActivity.startIntentSenderForResult(intentSender, 0, null, 0, 0, 0, null)
           }
       } else {
           throw SecurityException()
       }*/
   }
}


