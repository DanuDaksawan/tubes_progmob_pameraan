package com.example.intentandactivity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.firestore.FirebaseFirestore

class SearchActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var searchResultsRecyclerView: RecyclerView
    private lateinit var searchAdapter: KaryaAdapter
    private val searchResultsList = mutableListOf<Karya>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val numberOfColumns = 2
        val layoutManager = StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL)

        searchEditText = findViewById(R.id.search_edit_text)
        searchResultsRecyclerView = findViewById(R.id.hasilcari)
        val backButton = findViewById<ImageButton>(R.id.tombolx)

        searchAdapter = KaryaAdapter(searchResultsList) { karya ->
            val intent = Intent(this, DetailKaryaActivity::class.java)
            intent.putExtra("karyaId", karya.id) // Mengirim ID karya sebagai extra
            startActivity(intent)
        }
        searchResultsRecyclerView.layoutManager = layoutManager
        searchResultsRecyclerView.adapter = searchAdapter

        backButton.setOnClickListener {
            finish()
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            @SuppressLint("NotifyDataSetChanged")
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                if (query.isNotEmpty()) {
                    searchKarya(query)
                } else {
                    searchResultsList.clear()
                    searchAdapter.notifyDataSetChanged()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchKarya(query: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("products")
            .orderBy("title")
            .startAt(query)
            .endAt(query + "\uf8ff")
            .get()
            .addOnSuccessListener { querySnapshot ->
                searchResultsList.clear()
                for (document in querySnapshot.documents) {
                    val karya = document.toObject(Karya::class.java)
                    if (karya != null) {
                        karya.id = document.id // Menyimpan ID dokumen
                        searchResultsList.add(karya)
                    }
                }
                searchAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                // Handle the error
            }
    }
}
