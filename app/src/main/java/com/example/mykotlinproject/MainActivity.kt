package com.example.mykotlinproject

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mykotlinproject.databinding.MainActivityBinding
import com.example.mykotlinproject.ui.experiments.MainBroadcastReceiver
import com.example.mykotlinproject.ui.main.MainFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding
    private val receiver = MainBroadcastReceiver()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.container,  MainFragment.newInstance())
                    .commitNow()
        }

        registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }


    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }
}