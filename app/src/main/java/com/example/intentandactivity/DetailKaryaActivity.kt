package com.example.intentandactivity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DetailKaryaActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var backButton: ImageButton
    private lateinit var tambahKeranjangButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_karya)

        firestore = FirebaseFirestore.getInstance()

        val imageView: ImageView = findViewById(R.id.imageKarya)
        val titleTextView: TextView = findViewById(R.id.judulkarya)
        val descriptionTextView: TextView = findViewById(R.id.deskripsi)
        val hargaTextView: TextView = findViewById(R.id.harga)
        backButton = findViewById(R.id.tombolx)
        tambahKeranjangButton = findViewById(R.id.tambahkeranjang)

        backButton.setOnClickListener {
            finish()
        }

        val karyaId = intent.getStringExtra("karyaId")

        if (karyaId != null) {
            fetchKaryaDetails(karyaId, imageView, titleTextView, descriptionTextView, hargaTextView)
        } else {
            Log.e("DetailKaryaActivity", "karyaId is null")
            finish()
        }

        tambahKeranjangButton.setOnClickListener {
            if (karyaId != null) {
                tambahKeKeranjang(karyaId, titleTextView.text.toString(), hargaTextView.text.toString())
            }
        }
    }

    private fun fetchKaryaDetails(karyaId: String, imageView: ImageView, titleTextView: TextView, descriptionTextView: TextView, hargaTextView: TextView) {
        firestore.collection("products").document(karyaId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val karya = document.toObject(Karya::class.java)
                    karya?.let {
                        Glide.with(this)
                            .load(it.url)
                            .into((imageView))

                        titleTextView.text = it.title
                        descriptionTextView.text = it.description
                        hargaTextView.text = "Rp ${it.price}"
                    }
                } else {
                    Log.e("DetailKaryaActivity", "No such document")
                }
            }.addOnFailureListener { exception ->
                Log.e("DetailKaryaActivity", "Error getting documents: ", exception)
            }
    }

    private fun tambahKeKeranjang(karyaId: String, title: String, harga: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val keranjangItem = hashMapOf(
                "userId" to userId,
                "karyaId" to karyaId,
                "title" to title,
                "harga" to harga
            )

            firestore.collection("keranjang").add(keranjangItem)
                .addOnSuccessListener {
                    Log.d("DetailKaryaActivity", "Karya berhasil ditambahkan ke keranjang")
                    // Tambahkan pesan "Ditambahkan ke Keranjang" di sini
                    showToast("Ditambahkan ke Keranjang")
                }
                .addOnFailureListener { e ->
                    Log.e("DetailKaryaActivity", "Error adding to keranjang", e)
                }
        } else {
            Log.e("DetailKaryaActivity", "User not logged in")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
