package com.dicoding.carebymom.UI_for_apps.result

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.carebymom.databinding.ActivityTestResultBinding

class TestResultActivity : AppCompatActivity() {

    private var _binding: ActivityTestResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTestResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val advice = intent.getStringExtra("advice")
        binding.tvTestResult.text = advice
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}