package com.cyberkey.videoplayer.extensions

import com.cyberkey.videoplayer.helpers.*
import com.cyberkey.videoplayer.models.Medium

fun ArrayList<Medium>.getDirMediaTypes(): Int {
    var types = 0
    if (any { it.isImage() }) {
        types += TYPE_IMAGES
    }

    if (any { it.isVideo() }) {
        types += TYPE_VIDEOS
    }

    if (any { it.isGIF() }) {
        types += TYPE_GIFS
    }

    if (any { it.isRaw() }) {
        types += TYPE_RAWS
    }

    if (any { it.isSVG() }) {
        types += TYPE_SVGS
    }

    if (any { it.isPortrait() }) {
        types += TYPE_PORTRAITS
    }

    return types
}
