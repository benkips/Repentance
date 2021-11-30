package www.digitalexperts.church_tracker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class LaunchPlayerBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notifyIntent = Intent(context, index::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(notifyIntent)
    }
}