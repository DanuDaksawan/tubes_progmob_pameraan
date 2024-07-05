package com.example.intentandactivity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val buttonJelajahi = view.findViewById<Button>(R.id.jelajahi)
        val buttonMengikuti = view.findViewById<Button>(R.id.mengikuti)

        buttonJelajahi.setOnClickListener {
            loadFragment(JelajahiFragment())
        }

        buttonMengikuti.setOnClickListener {
            loadFragment(MengikutiFragment())
        }

        // Load the default fragment
        if (savedInstanceState == null) {
            loadFragment(JelajahiFragment())
        }

        return view
    }

    private fun loadFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
