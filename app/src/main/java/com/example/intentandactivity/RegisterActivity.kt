package com.example.intentandactivity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val editTextEmail: EditText = findViewById(R.id.editTextEmail)
        val editTextUsername: EditText = findViewById(R.id.editTextUsername)
        val editTextName: EditText = findViewById(R.id.editTextName)
        val editTextPassword: EditText = findViewById(R.id.editTextPassword)

        val btnRegister: Button = findViewById(R.id.btnRegister)
        val loginButton: Button = findViewById(R.id.login)

        btnRegister.setOnClickListener {
            val email = editTextEmail.text.toString()
            val username = editTextUsername.text.toString()
            val name = editTextName.text.toString()
            val password = editTextPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty()) {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = firebaseAuth.currentUser?.uid
                            val user = hashMapOf(
                                "username" to username,
                                "name" to name
                            )

                            if (userId != null) {
                                firestore.collection("users").document(userId).set(user)
                                    .addOnSuccessListener {
                                        val intent = Intent(this, LoginActivity::class.java)
                                        startActivity(intent)
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Gagal Register: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        } else {
                            task.exception?.let { e ->
                                Toast.makeText(this, "Gagal Register: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
            } else {
                Toast.makeText(this, "Mohon isi semua kolom", Toast.LENGTH_SHORT).show()
            }
        }


        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}