package com.example.networkmanager.model

import android.content.Context
import android.os.Handler
import com.example.commons.network.*
import com.example.networkmanager.contract.MainContract

class MainModel(
    private val presenter: MainContract.PresenterMain,
    private val context: Context
):MainContract.ModelMain {

    private var networkUsage:NetworkUsageManager? = NetworkUsageManager(context, Util.getSubscriberId(context))

    override fun calculateSpeedConnection() {
        val handler = Handler()
        val runnableCode = object : Runnable {
            override fun run() {
                val now = networkUsage?.getUsageNow(NetworkType.ALL)
                val speeds = NetworkUtil.calculateSpeed(now!!.timeTaken, now.downloads, now.uploads)
                presenter.setSpeedConnection(speeds)
                handler.postDelayed(this, 1000)
            }
        }
        runnableCode.run()
    }

}