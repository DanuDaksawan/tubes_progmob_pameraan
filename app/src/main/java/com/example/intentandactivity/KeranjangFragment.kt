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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_keranjang, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewKeranjang)
        recyclerView.layoutManager = LinearLayoutManager(context)
        firestore = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            firestore.collection("keranjang")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { documents ->
                    val keranjangItems = mutableListOf<KeranjangItem>()
                    for (document in documents) {
                        val karyaId = document.getString("karyaId") ?: ""
                        val title = document.getString("title") ?: ""
                        val harga = document.getString("harga") ?: ""

                        keranjangItems.add(KeranjangItem(userId, karyaId, title, harga))
                    }
                    keranjangAdapter = KeranjangAdapter(keranjangItems)
                    recyclerView.adapter = keranjangAdapter
                }
                .addOnFailureListener { exception ->
                    Log.e("KeranjangFragment", "Error getting documents: ", exception)
                }
        } else {
            Log.e("KeranjangFragment", "User not logged in")
        }

        return view
    }
}
