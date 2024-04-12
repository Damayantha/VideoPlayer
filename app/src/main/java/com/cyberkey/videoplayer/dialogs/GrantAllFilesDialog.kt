package com.cyberkey.videoplayer.dialogs

import com.cyberkey.commons.activities.BaseSimpleActivity
import com.cyberkey.commons.extensions.applyColorFilter
import com.cyberkey.commons.extensions.getAlertDialogBuilder
import com.cyberkey.commons.extensions.getProperTextColor
import com.cyberkey.commons.extensions.setupDialogStuff
import com.cyberkey.videoplayer.databinding.DialogGrantAllFilesBinding
import com.cyberkey.videoplayer.extensions.launchGrantAllFilesIntent

class GrantAllFilesDialog(val activity: BaseSimpleActivity) {
    init {
        val binding = DialogGrantAllFilesBinding.inflate(activity.layoutInflater)
        binding.grantAllFilesImage.applyColorFilter(activity.getProperTextColor())

        activity.getAlertDialogBuilder()
            .setPositiveButton(com.cyberkey.commons.R.string.ok) { _, _ -> activity.launchGrantAllFilesIntent() }
            .setNegativeButton(com.cyberkey.commons.R.string.cancel, null)
            .apply {
                activity.setupDialogStuff(binding.root, this) { }
            }
    }
}
