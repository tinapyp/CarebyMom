package com.dicoding.carebymom.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.carebymom.R
import com.dicoding.carebymom.ui.main.MainActivity
import java.util.Timer
import java.util.TimerTask

class SplashActivity : AppCompatActivity() {
    private val timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val intent = Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        val timerTask = object : TimerTask() {
            override fun run() {
                startActivity(intent)
                timer.cancel()
            }
        }

        timer.scheduleAtFixedRate(timerTask, 3000, 3000)
    }

}