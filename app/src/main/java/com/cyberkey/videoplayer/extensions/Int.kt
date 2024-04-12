package com.cyberkey.videoplayer.extensions

import com.cyberkey.commons.helpers.SORT_DESCENDING

fun Int.isSortingAscending() = this and SORT_DESCENDING == 0
