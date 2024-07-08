package com.example.intentandactivity.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.intentandactivity.activity.DetailKaryaActivity
import com.example.intentandactivity.model.Karya
import com.example.intentandactivity.adapter.KaryaAdapter
import com.example.intentandactivity.R
import com.example.intentandactivity.activity.SearchActivity
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

        val numberOfColumns = 2
        val layoutManager = StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager


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
            val products = itemkarya.documents.mapNotNull { doc ->
                val karya = doc.toObject(Karya::class.java)
                karya?.id = doc.id
                karya
            }

            karya.clear()
            karya.addAll(products)

            if (karya.isNotEmpty()) {
                adapter = KaryaAdapter(karya) { karya ->
                    val intent = Intent(requireContext(), DetailKaryaActivity::class.java)
                    intent.putExtra("karyaId", karya.id)
                    startActivity(intent)
                }
                recyclerView.adapter = adapter
            } else {
                checkForEmptyData()
            }

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
