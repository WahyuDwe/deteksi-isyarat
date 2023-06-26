package com.dwi.deti.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dwi.deti.R
import com.dwi.deti.databinding.ActivityMainBinding
import com.dwi.deti.ui.detection.DetectionActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabScan.setOnClickListener {
            Intent(this@MainActivity, DetectionActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}