package com.cyberkey.videoplayer.dialogs

import android.content.DialogInterface
import com.cyberkey.commons.activities.BaseSimpleActivity
import com.cyberkey.commons.dialogs.RadioGroupDialog
import com.cyberkey.commons.extensions.getAlertDialogBuilder
import com.cyberkey.commons.extensions.setupDialogStuff
import com.cyberkey.commons.models.RadioItem
import com.cyberkey.videoplayer.databinding.DialogChangeFileThumbnailStyleBinding
import com.cyberkey.videoplayer.extensions.config

class ChangeFileThumbnailStyleDialog(val activity: BaseSimpleActivity) : DialogInterface.OnClickListener {
    private var config = activity.config
    private val binding: DialogChangeFileThumbnailStyleBinding
    private var thumbnailSpacing = config.thumbnailSpacing

    init {
        binding = DialogChangeFileThumbnailStyleBinding.inflate(activity.layoutInflater).apply {
            dialogFileStyleRoundedCorners.isChecked = config.fileRoundedCorners
            dialogFileStyleShowThumbnailVideoDuration.isChecked = config.showThumbnailVideoDuration
            dialogFileStyleShowThumbnailFileTypes.isChecked = config.showThumbnailFileTypes
            dialogFileStyleMarkFavoriteItems.isChecked = config.markFavoriteItems

            dialogFileStyleRoundedCornersHolder.setOnClickListener { dialogFileStyleRoundedCorners.toggle() }
            dialogFileStyleShowThumbnailVideoDurationHolder.setOnClickListener { dialogFileStyleShowThumbnailVideoDuration.toggle() }
            dialogFileStyleShowThumbnailFileTypesHolder.setOnClickListener { dialogFileStyleShowThumbnailFileTypes.toggle() }
            dialogFileStyleMarkFavoriteItemsHolder.setOnClickListener { dialogFileStyleMarkFavoriteItems.toggle() }

            dialogFileStyleSpacingHolder.setOnClickListener {
                val items = arrayListOf(
                    RadioItem(0, "0x"),
                    RadioItem(1, "1x"),
                    RadioItem(2, "2x"),
                    RadioItem(4, "4x"),
                    RadioItem(8, "8x"),
                    RadioItem(16, "16x"),
                    RadioItem(32, "32x"),
                    RadioItem(64, "64x")
                )

                RadioGroupDialog(activity, items, thumbnailSpacing) {
                    thumbnailSpacing = it as Int
                    updateThumbnailSpacingText()
                }
            }
        }

        updateThumbnailSpacingText()

        activity.getAlertDialogBuilder()
            .setPositiveButton(com.cyberkey.commons.R.string.ok, this)
            .setNegativeButton(com.cyberkey.commons.R.string.cancel, null)
            .apply {
                activity.setupDialogStuff(binding.root, this)
            }
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        config.fileRoundedCorners = binding.dialogFileStyleRoundedCorners.isChecked
        config.showThumbnailVideoDuration = binding.dialogFileStyleShowThumbnailVideoDuration.isChecked
        config.showThumbnailFileTypes = binding.dialogFileStyleShowThumbnailFileTypes.isChecked
        config.markFavoriteItems = binding.dialogFileStyleMarkFavoriteItems.isChecked
        config.thumbnailSpacing = thumbnailSpacing
    }

    private fun updateThumbnailSpacingText() {
        binding.dialogFileStyleSpacing.text = "${thumbnailSpacing}x"
    }
}
