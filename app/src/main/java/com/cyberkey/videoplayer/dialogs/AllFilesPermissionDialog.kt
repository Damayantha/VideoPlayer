package com.cyberkey.videoplayer.dialogs

import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.cyberkey.commons.activities.BaseSimpleActivity
import com.cyberkey.commons.extensions.getAlertDialogBuilder
import com.cyberkey.commons.extensions.setupDialogStuff
import com.cyberkey.videoplayer.R

class AllFilesPermissionDialog(
    val activity: BaseSimpleActivity, message: String = "", val callback: (result: Boolean) -> Unit, val neutralPressed: () -> Unit
) {
    private var dialog: AlertDialog? = null

    init {
        val view = activity.layoutInflater.inflate(com.cyberkey.commons.R.layout.dialog_message, null)
        view.findViewById<TextView>(R.id.message).text = message

        activity.getAlertDialogBuilder().setPositiveButton(R.string.all_files) { _, which -> positivePressed() }
            .setNeutralButton(R.string.media_only) { _, _ -> neutralPressed() }
            .apply {
                activity.setupDialogStuff(view, this) { alertDialog ->
                    dialog = alertDialog
                }
            }
    }

    private fun positivePressed() {
        dialog?.dismiss()
        callback(true)
    }
}
