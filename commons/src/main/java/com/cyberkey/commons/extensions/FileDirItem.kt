package com.cyberkey.commons.extensions

import android.content.Context
import com.cyberkey.commons.models.FileDirItem

fun FileDirItem.isRecycleBinPath(context: Context): Boolean {
    return path.startsWith(context.recycleBinPath)
}
