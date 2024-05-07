package com.example.intentandactivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button


class LoginActivity : Activity(), View.OnClickListener {

    private lateinit var btnLogin: Button
    private lateinit var textRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initComponents()
        btnLogin.setOnClickListener(this)
        textRegister.setOnClickListener(this)
    }

    private fun initComponents() {
        btnLogin = findViewById(R.id.BtnLogin)
        textRegister = findViewById(R.id.register)
    }

    override fun onClick(view: View?) {
        if (view?.id == R.id.BtnLogin) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else if (view?.id == R.id.register) {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}