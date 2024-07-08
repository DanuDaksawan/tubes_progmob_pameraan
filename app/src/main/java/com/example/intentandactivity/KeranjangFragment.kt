package com.example.intentandactivity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class KeranjangFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var keranjangAdapter: KeranjangAdapter
    private lateinit var firestore: FirebaseFirestore
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_keranjang, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewKeranjang)
        recyclerView.layoutManager = LinearLayoutManager(context)
        firestore = FirebaseFirestore.getInstance()
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        if (userId.isNotEmpty()) {
            fetchKeranjangItems()
        } else {
            Log.e("KeranjangFragment", "User not logged in")
        }

        return view
    }

    private fun fetchKeranjangItems() {
        firestore.collection("keranjang")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                val keranjangItems = mutableListOf<KeranjangItem>()
                for (document in documents) {
                    val id = document.id
                    val karyaId = document.getString("karyaId") ?: ""
                    val title = document.getString("title") ?: ""
                    val harga = document.getString("harga") ?: ""
                    val imageUrl = document.getString("imageUrl") ?: ""

                    keranjangItems.add(KeranjangItem(id ,userId, karyaId, title, harga, imageUrl))
                }
                keranjangAdapter = KeranjangAdapter(keranjangItems) { item ->
                    deleteKeranjangItem(item)
                }
                recyclerView.adapter = keranjangAdapter
            }
            .addOnFailureListener { exception ->
                Log.e("KeranjangFragment", "Error getting documents: ", exception)
            }
    }

    private fun deleteKeranjangItem(item: KeranjangItem) {
        item.id?.let {
            firestore.collection("keranjang")
                .document(it)
                .delete()
                .addOnSuccessListener {
                    Log.d("KeranjangFragment", "Item deleted successfully")
                    fetchKeranjangItems() // Refresh the list after deletion
                }
                .addOnFailureListener { exception ->
                    Log.e("KeranjangFragment", "Error deleting item: ", exception)
                }
        }
    }
}
