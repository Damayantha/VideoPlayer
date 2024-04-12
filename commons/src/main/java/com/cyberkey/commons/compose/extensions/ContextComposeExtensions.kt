package com.cyberkey.commons.compose.extensions

import android.app.Activity
import android.content.Context
import com.cyberkey.commons.R
import com.cyberkey.commons.extensions.baseConfig
import com.cyberkey.commons.extensions.redirectToRateUs
import com.cyberkey.commons.extensions.toast
import com.cyberkey.commons.helpers.BaseConfig

val Context.config: BaseConfig get() = BaseConfig.newInstance(applicationContext)

fun Activity.rateStarsRedirectAndThankYou(stars: Int) {
    if (stars == 5) {
        redirectToRateUs()
    }
    toast(R.string.thank_you)
    baseConfig.wasAppRated = true
}
