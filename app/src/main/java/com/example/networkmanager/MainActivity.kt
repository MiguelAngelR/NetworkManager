package com.example.networkmanager

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.commons.network.NetworkUsageManager
import com.example.commons.network.Speed
import com.example.commons.network.Util
import com.example.networkmanager.contract.MainContract
import com.example.networkmanager.databinding.ActivityMainBinding
import com.example.networkmanager.presenter.MainPresenter

class MainActivity : AppCompatActivity(), MainContract.ViewMain {

    private lateinit var binding: ActivityMainBinding
    private var presenter: MainPresenter? = null

    private val internetPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            permissions ->
            when{
                permissions[Manifest.permission.READ_PHONE_STATE] == true -> {
                    presenter?.calculateSpeedConnection()
                }
                else -> {
                    Toast.makeText(this@MainActivity, R.string.request_permissions, Toast.LENGTH_LONG).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
        subscribeUI()
    }

    private fun initUI() {
        presenter = MainPresenter(this@MainActivity, this)
        internetPermissionRequest.launch(
            arrayOf(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE
            )
        )
    }

    private fun subscribeUI() {
        presenter?.speedConnection?.observe(this, Observer<List<Speed>>{
            binding.run {
                tvConnectionSpeed.text =  "${it.get(0).speed} ${it.get(0).unit}"
                tvDownConnectionSpeed.text =  "Bajada: ${it.get(1).speed} ${it.get(1).unit}"
                tvUpConnectionSpeed.text =  "Subida: ${it.get(2).speed} ${it.get(2).unit}"
            }
        })
    }

}