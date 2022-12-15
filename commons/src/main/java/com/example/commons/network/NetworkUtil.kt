package com.example.commons.network

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import java.util.*
import kotlin.collections.List

data class Speed(
    val speed: String,
    val unit: String
)

object NetworkUtil {

    private var speedValue = ""
    private var speedUnit = ""
    private var totalSpeed: Long = 0L
    private var downSpeed: Long = 0L
    private var upSpeed: Long = 0L
    private var isSpeedUnitBits: Boolean = false

    fun setSpeedUnitBits(isSpeedUnitBits: Boolean) {
        NetworkUtil.isSpeedUnitBits = isSpeedUnitBits
    }

    fun getSpeedUnitBits(): Boolean {
        return isSpeedUnitBits
    }

    /**
     * @speed bytes / timeTaken
     */
    private fun getSpeed(speed: Long): Speed {
        var speed = speed

        if (isSpeedUnitBits) {
            speed *= 8
        }

        if (speed < 1000000) {
            speedUnit =
                if (isSpeedUnitBits) "kb/s" else "kB/s"
            speedValue = (speed / 1000).toString()
        } else if (speed >= 1000000) {
            speedUnit =
                if (isSpeedUnitBits) "mb/s" else "MB/s"

            speedValue = if (speed < 10000000) {
                java.lang.String.format(Locale.ENGLISH, "%.1f", speed / 1000000.0)
            } else if (speed < 100000000) {
                (speed / 1000000).toString()
            } else {
                "99+"
            }
        } else {
            speedValue = "-"
            speedUnit = "-"
        }
        return Speed(speedValue, speedUnit)
    }

    /**
     * @description calculate speed of network with timetaken downbytes and upbytes as parameters
     * @param upBytes
     * @param downBytes
     * @param timeTaken
     */
    fun calculateSpeed(timeTaken: Long, downBytes: Long, upBytes: Long): List<Speed> {
        var totalSpeed: Long = 0
        var downSpeed: Long = 0
        var upSpeed: Long = 0
        val totalBytes = downBytes + upBytes
        if (timeTaken > 0) {
            totalSpeed = totalBytes * 1000 / timeTaken
            downSpeed = downBytes * 1000 / timeTaken
            upSpeed = upBytes * 1000 / timeTaken
        }
        NetworkUtil.totalSpeed = totalSpeed
        NetworkUtil.downSpeed = downSpeed
        NetworkUtil.upSpeed = upSpeed

        return listOf(
            getSpeed(totalSpeed),
            getSpeed(downSpeed),
            getSpeed(upSpeed)
        )
    }

}

//Define here function extensions
/**
 * @description detect if there is any type of network connection
 * @return true if some network is available, otherwise it will return false
 */
@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.hasNetwork(): Boolean = this.networkCapabilities() != null

/**
 * @description get network capabilities for the given network.
 * @return Network type, this function will return null if the network adapter is uknown
 */
@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.networkCapabilities(): NetworkCapabilities? {
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return cm.getNetworkCapabilities(cm.activeNetwork)
}