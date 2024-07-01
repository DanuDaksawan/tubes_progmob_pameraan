package com.example.intentandactivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button

class RegisterActivity : Activity(), View.OnClickListener {
    private lateinit var btnRegister: Button
    private lateinit var textLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initComponents()
        btnRegister.setOnClickListener(this)
        textLogin.setOnClickListener(this)
    }

    private fun initComponents() {
        btnRegister = findViewById(R.id.btnregister)
        textLogin = findViewById(R.id.login)
    }

    override fun onClick(view: View?) {
        if (view?.id == R.id.btnregister) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else if (view?.id == R.id.login) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}