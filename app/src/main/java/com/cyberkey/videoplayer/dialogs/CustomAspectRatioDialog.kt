package com.cyberkey.videoplayer.dialogs

import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.cyberkey.commons.activities.BaseSimpleActivity
import com.cyberkey.commons.extensions.getAlertDialogBuilder
import com.cyberkey.commons.extensions.setupDialogStuff
import com.cyberkey.commons.extensions.showKeyboard
import com.cyberkey.commons.extensions.value
import com.cyberkey.videoplayer.databinding.DialogCustomAspectRatioBinding

class CustomAspectRatioDialog(
    val activity: BaseSimpleActivity, val defaultCustomAspectRatio: Pair<Float, Float>?, val callback: (aspectRatio: Pair<Float, Float>) -> Unit
) {
    init {
        val binding = DialogCustomAspectRatioBinding.inflate(activity.layoutInflater).apply {
            aspectRatioWidth.setText(defaultCustomAspectRatio?.first?.toInt()?.toString() ?: "")
            aspectRatioHeight.setText(defaultCustomAspectRatio?.second?.toInt()?.toString() ?: "")
        }

        activity.getAlertDialogBuilder()
            .setPositiveButton(com.cyberkey.commons.R.string.ok, null)
            .setNegativeButton(com.cyberkey.commons.R.string.cancel, null)
            .apply {
                activity.setupDialogStuff(binding.root, this) { alertDialog ->
                    alertDialog.showKeyboard(binding.aspectRatioWidth)
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        val width = getViewValue(binding.aspectRatioWidth)
                        val height = getViewValue(binding.aspectRatioHeight)
                        callback(Pair(width, height))
                        alertDialog.dismiss()
                    }
                }
            }
    }

    private fun getViewValue(view: EditText): Float {
        val textValue = view.value
        return if (textValue.isEmpty()) 0f else textValue.toFloat()
    }
}
