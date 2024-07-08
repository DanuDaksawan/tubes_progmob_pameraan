package com.example.intentandactivity.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.example.intentandactivity.R
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : Activity() {
    private val SPLASH_TIME_OUT: Long = 2000 // Waktu tampilan Splash Screen (ms)
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen) // Ganti dengan layout yang Anda miliki (screen.xml)

        firebaseAuth = FirebaseAuth.getInstance()

        // Menggunakan Handler untuk menunda pemindahan ke MainActivity
        Handler().postDelayed({
            // Membuat intent untuk memulai MainActivity
            if (firebaseAuth.currentUser != null){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(this, WelcomePageActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, SPLASH_TIME_OUT)
    }
}