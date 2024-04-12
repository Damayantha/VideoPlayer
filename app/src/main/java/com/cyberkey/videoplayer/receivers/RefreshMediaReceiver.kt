package com.cyberkey.videoplayer.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.cyberkey.commons.helpers.REFRESH_PATH
import com.cyberkey.videoplayer.extensions.addPathToDB

class RefreshMediaReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val path = intent.getStringExtra(REFRESH_PATH) ?: return
        context.addPathToDB(path)
    }
}
