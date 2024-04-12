package com.cyberkey.videoplayer.activities

import android.content.Intent
import android.view.View
import com.cyberkey.commons.activities.BaseSplashActivity
import com.cyberkey.commons.cc
import com.cyberkey.commons.helpers.ensureBackgroundThread
import com.cyberkey.videoplayer.R
import com.cyberkey.videoplayer.extensions.config
import com.cyberkey.videoplayer.extensions.favoritesDB
import com.cyberkey.videoplayer.extensions.getFavoriteFromPath
import com.cyberkey.videoplayer.extensions.mediaDB
import com.cyberkey.videoplayer.models.Favorite

class SplashActivity : BaseSplashActivity() {



    override fun initActivity() {

//        if (cc.newInstance(applicationContext).removeSplashScreen) {
//            setTheme(R.style.SplashTheme)
//        } else {
//            setTheme(R.style.SplashTheme2)
//        }


        // check if previously selected favorite items have been properly migrated into the new Favorites table
        if (config.wereFavoritesMigrated) {
            launchActivity()
        } else {
            if (config.appRunCount == 0) {
                config.wereFavoritesMigrated = true
                launchActivity()
            } else {
                config.wereFavoritesMigrated = true
                ensureBackgroundThread {
                    val favorites = ArrayList<Favorite>()
                    val favoritePaths = mediaDB.getFavorites().map { it.path }.toMutableList() as ArrayList<String>
                    favoritePaths.forEach {
                        favorites.add(getFavoriteFromPath(it))
                    }
                    favoritesDB.insertAll(favorites)

                    runOnUiThread {
                        launchActivity()
                    }
                }
            }
        }
    }

    private fun launchActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
