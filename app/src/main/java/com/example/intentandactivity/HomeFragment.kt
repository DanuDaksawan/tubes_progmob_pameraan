package com.example.intentandactivity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noArtTextView: TextView
    private lateinit var adapter: KaryaAdapter
    private val karya = mutableListOf<Karya>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        noArtTextView = view.findViewById(R.id.belumAdaKaryaText)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val numberOfColumns = 2
        val layoutManager = GridLayoutManager(requireContext(), numberOfColumns)
        recyclerView.layoutManager = layoutManager

        adapter = KaryaAdapter(karya)
        recyclerView.adapter = adapter

        val buttonSearch = view.findViewById<ImageButton>(R.id.tombolsearch)
        buttonSearch.setOnClickListener {
            val intent = Intent(requireActivity(), SearchActivity::class.java)
            startActivity(intent)
        }

        fetchKarya()
        return view
    }

    private fun fetchKarya() {
        val db = FirebaseFirestore.getInstance()
        val karyaRef = db.collection("products")

        karyaRef.get().addOnSuccessListener { itemkarya ->
            val products = itemkarya.toObjects(Karya::class.java)
            karya.addAll(products)
            adapter.notifyDataSetChanged()
            checkForEmptyData()
        }.addOnFailureListener {
            checkForEmptyData()
        }
    }

    private fun checkForEmptyData() {
        if (karya.isEmpty()) {
            noArtTextView.visibility = View.VISIBLE
        } else {
            noArtTextView.visibility = View.GONE
        }
    }
}