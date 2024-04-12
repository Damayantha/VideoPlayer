package com.cyberkey.videoplayer.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.cyberkey.commons.helpers.ensureBackgroundThread
import com.cyberkey.videoplayer.extensions.updateDirectoryPath
import com.cyberkey.videoplayer.helpers.MediaFetcher

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        ensureBackgroundThread {
            MediaFetcher(context).getFoldersToScan().forEach {
                context.updateDirectoryPath(it)
            }
        }
    }
}
