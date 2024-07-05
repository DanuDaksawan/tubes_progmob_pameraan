package com.example.intentandactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class JelajahiFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var karyaAdapter: KaryaAdapter
    private lateinit var belumAdaKaryaText: TextView
    private val karyaList: MutableList<Karya> = mutableListOf() // List untuk menyimpan data produk

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        belumAdaKaryaText = view.findViewById(R.id.belumAdaKaryaText)

        val numberOfColumns = 2
        val layoutManager = GridLayoutManager(requireContext(), numberOfColumns)
        recyclerView.layoutManager = layoutManager

        karyaAdapter = KaryaAdapter(karyaList)
        recyclerView.adapter = karyaAdapter

        loadProducts()

        return view
    }

    private fun loadProducts() {
        val db = FirebaseFirestore.getInstance()
        db.collection("products")
            .get()
            .addOnSuccessListener { querySnapshot ->
                karyaList.clear()
                if (querySnapshot.isEmpty) {
                    recyclerView.visibility = View.GONE
                    belumAdaKaryaText.visibility = View.VISIBLE
                } else {
                    recyclerView.visibility = View.VISIBLE
                    belumAdaKaryaText.visibility = View.GONE

                    for (document in querySnapshot.documents) {
                        val productName = document.getString("title") ?: ""
                        val productDescription = document.getString("description") ?: ""
                        val productImageUrl = document.getString("url")

                        karyaList.add(Karya(productName, productDescription, productImageUrl))
                    }
                    karyaAdapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Failed to load products: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}