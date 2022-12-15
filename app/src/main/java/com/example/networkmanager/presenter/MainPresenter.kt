package com.example.networkmanager.presenter

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.commons.network.Speed
import com.example.networkmanager.contract.MainContract
import com.example.networkmanager.model.MainModel

class MainPresenter(
    private val context: Context,
    private val view: MainContract.ViewMain
) : MainContract.PresenterMain {

    private val _speedConnection =  MutableLiveData<List<Speed>>()
    override val speedConnection: LiveData<List<Speed>>
        get() = _speedConnection
    private val _model = MainModel(this, context)

    override fun calculateSpeedConnection() {
       _model.calculateSpeedConnection()
    }

    override fun setSpeedConnection(speeds: List<Speed>) {
        _speedConnection.value = speeds
    }



}