package com.dicoding.carebymom.UI_for_apps.health

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.carebymom.R
import com.dicoding.carebymom.databinding.ActivityHealthHelpBinding

class HealthHelpActivity : AppCompatActivity() {
    private var _binding: ActivityHealthHelpBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHealthHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idHelp = intent.getStringExtra("idHelp")

        if (idHelp == "arm") {
            binding.ivHelp.setImageResource(R.drawable.armcircum)
            binding.tvTitleHelp.text = getString(R.string.armHelp)
            binding.tvDescHelp.text = getString(R.string.armHelpDesc)
        }
        if (idHelp == "fundus") {
            binding.ivHelp.setImageResource(R.drawable.fundus)
            binding.tvTitleHelp.text = getString(R.string.fundusHelp)
            binding.tvDescHelp.text = getString(R.string.fundusHelpDesc)
        }
        if (idHelp == "heart") {
            binding.ivHelp.setImageResource(R.drawable.heartbeat)
            binding.tvTitleHelp.text = getString(R.string.heartHelp)
            binding.tvDescHelp.text = getString(R.string.heartHelpDesc)
        }
    }
}