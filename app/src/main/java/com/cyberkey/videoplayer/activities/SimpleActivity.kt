package com.cyberkey.videoplayer.activities

import android.database.ContentObserver
import android.net.Uri
import android.provider.MediaStore.Images
import android.provider.MediaStore.Video
import android.view.WindowManager
import com.cyberkey.commons.activities.BaseSimpleActivity
import com.cyberkey.commons.dialogs.FilePickerDialog
import com.cyberkey.commons.extensions.getParentPath
import com.cyberkey.commons.extensions.getRealPathFromURI
import com.cyberkey.commons.extensions.scanPathRecursively
import com.cyberkey.commons.helpers.ensureBackgroundThread
import com.cyberkey.commons.helpers.isPiePlus
import com.cyberkey.videoplayer.R
import com.cyberkey.videoplayer.extensions.addPathToDB
import com.cyberkey.videoplayer.extensions.config
import com.cyberkey.videoplayer.extensions.updateDirectoryPath

open class SimpleActivity : BaseSimpleActivity() {
    val observer = object : ContentObserver(null) {
        override fun onChange(selfChange: Boolean, uri: Uri?) {
            super.onChange(selfChange, uri)
            if (uri != null) {
                val path = getRealPathFromURI(uri)
                if (path != null) {
                    updateDirectoryPath(path.getParentPath())
                    addPathToDB(path)
                }
            }
        }
    }


    override fun getAppLauncherName() = getString(R.string.app_launcher_name)

    protected fun checkNotchSupport() {
        if (isPiePlus()) {
            val cutoutMode = when {
                config.showNotch -> WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                else -> WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER
            }

            window.attributes.layoutInDisplayCutoutMode = cutoutMode
            if (config.showNotch) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            }
        }
    }

    protected fun registerFileUpdateListener() {
        try {
            contentResolver.registerContentObserver(Images.Media.EXTERNAL_CONTENT_URI, true, observer)
            contentResolver.registerContentObserver(Video.Media.EXTERNAL_CONTENT_URI, true, observer)
        } catch (ignored: Exception) {
        }
    }

    protected fun unregisterFileUpdateListener() {
        try {
            contentResolver.unregisterContentObserver(observer)
        } catch (ignored: Exception) {
        }
    }

    protected fun showAddIncludedFolderDialog(callback: () -> Unit) {
        FilePickerDialog(this, config.lastFilepickerPath, false, config.shouldShowHidden, false, true) {
            config.lastFilepickerPath = it
            config.addIncludedFolder(it)
            callback()
            ensureBackgroundThread {
                scanPathRecursively(it)
            }
        }
    }
}
