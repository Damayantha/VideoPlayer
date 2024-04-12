package com.cyberkey.commons

import android.content.Context
import com.cyberkey.commons.helpers.BaseConfig

class cc(context: Context) : BaseConfig(context) {
    companion object {
        fun newInstance(context: Context) = cc(context)
    }

    var removeSplashScreen: Boolean
        get() = prefs.getBoolean("SPSCREEN", false)
        set(removeSplashScreen) = prefs.edit().putBoolean("SPSCREEN", removeSplashScreen).apply()


}
