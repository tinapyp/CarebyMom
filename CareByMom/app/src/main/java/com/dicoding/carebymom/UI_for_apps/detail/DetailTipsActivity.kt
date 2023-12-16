package com.dicoding.carebymom.UI_for_apps.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.carebymom.data.Tips
import com.dicoding.carebymom.databinding.ActivityDetailTipsBinding

class DetailTipsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailTipsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTipsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<Tips>("DATA")

        binding.tvTitleTips.text = data?.name
        binding.tvDescTips.text = data?.description
        data?.photo?.let { binding.ivTips.setImageResource(it) }

    }
}