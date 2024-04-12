package com.cyberkey.videoplayer.dialogs

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.cyberkey.commons.extensions.getAlertDialogBuilder
import com.cyberkey.commons.extensions.setupDialogStuff
import com.cyberkey.videoplayer.databinding.DialogConfirmDeleteFolderBinding

class ConfirmDeleteFolderDialog(activity: Activity, message: String, warningMessage: String, val callback: () -> Unit) {
    private var dialog: AlertDialog? = null

    init {
        val binding = DialogConfirmDeleteFolderBinding.inflate(activity.layoutInflater)
        binding.message.text = message
        binding.messageWarning.text = warningMessage

        activity.getAlertDialogBuilder()
            .setPositiveButton(com.cyberkey.commons.R.string.yes) { _, _ -> dialogConfirmed() }
            .setNegativeButton(com.cyberkey.commons.R.string.no, null)
            .apply {
                activity.setupDialogStuff(binding.root, this) { alertDialog ->
                    dialog = alertDialog
                }
            }
    }

    private fun dialogConfirmed() {
        dialog?.dismiss()
        callback()
    }
}
