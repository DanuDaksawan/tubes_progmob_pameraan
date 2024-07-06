package com.example.intentandactivity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.add -> {
                    loadFragment(TambahKaryaFragment())
                    true
                }
                R.id.cart -> {
                    loadFragment(KeranjangFragment())
                    true
                }
                R.id.profil -> {
                    loadFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }

        // Load the default fragment
        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.home
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
