package com.example.intentandactivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button

class WelcomePageActivity : Activity() {
    private lateinit var startButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_page)

        initComponents()

        // Fungsi untuk menangani onClick event dari tombol "Masuk"
        startButton.setOnClickListener {
            // Pindah ke activity Login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initComponents() {
        startButton = findViewById(R.id.start)
    }
}