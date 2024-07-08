package com.example.intentandactivity.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.intentandactivity.R
import com.example.intentandactivity.model.Karya
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class KaryaPribadiActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var backButton: ImageButton
    private lateinit var editButton: FloatingActionButton
    private lateinit var deleteButton: FloatingActionButton
    private lateinit var titleTextView: EditText
    private lateinit var descriptionTextView: EditText
    private lateinit var priceTextView: EditText
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_karya_pribadi)

        firestore = FirebaseFirestore.getInstance()

        // Inisialisasi semua view
        imageView = findViewById(R.id.imageKarya)
        titleTextView = findViewById(R.id.judulkarya)
        descriptionTextView = findViewById(R.id.deskripsi)
        priceTextView = findViewById(R.id.harga)
        backButton = findViewById(R.id.tombolx)
        editButton = findViewById(R.id.fab_edit)
        deleteButton = findViewById(R.id.fab_delete)

        backButton.setOnClickListener {
            finish()
        }

        val karyaId = intent.getStringExtra("karyaId")

        if (karyaId != null) {
            fetchKaryaDetails(karyaId)
        } else {
            Log.e("KaryaPribadiActivity", "karyaId is null")
            finish()
        }
    }

    private fun fetchKaryaDetails(karyaId: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser

        firestore.collection("products").document(karyaId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val karya = document.toObject(Karya::class.java)
                    karya?.let {
                        Glide.with(this)
                            .load(it.url)
                            .into(imageView)

                        titleTextView.setText(it.title)
                        descriptionTextView.setText(it.description)
                        priceTextView.setText(it.price.toString())

                        if (currentUser?.uid == it.userId) {
                            editButton.visibility = View.VISIBLE
                            deleteButton.visibility = View.VISIBLE
                        } else {
                            editButton.visibility = View.GONE
                            deleteButton.visibility = View.GONE
                        }

                        editButton.setOnClickListener {
                            val updatedTitle = titleTextView.text.toString().trim()
                            val updatedDescription = descriptionTextView.text.toString().trim()
                            val updatedPrice = priceTextView.text.toString().trim().toDoubleOrNull()

                            if (updatedTitle.isNotEmpty() && updatedDescription.isNotEmpty() && updatedPrice != null) {
                                updateKarya(karyaId, updatedTitle, updatedDescription, updatedPrice)
                            } else {
                                Toast.makeText(this, "Mohon lengkapi semua field", Toast.LENGTH_SHORT).show()
                            }
                        }

                        deleteButton.setOnClickListener {
                            firestore.collection("products").document(karyaId)
                                .delete()
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Karya berhasil dihapus", Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Gagal menghapus karya: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                } else {
                    Log.e("KaryaPribadiActivity", "No such document")
                    finish()
                }
            }.addOnFailureListener { exception ->
                Log.e("KaryaPribadiActivity", "Error getting documents: ", exception)
                editButton.visibility = View.GONE
                deleteButton.visibility = View.GONE
            }
    }

    private fun updateKarya(karyaId: String, updatedTitle: String, updatedDescription: String, updatedPrice: Double) {
        firestore.collection("products").document(karyaId)
            .update(
                mapOf(
                    "title" to updatedTitle,
                    "description" to updatedDescription,
                    "price" to updatedPrice
                )
            )
            .addOnSuccessListener {
                Toast.makeText(this, "Karya berhasil diperbarui", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Gagal memperbarui karya: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteKarya(karyaId: String) {
        firestore.collection("products").document(karyaId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Karya berhasil dihapus", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Gagal menghapus karya: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
