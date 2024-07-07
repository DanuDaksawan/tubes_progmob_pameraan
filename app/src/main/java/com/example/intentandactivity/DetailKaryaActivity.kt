package com.example.intentandactivity

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DetailKaryaActivity : AppCompatActivity() {

    private lateinit var firestore:FirebaseFirestore
    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_karya)

        firestore = FirebaseFirestore.getInstance()

        val imageView: ImageView = findViewById(R.id.imageKarya)
        val titleTextView: TextView = findViewById(R.id.judulkarya)
        val descriptionTextView: TextView = findViewById(R.id.deskripsi)
        backButton = findViewById(R.id.tombolx)

        backButton.setOnClickListener{
            finish()
        }

        val karyaId = intent.getStringExtra("karyaId")

        if (karyaId !=null){
            fetchKaryaDetails(karyaId, imageView, titleTextView, descriptionTextView)
        } else {
            Log.e("DetailKaryaActivity", "karyaId is null")
            finish()
        }
    }

    private fun fetchKaryaDetails(karyaId: String, imageView: ImageView, titleTextView: TextView, descriptionTextView: TextView) {
        val currentUser = FirebaseAuth.getInstance().currentUser

        firestore.collection("products").document(karyaId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val karya = document.toObject(Karya::class.java)
                    karya?.let {
                        Glide.with(this)
                            .load(it.url)
                            .into((imageView))

                        titleTextView.text =  it.title
                        descriptionTextView.text = it.description
                    }
                } else {
                Log.e("DetailKaryaActivity", "No such document")
            }
        }.addOnFailureListener { exception ->
            Log.e("DetailKaryaActivity", "Error getting documents: ", exception)
        }
    }
}
