package com.example.intentandactivity

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class DetailKaryaActivity : AppCompatActivity() {

    private lateinit var idKarya: String
    private lateinit var judulKarya: TextView
    private lateinit var deskripsiKarya: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_karya)

        judulKarya = findViewById(R.id.judulkarya)
        deskripsiKarya = findViewById(R.id.deskripsi)

        idKarya = intent.getStringExtra("karyaId") ?: return

        fetchKaryaDetails()
    }

    private fun fetchKaryaDetails() {
        val db = FirebaseFirestore.getInstance()
        val karyaRef = db.collection("products").document(idKarya)

        karyaRef.get().addOnSuccessListener { document ->
            if (document != null) {
                val karya = document.toObject(Karya::class.java)
                karya?.let {
                    judulKarya.text = it.title
                    // Set other fields as needed
                }
            } else {
                Log.e("DetailKaryaActivity", "No such document")
            }
        }.addOnFailureListener { exception ->
            Log.e("DetailKaryaActivity", "Error getting documents: ", exception)
        }
    }
}
