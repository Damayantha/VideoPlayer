package com.cyberkey.commons.compose.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.cyberkey.commons.R
import com.cyberkey.commons.compose.alert_dialog.rememberAlertDialogState
import com.cyberkey.commons.dialogs.ConfirmationAlertDialog
import com.cyberkey.commons.extensions.launchViewIntent


@Composable
fun CheckAppOnSdCard() {
    val context = LocalContext.current.getComponentActivity()
    val confirmationDialogAlertDialogState = rememberAlertDialogState().apply {
        DialogMember {
            ConfirmationAlertDialog(
                alertDialogState = this,
                messageId = R.string.app_on_sd_card,
                positive = R.string.ok,
                negative = null
            ) {}
        }
    }
    LaunchedEffect(Unit) {
        context.appOnSdCardCheckCompose(confirmationDialogAlertDialogState::show)
    }
}
