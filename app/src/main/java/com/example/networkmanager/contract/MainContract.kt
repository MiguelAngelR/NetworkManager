package com.example.networkmanager.contract

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.commons.network.Speed
import java.sql.Connection

class MainContract {

    interface ViewMain {
    }

    interface PresenterMain{
        fun calculateSpeedConnection()
        fun setSpeedConnection(speeds: List<Speed>)
        val speedConnection: LiveData<List<Speed>>
    }

    interface ModelMain{
        fun calculateSpeedConnection()
    }

}