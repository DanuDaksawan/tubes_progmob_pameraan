package com.example.intentandactivity.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.intentandactivity.R

class SuccessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)

        // Temukan tombol menggunakan ID-nya
        val btnOke: Button = findViewById(R.id.btn_ok)

        // Set OnClickListener untuk tombol
        btnOke.setOnClickListener {
            // Buat intent untuk kembali ke MainActivity dan menampilkan HomeFragment
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("navigateToHome", true)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}
