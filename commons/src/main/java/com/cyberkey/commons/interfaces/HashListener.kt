package com.cyberkey.commons.interfaces

interface HashListener {
    fun receivedHash(hash: String, type: Int)
}
