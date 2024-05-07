package com.example.intentandactivity

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.os.Handler

class SplashScreenActivity : Activity() {
    private val SPLASH_TIME_OUT: Long = 2000 // Waktu tampilan Splash Screen (ms)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen) // Ganti dengan layout yang Anda miliki (screen.xml)

        // Menggunakan Handler untuk menunda pemindahan ke MainActivity
        Handler().postDelayed({
            // Membuat intent untuk memulai MainActivity
            val intent = Intent(this, WelcomePageActivity::class.java)
            startActivity(intent)
            finish() // Menutup SplashActivity agar tidak bisa kembali
        }, SPLASH_TIME_OUT)
    }
}
